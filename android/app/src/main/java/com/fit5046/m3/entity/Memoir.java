package com.fit5046.m3.entity;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Memoir {
    @SerializedName("movieid")
    private int mid;
    @SerializedName(value = "moviename", alternate = "name")
    private String movieName;
    @SerializedName(value = "moviereleasedate", alternate = "releaseDate")
    private String releaseDate;
    @SerializedName(value = "watchtime", alternate = "watchTime")
    private String watchTime;
    private String comment;
    private float score;
    @SerializedName(value = "personid")
    private Person person;
    @SerializedName(value = "cinemaid")
    private Cinema cinema;
    // store poster of this movie.
    private Bitmap poster = null;

    public Memoir() {
    }

    public Memoir(int mid, String movieName, String releaseDate, String watchTime, String comment,
                  float score, Person person, Cinema cinema) {
        this.mid = mid;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.watchTime = watchTime;
        this.comment = comment;
        this.score = score;
        this.person = person;
        this.cinema = cinema;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getWatchTime() {
        return watchTime;
    }

    public void setWatchTime(String watchTime) {
        this.watchTime = watchTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }
}
