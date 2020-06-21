package com.fit5046.m3.ui.memoir;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fit5046.m3.entity.Cast;
import com.fit5046.m3.entity.Memoir;
import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.entity.MovieDetail;
import com.fit5046.m3.lib.ImageHandler;
import com.fit5046.m3.lib.ImageLoader;
import com.fit5046.m3.lib.NetworkConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MemoirViewModel extends ViewModel {

    private MutableLiveData<List<Memoir>> memoirs;
    private MutableLiveData<Set<String>> cinemas;
    private MutableLiveData<MovieDetail> detail;
    private MutableLiveData<Cast> cast;
    // used to inform the fragment the recycler view contends were changed.
    private MutableLiveData<Boolean> change;

    public MemoirViewModel() {
        memoirs = new MutableLiveData<>();
        detail = new MutableLiveData<>();
        cast = new MutableLiveData<>();
        cinemas = new MutableLiveData<>();
        change = new MutableLiveData<>();
    }

    MutableLiveData<List<Memoir>> getMemoirs() {
        return memoirs;
    }

    public void setMemoirs(List<Memoir> memoirs) {
        this.memoirs.setValue(memoirs);
    }

    public MutableLiveData<Set<String>> getCinemas() {
        return cinemas;
    }

    private void setCinemas(Set<String> cinemas) {
        this.cinemas.setValue(cinemas);
    }

    public MutableLiveData<MovieDetail> getDetail() {
        return detail;
    }

    public MutableLiveData<Cast> getCast() {
        return cast;
    }

    public MutableLiveData<Boolean> getChange() {
        return change;
    }

    public void setChange(Boolean change) {
        this.change.setValue(change);
    }

    // start a new thread to retrieve memoirs.
    void retrieveMemoirs(int pid) {
        if (pid <= 0) return;

        Thread t = new Thread(() -> processMemoirRetrieve(pid));
        t.start();
    }

    // Used when user selects a memoir on memoir page, and the movie detail should be retrieved.
    void retrieveMovieInfo(int mid) {
        if (mid < 0) return;

        Thread t1 = new Thread(() -> processMovieRetrieve(mid, 1));
        t1.start();
        Thread t2 = new Thread(() -> processMovieRetrieve(mid, 2));
        t2.start();
    }

    // perform retrieve task from TMDB api.
    // op 1 means retrieving movie detail, 2 means retrieving cast.
    private void processMovieRetrieve(int mid, int op) {
        NetworkConnection conn = new NetworkConnection();

        switch (op) {
            case 1:
                conn.buildUrl(3, mid + ""); break;
            case 2:
                conn.buildUrl(3, mid + "/credits"); break;
            default:
                return;
        }

        String output = conn.request("get", null);
        if (op == 1)
            setMovieDetail(output);
        else
            setMovieCast(output);
    }

    // process the response for movie detail.
    private void setMovieDetail(String output) {
        Gson gson = new Gson();
        MovieDetail detail = gson.fromJson(output, MovieDetail.class);
        if (detail == null) return;

        this.detail.postValue(detail);
    }

    // process the response for movie cast.
    private void setMovieCast(String output) {
        Gson gson = new Gson();
        Cast cast = gson.fromJson(output, Cast.class);
        if (cast == null) return;

        this.cast.postValue(cast);
    }

    // process the response of memoirs of the user.
    private void processMemoirRetrieve(int pid) {
        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(1, "m3.memoir/person/" + pid);
        String output = conn.request("get", null);
        Gson gson = new Gson();
        Memoir[] mList = gson.fromJson(output, Memoir[].class);
        this.memoirs.postValue(Arrays.asList(mList));

        retrieveCinemas();
    }

    // revoked by fragment when the memoirs has been set.
    void retrievePosters() {
        if (memoirs.getValue() == null) return;

        for (Memoir m: memoirs.getValue()) {
            if (m.getPoster() != null) continue;
            Thread t = new Thread(() -> requestPoster(m));
            t.start();
        }
    }

    // send request to poster and connect it with memoir object after getting response.
    private void requestPoster(Memoir m) {
        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(3, Integer.toString(m.getMid()));
        String output = conn.request("get", null);

        JsonParser parser = new JsonParser();
        JsonObject jo = parser.parse(output).getAsJsonObject();
        String posterPath = jo.get("poster_path").getAsString();
        conn.buildUrl(6, 200 + posterPath);
        Bitmap image = conn.requestImage();

        m.setPoster(image);
        this.change.postValue(true);
    }

    // retrieve cinemas after the memoirs have been retrieved.
    private void retrieveCinemas() {
        while (this.memoirs == null || this.memoirs.getValue() == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Set<String> nSet = new HashSet<>();
        nSet.add("All");
        for (Memoir m: this.memoirs.getValue()) {
            nSet.add(m.getCinema().getName());
        }

        this.cinemas.postValue(nSet);
        System.out.println("retrieve cinemas complete");
    }

    // sort memoirs by different keys according to the user selection.
    void sortMemoirs(int op) {
        if (this.memoirs == null || this.memoirs.getValue() == null) return;
        List<Memoir> nList = memoirs.getValue();

        switch (op) {
            case 0:
                nList = this.memoirs.getValue().stream()
                        .sorted(Comparator.comparing(Memoir::getMid))
                        .collect(Collectors.toList());
                break;
            case 1:
                nList = this.memoirs.getValue().stream()
                        .sorted(Comparator.comparing(Memoir::getReleaseDate).reversed())
                        .collect(Collectors.toList());
                break;
            case 2:
                nList = this.memoirs.getValue().stream()
                        .sorted(Comparator.comparing(Memoir::getWatchTime).reversed())
                        .collect(Collectors.toList());
                break;
            case 3:
                nList = this.memoirs.getValue().stream()
                        .sorted(Comparator.comparing(Memoir::getScore).reversed())
                        .collect(Collectors.toList());
                break;
            default:
                break;
        }

        this.memoirs.setValue(nList);
    }

}
