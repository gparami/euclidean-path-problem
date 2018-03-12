package com.company;

public class Pool {

    private final String name;
    private final double longitude;
    private final double latitude;

    public Pool(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public double getLon() {
        return longitude;
    }

    public double getLat() {
        return latitude;
    }

}