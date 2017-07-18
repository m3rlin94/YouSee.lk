package com.uclk.shani.youseelk.objects;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Location {

    public String locationName;
    public String streetNumber;
    public String streetName;
    public Double locationLongitude;
    public Double locationLatitude;
    public String locationCol;
    public String nearestCity;
    public String otherName;

    public Location() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Location(String locationName, String streetNumber,
                    String streetName, Double locationLongitude,
                    Double locationLatitude, String locationCol,
                    String nearestCity, String otherName)
    {
        this.locationName = locationName;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.locationCol = locationCol;
        this.nearestCity = nearestCity;
        this.otherName = otherName;
    }








}
