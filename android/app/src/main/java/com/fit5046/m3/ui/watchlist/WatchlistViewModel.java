package com.fit5046.m3.ui.watchlist;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fit5046.m3.entity.Watchlist;
import com.fit5046.m3.lib.WatchlistRepository;

import java.util.List;

public class WatchlistViewModel extends ViewModel {

    private WatchlistRepository repository;
    private MutableLiveData<List<Watchlist>> movies;

    public WatchlistViewModel() {
        movies = new MutableLiveData<>();
    }

    public MutableLiveData<List<Watchlist>> getMovies() {
        return movies;
    }

    public void setMovies(List<Watchlist> movies) {
        this.movies.postValue(movies);
    }

    public void init(Application app) {
        repository = new WatchlistRepository(app);
    }

    public void findAllList() {
        repository.getAllLists(movies);
    }

    public void remove(Watchlist watchlist) {
        repository.delete(watchlist, this);
    }

}
