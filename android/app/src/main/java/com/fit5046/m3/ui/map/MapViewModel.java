package com.fit5046.m3.ui.map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fit5046.m3.entity.Location;
import com.fit5046.m3.entity.Memoir;
import com.fit5046.m3.entity.Person;
import com.fit5046.m3.lib.NetworkConnection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapViewModel extends ViewModel {

    // store the user address according to the record in server.
    private MutableLiveData<Location> userLoc;
    // store the cinema locations.
    private MutableLiveData<List<Location>> cinemaLoc;

    public MapViewModel() {
        userLoc = new MutableLiveData<>();
        cinemaLoc = new MutableLiveData<>();
    }

    public MutableLiveData<Location> getUserLoc() {
        return userLoc;
    }

    public void setUserLoc(Location userLoc) {
        this.userLoc.setValue(userLoc);
    }

    public MutableLiveData<List<Location>> getCinemaLoc() {
        return cinemaLoc;
    }

    public void setCinemaLoc(List<Location> cinemaLoc) {
        this.cinemaLoc.setValue(cinemaLoc);
    }

    // Start two threads to retrieve user address and cinemas respectively.
    public void init(int uid) {
        Thread t1 = new Thread(() -> retrieveUserInfo(uid));
        t1.start();
        Thread t2 = new Thread(() -> retrieveCinemas(uid));
        t2.start();
    }

    // Get user address from server and translate into location.
    private void retrieveUserInfo(int uid) {
        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(1, "m3.person/" + uid);
        String output = conn.request("get", null);
        Person person = new Gson().fromJson(output, Person.class);
        if (person == null) return;

        String address = person.getAddress() + ", " + person.getStat() + " " + person.getPostcode();
        Location loc = translateLoc(address);
        if (loc == null) return;
        loc.setOwner("You");

        this.userLoc.postValue(loc);
    }

    // Get memoirs from server, retrieve cinema info and translate into locatinos.
    private void retrieveCinemas(int uid) {
        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(1, "m3.memoir/person/" + uid);
        String output = conn.request("get", null);
        Memoir[] memoirs = new Gson().fromJson(output, Memoir[].class);
        Set<String> addresses = new HashSet<>();
        for (Memoir m: memoirs) {
            String s = m.getCinema().getName() + ", " + m.getCinema().getPostcode();
            addresses.add(s);
        }

        List<Location> nList = new ArrayList<>();
        for (String address: addresses) {
            Location location = translateLoc(address);
            if (location == null) continue;
            location.setOwner(address.split(",")[0]);
            nList.add(location);
        }

        this.cinemaLoc.postValue(nList);
    }

    // Translation function. Request sends to Google API.
    private Location translateLoc(String address) {
        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(5, address);
        String output = conn.request("get", null);

        // Do not want to create entities for the nested structure, so parse it manually.
        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(output).getAsJsonObject();
        JsonArray results = rootObj.getAsJsonArray("results");
        if (results.size() == 0) return null;
        JsonObject jo = results.get(0).getAsJsonObject()
                .getAsJsonObject("geometry").getAsJsonObject("location");
        float lat = jo.get("lat").getAsFloat();
        float lon = jo.get("lng").getAsFloat();

        return  new Location(lon, lat);
    }
}
