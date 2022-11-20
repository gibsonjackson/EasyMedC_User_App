package com.anvaishy.easymedc_user_app.model;

import com.google.firebase.Timestamp;

public class MedicalPassRequestUser {
    private String description;
    private int status;
    private Timestamp arrival;
    private Timestamp depart;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setArrival(Timestamp arrival) {
        this.arrival = arrival;
    }

    public void setDeparture(Timestamp departure) {
        this.depart = departure;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getArrival() {
        return arrival;
    }

    public Timestamp getDepart() {
        return depart;
    }
}
