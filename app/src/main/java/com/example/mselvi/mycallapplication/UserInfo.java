package com.example.mselvi.mycallapplication;

public class UserInfo {

    String username;

    public String getUsername() {
        return username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    String phonenumber;
    public UserInfo(String username,String phonenumber){
        this.username = username;
        this.phonenumber = phonenumber;
    }
}
