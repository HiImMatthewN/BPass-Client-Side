package com.spcba.bpass.data.datamodels;

import java.util.Date;

public class Destination {
    private String startDestination;
    private String endDestination;
    private Date expectLeaveTime;
    private Date expectArriveTime;
    private int fare;

    public Destination() {
    }

    public Destination( String startDestination, String endDestination, Date expectLeaveTime, Date expectArriveTime, int fare) {
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

    public Date getExpectLeaveTime() {
        return expectLeaveTime;
    }

    public Date getExpectArriveTime() {
        return expectArriveTime;
    }

    public int getFare() {
        return fare;
    }

    public void setExpectLeaveTime(Date expectLeaveTime) {
        this.expectLeaveTime = expectLeaveTime;
    }

    public void setExpectArriveTime(Date expectArriveTime) {
        this.expectArriveTime = expectArriveTime;
    }
}
