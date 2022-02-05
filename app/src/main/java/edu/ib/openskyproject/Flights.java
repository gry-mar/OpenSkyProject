package edu.ib.openskyproject;

import java.util.List;

public class Flights {
//    private int time;
//    private String icao24;
//    private String callsign;
//    /protected String originCountry;
    //protected int timePosition;
    //protected int lastContact;
    protected double longitude;
    protected double latitude;
    // protected boolean onGround;
    // protected double velocity;
    // protected double trueTrack;
    // protected double verticalRate;
    //protected int[] sensors;
    //protected double geoAltitude;
    // protected String squawk;
    // protected boolean spi;
    //protected int positionSource;

    public Flights(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;

    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    @Override
    public String toString() {
        return "states{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
