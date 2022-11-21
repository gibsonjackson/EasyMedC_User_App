package com.anvaishy.easymedc_user_app.model;

import com.google.firebase.Timestamp;

public class MedicalPassRequestGlobal {
    private String name;
    private String phoneNo;
    private String uid;
    private String description;
    private int status;
    private Timestamp arrival;
    private Timestamp departure;

    public Timestamp getDeparture() {
        return departure;
    }

    public Timestamp getArrival() {
        return arrival;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public void setDeparture(Timestamp departure) {
        this.departure = departure;
    }

    public void setArrival(Timestamp arrival) {
        this.arrival = arrival;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
