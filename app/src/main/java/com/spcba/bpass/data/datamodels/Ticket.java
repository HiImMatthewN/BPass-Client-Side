package com.spcba.bpass.data.datamodels;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Ticket {
    private String id;
    private Destination destination;
    @PropertyName("used")
    private boolean isUsed;
    private Date schedule;
    private String busNumber;
    public Ticket() {
    }

    public Ticket(String id, Destination destination, boolean isUsed,Date schedule,String busNumber) {
        this.id = id;
        this.destination = destination;
        this.isUsed = isUsed;
        this.schedule = schedule;
        this.busNumber = busNumber;
    }

    public String getId() {
        return id;
    }

    public Destination getDestination() {
        return destination;
    }
    @PropertyName("used")
    public boolean isUsed() {
        return isUsed;
    }

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }

    public String getBusNumber() {
        return busNumber;
    }
}
