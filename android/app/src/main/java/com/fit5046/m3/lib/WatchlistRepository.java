package com.fit5046.m3.lib;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.entity.Watchlist;
import com.fit5046.m3.ui.movieview.MovieviewViewModel;
import com.fit5046.m3.ui.watchlist.WatchlistViewModel;

import java.util.List;

// Repository for watchlist.
public class WatchlistRepository {

    private WatchlistDao dao;

    public WatchlistRepository(Application app) {
        Database db = Database.getInstance(app);
        dao = db.watchlistDao();
    }

    public void getAllLists(MutableLiveData<List<Watchlist>> lists) {
        Database.executor.execute(new Runnable() {
            @Override
            public void run() {
                lists.postValue(dao.getAll());
            }
        });
    }

    public void insert(final Watchlist watchlist) {
        Database.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(watchlist);
            }
        });
    }

    public void update(final Watchlist watchlist) {
        Database.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.update(watchlist);
            }
        });
    }

    public void delete(final Watchlist watchlist, WatchlistViewModel model) {
        Database.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(watchlist);
                model.setMovies(dao.getAll());
            }
        });
    }

    public void checkExist(final Movie movie, MovieviewViewModel model) {
        Database.executor.execute(new Runnable() {
            @Override
            public void run() {
                Watchlist w = dao.findByID(movie.getId());
                if (w != null) {
                    model.confirmExistInWatchlist();
                }
            }
        });
    }

}
