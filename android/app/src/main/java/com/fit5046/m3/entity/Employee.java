package com.fit5046.m3.entity;

import org.jetbrains.annotations.NotNull;

// Used to parse json response from TMDB.
// cast and crew both belong to it.
public class Employee {

    private int id;
    private String job;
    private String name;

    public Employee(int id, String job, String name) {
        this.id = id;
        this.job = job;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String toString() {
        String job2 = (job == null)? "Unset": job;
        return "Employee{" +
                "id=" + id +
                ", job='" + job2 + '\'' +
                ", name='" + name + '\'' +
                '}' + '\n';
    }
}
