package com.fit5046.m3.ui.addmemoir;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fit5046.m3.entity.Cinema;
import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.lib.NetworkConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

public class AddMemoirViewModel extends ViewModel{

    private MutableLiveData<String> watchTime;
    private MutableLiveData<String> watchDate;
    // Cinema list which will be displayed in the spinner.
    private MutableLiveData<List<Cinema>> cinemas;
    // The info responded from the server.
    private MutableLiveData<String> info;
    // Whether the memoir was saved successfully.
    private MutableLiveData<Integer> sent;
    // whether the tweet was sent successfully.
    private MutableLiveData<Integer> tweetSent;
    // Prepared tweet contents for user to send.
    private String message;

    public AddMemoirViewModel() {
        watchTime = new MutableLiveData<>();
        watchDate = new MutableLiveData<>();
        cinemas = new MutableLiveData<>();
        info = new MutableLiveData<>();
        sent = new MutableLiveData<>();
        tweetSent = new MutableLiveData<>();
        sent.setValue(0);
        tweetSent.setValue(0);
        getCinemaList();
    }


    MutableLiveData<String> getWatchTime() {
        return watchTime;
    }

    void setWatchTime(String  watchTime) {
        this.watchTime.setValue(watchTime);
    }

    MutableLiveData<String> getWatchDate() {
        return watchDate;
    }

    void setWatchDate(String watchDate) {
        this.watchDate.setValue(watchDate);
    }

    public MutableLiveData<List<Cinema>> getCinemas() {
        return cinemas;
    }

    public void setCinemas(List<Cinema> cinemas) {
        this.cinemas.setValue(cinemas);
    }

    public MutableLiveData<String> getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info.setValue(info);
    }

    public MutableLiveData<Integer> getTweetSent() {
        return tweetSent;
    }

    public void setTweetSent(Integer tweetSent) {
        this.tweetSent.setValue(tweetSent);
    }

    public MutableLiveData<Integer> getSent() {
        return sent;
    }

    public void setSent(Integer sent) {
        this.sent.setValue(sent);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // used when user inputs the new cinema info and click submit.
    void addCinema(String name, String postcode) {
        if (name == null || postcode == null || name.length() == 0 || postcode.length() == 0) return;

        Thread t = new Thread(() -> processAddCinema(name, postcode));
        t.start();
    }

    // get memoir info from user input and prepare to send request to server.
    void addMemoir(Movie movie, String time, int cid, int uid, float rating, String comment) {
        JsonObject jo = new JsonObject();
        jo.addProperty("moviename", movie.getTitle());
        jo.addProperty("release", movie.getReleaseDate());
        jo.addProperty("watch", time);
        jo.addProperty("cid", cid);
        jo.addProperty("uid", uid);
        jo.addProperty("rating", Float.toString(rating));
        jo.addProperty("comment", comment);
        jo.addProperty("mid", movie.getId());

        message = "I just watched movie " + movie.getTitle() + " and rated it " +
                rating + " stars.";
        if (rating >= 4.0) message += " I think it's really nice.";
        else if (rating >= 3.0) message += " It's worth a try.";
        else message += " It's not my favorite.";

        Thread t = new Thread(() -> processAddMemoir(jo));
        t.start();
    }

    // Start another thread to get cinema list.
    private void getCinemaList() {
        Thread t = new Thread(this::processCinemaList);
        t.start();
    }

    // Send request to add cinema to server.
    private void processAddCinema(String name, String postcode) {
        NetworkConnection conn = new NetworkConnection();
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("postcode", postcode);

        conn.buildUrl(1, "m3.cinema/newcinema");
        String output = conn.request("post", json.toString());
        this.info.postValue(output);
        if (output.contains("Success")) {
            getCinemaList();
        }
    }

    // Send request to add memoir to the server.
    private void processAddMemoir(JsonObject jo) {
        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(1, "m3.memoir/new");
        String output = conn.request("post", jo.toString());
        if (output.contains("Success")) {
            this.sent.postValue(1);
        } else {
            this.sent.postValue(2);
        }
    }

    // Send request to server to get the cinema list.
    private void processCinemaList() {
        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(1, "m3.cinema");
        String output = conn.request("get", null);
        Gson gson = new Gson();
        Cinema[] nList = gson.fromJson(output, Cinema[].class);
        if (nList != null && nList.length > 0) {
            this.cinemas.postValue(Arrays.asList(nList));
        }
    }

}
