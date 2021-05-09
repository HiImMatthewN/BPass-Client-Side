package com.spcba.bpass.data.datamodels;

import com.google.firebase.firestore.PropertyName;

public class Ticket {
    private String id;
    private Destination destination;
    @PropertyName("used")
    private boolean isUsed;

    public Ticket() {
    }

    public Ticket(String id, Destination destination, boolean isUsed) {
        this.id = id;
        this.destination = destination;
        this.isUsed = isUsed;
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
}
