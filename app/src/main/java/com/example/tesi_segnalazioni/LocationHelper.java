package com.example.tesi_segnalazioni;

public class LocationHelper {
    private int id;
    private float Longitude;
    private float Latitude;
    private String gravita;

    public LocationHelper(int id, float latitude, float longitude, String gravita) {
        this.id = id;
        Longitude = longitude;
        Latitude = latitude;
        this.gravita = gravita;
    }

    public LocationHelper(float latitude, float longitude) {
        Longitude = longitude;
        Latitude = latitude;
    }

    public LocationHelper() {
    }

    public LocationHelper(int id, float latitude, float longitude) {
        this.id = id;
        Longitude = longitude;
        Latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public String getGravita() {
        return gravita;
    }

    public void setGravita(String gravita) {
        this.gravita = gravita;
    }
}
