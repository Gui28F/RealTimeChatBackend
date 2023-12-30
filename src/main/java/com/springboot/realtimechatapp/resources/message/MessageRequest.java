package com.springboot.realtimechatapp.resources.message;

public class MessageRequest {

    private String senderId;
    private String msg;

    public MessageRequest(){}

    public MessageRequest(String senderId, String msg){
        this.senderId =senderId;
        this.msg = msg;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMsg() {
        return msg;
    }
}
