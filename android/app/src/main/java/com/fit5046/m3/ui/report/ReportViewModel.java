package com.fit5046.m3.ui.report;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fit5046.m3.entity.WatchedInMonth;
import com.fit5046.m3.entity.WathedInArea;
import com.fit5046.m3.lib.NetworkConnection;
import com.fit5046.m3.lib.Utils;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

// Used for report screen.
public class ReportViewModel extends ViewModel {

    private MutableLiveData<List<WathedInArea>> areaCounts;
    private MutableLiveData<List<WatchedInMonth>> monthCounts;
    // the start date, end date, and year user selected.
    private MutableLiveData<String> startDate;
    private MutableLiveData<String> endDate;
    private MutableLiveData<String> year;

    public ReportViewModel() {
        areaCounts = new MutableLiveData<>();
        monthCounts = new MutableLiveData<>();
        startDate = new MutableLiveData<>();
        endDate = new MutableLiveData<>();
        startDate.setValue("2020-01-01");
        endDate.setValue(Utils.getCurrentDate());
        year = new MutableLiveData<>();
        year.setValue("2020");
    }

    public MutableLiveData<List<WathedInArea>> getAreaCounts() {
        return areaCounts;
    }

    public void setAreaCounts(List<WathedInArea> areaCounts) {
        this.areaCounts.setValue(areaCounts);
    }

    public MutableLiveData<List<WatchedInMonth>> getMonthCounts() {
        return monthCounts;
    }

    public void setMonthCounts(List<WatchedInMonth> monthCounts) {
        this.monthCounts.setValue(monthCounts);
    }


    public MutableLiveData<String> getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.setValue(startDate);
    }

    public MutableLiveData<String> getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate.setValue(endDate);
    }

    public MutableLiveData<String> getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year.setValue(year);
    }

    // Prepare to retrieve area counts when user clicks the submit button.
    void clickRetrieveAreaCounts(int uid) {
        String start = startDate.getValue();
        String end = endDate.getValue();

        if (start == null || end == null || start.length() < 8 || end.length() < 8) return;
        if (start.compareTo(end) > 0) return;

        Thread t = new Thread(() -> retrieveAreaCounts(uid, start, end));
        t.start();
    }

    // Prepare to retrieve month counts when user clicks the submit button.
    void clickRetrieveMonthCounts(int uid) {
        String year = this.year.getValue();

        if (year == null || year.length() < 3) return;

        Thread t = new Thread(() -> retrieveMonthCounts(uid, year));
        t.start();
    }

    // retrieve area counts from server.
    private void retrieveAreaCounts(int uid, String start, String end) {
        NetworkConnection conn = new NetworkConnection();
        String methodUrl = "m3.memoir/personAndDate/" + uid + "/" + start + "/" + end;
        conn.buildUrl(1, methodUrl);
        String output = conn.request("get", null);
        WathedInArea[] nList = new Gson().fromJson(output, WathedInArea[].class);
        this.areaCounts.postValue(Arrays.asList(nList));
    }

    // retrieve area counts from server.
    private void retrieveMonthCounts(int uid, String year) {
        String methodUrl = "m3.memoir/personAndYear/" + uid + "/" + year;
        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(1, methodUrl);
        String output = conn.request("get", null);
        WatchedInMonth[] nList = new Gson().fromJson(output, WatchedInMonth[].class);
        this.monthCounts.postValue(Arrays.asList(nList));
    }

}
