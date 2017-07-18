package com.uclk.shani.youseelk.objects;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Tour {

    public String startingDate;
    public String endingDate;
    public int duration;
    public Double totalDistance;
    public Double totalTime;
    public String userId;
    public String tourPlacesId;

    public Tour() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Tour(String startingDate, String endingDate,
                int duration, Double totalDistance,
                Double totalTime, String userId,
                String tourPlacesId)
    {
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.duration = duration;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.userId = userId;
        this.tourPlacesId = tourPlacesId;
    }
}
