package com.fit5046.m3.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Watchlist {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "movie_name")
    public String movieName;

    @ColumnInfo(name = "release_date")
    public String release;

    @ColumnInfo(name = "add_time")
    public String addTime;

    public Watchlist(Movie movie, String addTime) {
        this.addTime = addTime;
        this.id = movie.getId();
        this.movieName = movie.getTitle();
        this.release = movie.getReleaseDate();
    }

    public Watchlist(int id, String movieName, String release, String addTime) {
        this.id = id;
        this.movieName = movieName;
        this.release = release;
        this.addTime = addTime;
    }
}
