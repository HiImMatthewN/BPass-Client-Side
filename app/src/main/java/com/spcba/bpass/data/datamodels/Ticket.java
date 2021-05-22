package com.spcba.bpass.data.datamodels;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Ticket {
    private String id;
    private Destination destination;
    @PropertyName("used")
    private boolean isUsed;
    private Date schedule;
    public Ticket() {
    }

    public Ticket(String id, Destination destination, boolean isUsed,Date schedule) {
        this.id = id;
        this.destination = destination;
        this.isUsed = isUsed;
        this.schedule = schedule;
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
}
