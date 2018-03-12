package com.company;

public class Pool implements Comparable<Pool> {

    private final String name;
    private final double longitude;
    private final double latitude;
    private double distanceToParent;
    private double distanceToRoot;

    public Pool(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        distanceToParent = 0;
        distanceToRoot = 0;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getDistanceToParent() { return distanceToParent; }

    public double getDistanceToRoot() { return distanceToRoot; }

    public void setDistanceToParent(double distanceToParent) { this.distanceToParent = distanceToParent; }

    public void setDistanceToRoot(double distanceToRoot) { this.distanceToRoot = distanceToRoot; }

    public double distanceFrom(Pool other) {
        double X = Math.pow(Math.sin((this.latitude - other.getLatitude())/2), 2);
        double Y = (Math.cos(this.latitude) * Math.cos(other.getLatitude()));
        double Z = Math.pow(Math.sin((this.longitude - other.getLongitude())/2), 2);
        double dRadians = (2 * Math.asin(Math.sqrt(X + Y * Z)));
        return (6371.0 * dRadians);
    }

    private double radiansAngle(double angleDegrees) { return 180.0 * (angleDegrees/Math.PI); }

    @Override
    public int compareTo(Pool o) {

        if (this == o) return 0;
        if(this.longitude == o.getLongitude())
            return 0;
        else
            return this.longitude > o.getLongitude() ? 1 : -1;

    }
}