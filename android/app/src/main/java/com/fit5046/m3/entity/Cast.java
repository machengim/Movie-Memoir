package com.fit5046.m3.entity;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

// Used to parse cast from the response of TMDB query.
public class Cast {

    private int id;
    // cast list contains the actors, crew list contains other staff.
    private List<Employee> cast;
    private List<Employee> crew;

    public Cast(int id, List<Employee> cast, List<Employee> crew) {
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Employee> getCast() {
        return cast;
    }

    public void setCast(List<Employee> cast) {
        this.cast = cast;
    }

    public List<Employee> getCrew() {
        return crew;
    }

    public void setCrew(List<Employee> crew) {
        this.crew = crew;
    }

    @NotNull
    @Override
    public String toString() {
        String s =  "id:" + id + '\n' + "size:" + cast.size() + '\n';
        s += cast.stream().map(Employee::toString).collect(Collectors.joining());
        return s;
    }
}
