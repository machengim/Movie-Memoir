package com.fit5046.m3.entity;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// Used for read json result of movie detail.
// including genres, countries, rating and overview.
// cast and crew are not included.
public class MovieDetail{

    private List<Genre> genres;
    @SerializedName("production_countries")
    private List<ProductionCountry> countries;
    @SerializedName("vote_average")
    private float rating;
    private String overview;

    public MovieDetail(List<Genre> genres, List<ProductionCountry> countries, float rating) {
        this.genres = genres;
        this.countries = countries;
        this.rating = rating;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<ProductionCountry> getCountries() {
        return countries;
    }

    public void setCountries(List<ProductionCountry> countries) {
        this.countries = countries;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Genres: ");
        for (Genre g: genres) {
            output.append(g.getName()).append(" ");
        }

        output.append("Countries: ");
        for (ProductionCountry c: countries) {
            output.append(c.getName()).append(" ");
        }

        output.append("Rating: ").append(rating);

        return output.toString();
    }
}
