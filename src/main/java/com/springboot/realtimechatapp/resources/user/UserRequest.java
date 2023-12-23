package com.springboot.realtimechatapp.resources.user;

public class UserRequest {
    private String userID;
    private String username;
    private String password;

    public UserRequest(){}

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
