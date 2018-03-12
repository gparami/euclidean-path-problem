package com.company;

public class Pool {

    private final String name;
    private final double lon;
    private final double lat;

    public Pool(String name, double lon, double lat) {
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

}
