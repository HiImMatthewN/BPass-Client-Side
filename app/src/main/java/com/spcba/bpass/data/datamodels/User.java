package com.spcba.bpass.data.datamodels;

public class User {
    private String name;
    private String mobileNumber;
    private String email;
    private String profilePicUrl;
    private String uid;
    private double balance;

    public User() {
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public User(String name, String mobileNumber, String email, String profilePicUrl, String uid,double balance) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
        this.uid = uid;
        this.balance = balance;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }
}
