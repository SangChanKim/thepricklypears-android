package com.example.sang.waterreportingapp.model;


/**
 * Created by Victor on 10/18/2016.
 *
 * Represents a location on the map
 */
public class Location {

    private double latitude;
    private double longitude;
    private String title;

    /**
     * creates a location
     * @param latitude latitude coordinate
     * @param longitude longitude coordinate
     * @param title name of location
     */
    public Location(double latitude, double longitude, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        return title;
    }

    /**
     * checks if longitude/latitude and location title are the same for two Locations
     * @param o other Location to compare with
     * @return whether they are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Location) {
            Location l2 = (Location) o;
            return (this.getLatitude() == l2.getLatitude()
                    && this.getLongitude() == l2.getLongitude())
                    && (this.getTitle().equals(l2.getTitle()));
        }
        return false;
    }
}
