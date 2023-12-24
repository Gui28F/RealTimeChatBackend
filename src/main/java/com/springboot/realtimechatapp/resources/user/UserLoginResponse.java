package com.springboot.realtimechatapp.resources.user;

public class UserLoginResponse {
    private String userID;
    private String token;

    public UserLoginResponse(){}

    public UserLoginResponse(String userID, String token){
        this.userID = userID;
        this.token = token;
    }

    public String getUser() {
        return userID;
    }

    public String getToken() {
        return token;
    }
}
