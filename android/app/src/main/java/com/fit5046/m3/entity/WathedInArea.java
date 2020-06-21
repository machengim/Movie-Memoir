package com.fit5046.m3.entity;

// // used to store the results of watched movies over areas.
public class WathedInArea {

    private int count;
    private String postcode;

    public WathedInArea(int count, String postcode) {
        this.count = count;
        this.postcode = postcode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
