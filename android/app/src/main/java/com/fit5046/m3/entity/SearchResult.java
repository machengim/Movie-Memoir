package com.fit5046.m3.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// used only to parse the json result of TMDB after searching movie by keyword.
public class SearchResult {
    private int page;
    private int totalPage;
    private int totalResults;
    @SerializedName("results")
    private List<Movie> movies;

    public SearchResult(int page, int totalPage, int totalResults, List<Movie> movies) {
        this.page = page;
        this.totalPage = totalPage;
        this.totalResults = totalResults;
        this.movies = movies;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
