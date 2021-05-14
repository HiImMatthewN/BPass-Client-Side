package com.spcba.bpass.data.datamodels;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class TopUp {

   private int amount;
    private String paymentMethod;
    private String refNumber;
    @PropertyName("paid")
    private boolean isPaid;
    private Date dateCreated;
    public TopUp(){



    }
    public TopUp( int amount,String paymentMethod,String refNumber, boolean isPaid, Date dateCreated) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.refNumber = refNumber;
        this.isPaid = isPaid;
        this.dateCreated = dateCreated;
    }
    public int getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getRefNumber() {
        return refNumber;
    }
    @PropertyName("paid")
    public boolean isPaid() {
        return isPaid;
    }

    public Date getDateCreated() {
        return dateCreated;
    }
}
