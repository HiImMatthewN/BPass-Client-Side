package com.spcba.bpass.data.datamodels;

import com.google.firebase.firestore.PropertyName;

public class Ticket {
    private String id;
    private Trip trip;
    @PropertyName("used")
    private boolean isUsed;

    public Ticket() {
    }

    public Ticket(String id, Trip trip, boolean isUsed) {
        this.id = id;
        this.trip = trip;
        this.isUsed = isUsed;
    }

    public String getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }
    @PropertyName("used")
    public boolean isUsed() {
        return isUsed;
    }
}
