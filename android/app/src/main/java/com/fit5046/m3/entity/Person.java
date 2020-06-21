package com.fit5046.m3.entity;

import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName(value = "personid")
    private int pid;
    private String fname;
    private String lname;
    private int gender;
    private String stat;
    private String address;
    private String postcode;
    private String dob;
    // following fields are used for sign up, so the info can be sent to server in one request.
    private String email;
    private String password;
    private String signupDate;

    public Person() {
    }

    public Person(int pid, String fname, String lname, int gender,
                  String stat, String address, String postcode, String dob) {
        this.pid = pid;
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.stat = stat;
        this.address = address;
        this.postcode = postcode;
        this.dob = dob;
    }

    public Person(String fname, String lname, int gender, String stat, String address,
                  String postcode, String dob, String email, String password, String signupDate) {
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.stat = stat;
        this.address = address;
        this.postcode = postcode;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.signupDate = signupDate;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
