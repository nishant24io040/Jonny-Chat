package com.example.myapplication.model;

public class MassageModal {
    String Uid,massage,senderUserid,reciverUid;
    long timeStamp;

//    public MassageModal(String Uid, String massage, long timeStamp) {
//        this.Uid = Uid;
//        this.massage = massage;
//        this.timeStamp = timeStamp;
//    }
    public MassageModal(String Uid, String massage) {
        this.Uid = Uid;
        this.massage = massage;
    }
    public MassageModal(String senderUserid,String reciverUid, String massage) {
        this.reciverUid = reciverUid;
        this.senderUserid = senderUserid;
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

    public String getSenderUserid() {
        return senderUserid;
    }

    public void setSenderUserid(String senderUserid) {
        this.senderUserid = senderUserid;
    }

    public String getReciverUid() {
        return reciverUid;
    }

    public void setReciverUid(String reciverUid) {
        this.reciverUid = reciverUid;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
