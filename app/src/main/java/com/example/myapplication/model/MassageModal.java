package com.example.myapplication.model;

public class MassageModal {
    String Uid,massage;
    long timeStamp;

    public MassageModal(String Uid, String massage, long timeStamp) {
        this.Uid = Uid;
        this.massage = massage;
        this.timeStamp = timeStamp;
    }
    public MassageModal(String Uid, String massage) {
        this.Uid = Uid;
        this.massage = massage;
    }
    public MassageModal() {

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
