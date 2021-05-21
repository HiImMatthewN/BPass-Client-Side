package com.spcba.bpass.data.datamodels;

import java.util.Date;

public class Trip {
    private Date tripSchedule;
    private String startDestination;
    private String endDestination;
    private String expectLeaveTime;
    private String expectArriveTime;
    private int fare;

    public Trip() {
    }

    public Trip(Date tripSchedule,String startDestination, String endDestination, String expectLeaveTime, String expectArriveTime, int fare) {
        this.tripSchedule = tripSchedule;
        this.fare =fare;
        this.startDestination = startDestination;
        this.endDestination = endDestination;
        this.expectLeaveTime = expectLeaveTime;
        this.expectArriveTime = expectArriveTime;
    }

    public String getStartDestination() {
        return startDestination;
    }

    public String getEndDestination() {
        return endDestination;
    }

    public String getExpectLeaveTime() {
        return expectLeaveTime;
    }

    public String getExpectArriveTime() {
        return expectArriveTime;
    }

    public int getFare() {
        return fare;
    }

    public Date getTripSchedule() {
        return tripSchedule;
    }
}
