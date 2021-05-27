package com.spcba.bpass.data.datamodels;

import java.util.Date;

public class Trip {
    private int slotAvailable;
    private Date schedule;
    private Destination destination;
    private String busNumber;

    public Trip() {
    }

    public Trip(int slotAvailable, Date schedule, Destination destination, String busNumber) {
        this.slotAvailable = slotAvailable;
        this.schedule = schedule;
        this.destination = destination;
        this.busNumber = busNumber;
    }

    public void setSlotAvailable(int slotAvailable) {
        this.slotAvailable = slotAvailable;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public int getSlotAvailable() {
        return slotAvailable;
    }

    public Date getSchedule() {
        return schedule;
    }

    public Destination getDestination() {
        return destination;
    }

    public String getBusNumber() {
        return busNumber;
    }


}
