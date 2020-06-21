package com.fit5046.m3.entity;

// used to parse the response from google geocode api.
public class Location {
    // set an owner for each location to display on the map.
    private String owner;
    private float lon;
    private float lat;

    public Location(float lon, float lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public Location(String owner, float lon, float lat) {
        this.owner = owner;
        this.lon = lon;
        this.lat = lat;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }
}
