package com.fit5046.m3.ui.movieview;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fit5046.m3.entity.Cast;
import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.entity.MovieDetail;
import com.fit5046.m3.lib.NetworkConnection;
import com.fit5046.m3.lib.WatchlistRepository;
import com.google.gson.Gson;

import java.util.Objects;

public class MovieviewViewModel extends ViewModel {

    private WatchlistRepository repository;
    private MutableLiveData<Movie> movie;
    private MutableLiveData<MovieDetail> detail;
    private MutableLiveData<Cast> cast;
    // indicate whether this movie existed in database.
    private MutableLiveData<Boolean> existInWatchList;

    public MovieviewViewModel() {
        movie = new MutableLiveData<>();
        detail = new MutableLiveData<>();
        cast = new MutableLiveData<>();
        existInWatchList = new MutableLiveData<>();
        existInWatchList.setValue(false);
    }

    LiveData<Movie> getMovie() {
        return movie;
    }

    void setMovie(Movie movie) {
        this.movie.setValue(movie);
    }

    MutableLiveData<MovieDetail> getDetail() {
        return detail;
    }

    MutableLiveData<Cast> getCast() {
        return cast;
    }

    MutableLiveData<Boolean> getExistInWatchList() {
        return existInWatchList;
    }

    // Init the object with repository and movie info.
    void init(Application app, Movie movie) {
        repository = new WatchlistRepository(app);
        this.movie.setValue(movie);
    }

    // get movie detail and cast and check exists. invoked from fragment.
    void getMovieDetail() {
        getMovieDetail(1);
        getMovieDetail(2);
        repository.checkExist(movie.getValue(), this);
    }

    // start a new thread to get movie detail from TMDB.
    private void getMovieDetail(int op) {
        if (Objects.requireNonNull(movie.getValue()).getId() < 0) return;

        int id = movie.getValue().getId();
        Thread t = new Thread(() -> requestDetail(id, op));
        t.start();
    }

    // request movie info using NetworkConnection class.
    // option is the action type. 1 means getting movie detail, 2 means getting cast.
    private void requestDetail(int id, int op) {
        StringBuilder url = new StringBuilder("https://api.themoviedb.org/3/movie/").append(id) ;
        NetworkConnection conn = new NetworkConnection();

        switch (op) {
            case 1:
                conn.buildUrl(3, id + "");
                break;
            case 2:
                conn.buildUrl(3, id + "/credits");
                break;
            default:
                return;
        }

        String output = conn.request("get", null);
        if (op == 1)
            setMovieDetail(output);
        else
            setMovieCast(output);
    }

    // Set detail value according to the response from server.
    private void setMovieDetail(String output) {
        Gson gson = new Gson();
        MovieDetail detail = gson.fromJson(output, MovieDetail.class);
        if (detail == null) return;

        this.detail.postValue(detail);
    }

    // Set cast value according to the response from server.
    private void setMovieCast(String output) {
        Gson gson = new Gson();
        Cast cast = gson.fromJson(output, Cast.class);
        if (cast == null) return;

        //System.out.println(cast);
        this.cast.postValue(cast);
    }

    // Change the exist value to true if existed.
    public void confirmExistInWatchlist() {
        existInWatchList.postValue(true);
    }

}
