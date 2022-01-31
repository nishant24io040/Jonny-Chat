package com.example.myapplication.model;

public class UserModal {
    String propic,name,phone,password,lastmasg,email,Uid;
    childModal n;

    public UserModal() {
    }

    public UserModal(String propic, String name, String phone, String Uid, String password, String lastmasg, String email) {
        this.propic = propic;
        this.name = name;
        this.Uid = Uid;
        this.phone = phone;
        this.password = password;
        this.lastmasg = lastmasg;
        this.email = email;

    }


    public UserModal(String name, String phone, String password, String email, String propic) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.propic = propic;
        this.email = email;
    }
    public UserModal(String name,String email, String propic,String Uid) {
        this.name = name;
        this.propic = propic;
        this.email = email;
        this.Uid = Uid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic)
    {
        this.propic = propic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastmasg() {
        return lastmasg;
    }

    public void setLastmasg(String lastmasg) {
        this.lastmasg = lastmasg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
