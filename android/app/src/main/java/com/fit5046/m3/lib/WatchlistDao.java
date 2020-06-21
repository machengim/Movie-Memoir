package com.fit5046.m3.lib;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fit5046.m3.entity.Watchlist;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

// Database access object for watchlist.
@Dao
public interface WatchlistDao {

    @Query("SELECT * FROM watchlist")
    List<Watchlist> getAll();

    @Query("SELECT * FROM watchlist WHERE id = :id LIMIT 1")
    Watchlist findByID(int id);

    @Insert
    void insertAll(Watchlist... watchlists);

    @Insert
    long insert(Watchlist watchlist);

    @Delete
    void delete(Watchlist watchlist);

    @Update(onConflict = REPLACE)
    void update(Watchlist watchlist);

    @Query("DELETE FROM watchlist")
    void deleteAll();

}
