package com.fit5046.m3.ui.search;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.entity.SearchResult;
import com.fit5046.m3.lib.AsyncResponse;
import com.fit5046.m3.lib.AsyncRequest;
import com.fit5046.m3.lib.ImageHandler;
import com.fit5046.m3.lib.ImageLoader;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

// used for search screen.
public class SearchViewModel extends ViewModel implements AsyncResponse, ImageHandler {

    private MutableLiveData<List<Movie>> movies;
    private MutableLiveData<Boolean> newImage;

    public SearchViewModel() {
        movies = new MutableLiveData<>();
        newImage = new MutableLiveData<>();
        newImage.setValue(false);
    }

    MutableLiveData<List<Movie>> getMovies() {
        return movies;
    }

    MutableLiveData<Boolean> getNewImage() {
        return newImage;
    }

    public void setNewImage(Boolean b) {
        this.newImage.setValue(b);
    }

    // Use AsyncTask to request movies from TMDB using user input as keyword.
    void searchMovie(String keyword) {
        AsyncRequest request = new AsyncRequest(2, keyword, this);
        request.execute();
    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        SearchResult result = gson.fromJson(output, SearchResult.class);
        this.movies.setValue(result.getMovies());
        // After movie info retrieved, download posters if exist.
        for (Movie movie: Objects.requireNonNull(result.getMovies())) {
            if (movie.getPosterPath() != null) {
                ImageLoader loader = new ImageLoader(movie, 200, this);
                loader.execute();
            }
        }
    }

    @Override
    public void handleImage(Bitmap image, Movie movie) {
        movie.setImage(image);
        newImage.setValue(true);
    }
}
