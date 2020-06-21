package com.fit5046.m3.entity;

// used only to parse the json response of the TMDB api about movie detail.
public class ProductionCountry {
    private int id;
    private String name;

    public ProductionCountry(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
