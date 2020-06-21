package com.fit5046.m3.entity;

// used to store the results of watched movies over months.
public class WatchedInMonth {
    private int count;
    private String monthName;

    public WatchedInMonth(int count, String monthName) {
        this.count = count;
        this.monthName = monthName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }
}
