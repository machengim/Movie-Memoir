package com.fit5046.m3.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fit5046.m3.entity.Memoir;
import com.fit5046.m3.entity.Person;
import com.fit5046.m3.lib.AsyncResponse;
import com.fit5046.m3.lib.AsyncRequest;
import com.fit5046.m3.lib.NetworkConnection;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

// Viewmodel for home fragment.
public class HomeViewModel extends ViewModel implements AsyncResponse {

    private MutableLiveData<List<Memoir>> memoirs;
    private MutableLiveData<String> userFname;

    public HomeViewModel() {
        memoirs = new MutableLiveData<>();
        userFname = new MutableLiveData<>("Cheng");
    }

    LiveData<List<Memoir>> getMemoirs() {
        return memoirs;
    }

    public MutableLiveData<String> getUserFname() {
        return userFname;
    }

    public void setUserFname(MutableLiveData<String> userFname) {
        this.userFname = userFname;
    }

    // Get the top five movies this year to display on home screen.
    private void getTop5(int uid) {
        AsyncRequest request = new AsyncRequest(1,
                "m3.memoir/topFive/" + uid, this);
        request.execute();
    }

    // retrieve the user first name from server when this view model starts.
    public void init(int uid) {
        Thread t = new Thread(() -> retrieveFname(uid));
        t.start();
        getTop5(uid);
    }

    // retrieve user first name in a new thread.
    private void retrieveFname(int uid) {
        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(1, "m3.person/" + uid);
        String output = conn.request("get", null);
        Person p = new Gson().fromJson(output, Person.class);
        this.userFname.postValue(p.getFname());
    }

    // process the retrieved memoirs.
    @Override
    public void processFinish(String output) {
        if (output == null) return;
        Gson gson = new Gson();
        Memoir[] memoirs = gson.fromJson(output, Memoir[].class);
        this.memoirs.setValue(Arrays.asList(memoirs));
    }

}