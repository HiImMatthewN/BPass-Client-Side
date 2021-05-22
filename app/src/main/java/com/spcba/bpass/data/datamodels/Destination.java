package com.spcba.bpass.data.datamodels;

public class Destination {
    private String startDestination;
    private String endDestination;
    private String expectLeaveTime;
    private String expectArriveTime;
    private int fare;

    public Destination() {
    }

    public Destination( String startDestination, String endDestination, String expectLeaveTime, String expectArriveTime, int fare) {
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


}
