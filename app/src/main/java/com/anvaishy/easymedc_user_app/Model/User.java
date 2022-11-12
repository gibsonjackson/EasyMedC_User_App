package com.anvaishy.easymedc_user_app.Model;

public class User {
    private String name;
    private String studentID;
    private String hostel;
    private String roomNo;
    private String studentPhoneNo;
    private String guardianPhoneNo;
    private String email;

    public String getName() {
        return name;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getHostel() {
        return hostel;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getStudentPhoneNo() {
        return studentPhoneNo;
    }

    public String getGuardianPhoneNo() {
        return guardianPhoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public void setStudentPhoneNo(String studentPhoneNo) {
        this.studentPhoneNo = studentPhoneNo;
    }

    public void setGuardianPhoneNo(String guardianPhoneNo) {
        this.guardianPhoneNo = guardianPhoneNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
