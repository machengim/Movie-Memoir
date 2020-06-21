package com.fit5046.m3;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.lib.NetworkConnection;
import com.fit5046.m3.lib.Utils;
import com.google.gson.JsonObject;

import java.security.NoSuchAlgorithmException;

// This viewmodel is used for Login activity and Main activity.
public class MainViewModel extends ViewModel {

    private final MutableLiveData<Integer> userId;
    // share selected movie info between search and movieview fragments.
    private Movie movie;

    public MainViewModel() {
        userId = new MutableLiveData<>(0);
    }

    public MutableLiveData<Integer> getUserId() {
        return userId;
    }

    void setUserId(Integer id) {
        userId.setValue(id);
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    // Hash password and start another thread to send request.
    void login(String email, String password) {
        String hashPw = "";

        try {
            hashPw = Utils.hash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }

        JsonObject jo = new JsonObject();
        jo.addProperty("email", email);
        jo.addProperty("password", hashPw);

        Thread t = new Thread(() -> loginRequest(jo));
        t.start();
    }

    // Send login request and handle the response from server.
    private void loginRequest(JsonObject jo) {
        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(4, "m3.credential/login");
        String output = conn.request("post", jo.toString());
        int id = Integer.parseInt(output);
        userId.postValue(id);
    }
}
