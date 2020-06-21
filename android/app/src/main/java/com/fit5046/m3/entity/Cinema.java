package com.fit5046.m3.entity;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class Cinema {

    private int cinemaid;
    private String name;
    private String postcode;

    public Cinema(int cinemaid, String name, String postcode) {
        this.cinemaid = cinemaid;
        this.name = name;
        this.postcode = postcode;
    }

    public int getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(int cinemaid) {
        this.cinemaid = cinemaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @NotNull
    @Override
    public String toString() {
        return cinemaid + ", " + name + ", " + postcode;
    }
}
