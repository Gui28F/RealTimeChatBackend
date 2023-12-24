package com.springboot.realtimechatapp.resources.user;

public class UserRequest {
    private String userID;
    private String username;
    private String password;

    public UserRequest(){}
    public UserRequest(String userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }
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
