package com.anvaishy.easymedc_user_app.model;


public class Doctor {
    private final String name;
    private final String specialisation;
    private String startTime;
    private String endTime;

    public Doctor(String n, String sp, String s, String e) {
        name = n;
        specialisation = sp;
        startTime = s;
        endTime = e;
    }


    public String getName() {
        return name;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
