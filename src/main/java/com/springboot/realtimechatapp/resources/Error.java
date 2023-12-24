package com.springboot.realtimechatapp.resources;

public enum Error {
    NOT_FOUND("Not Found"),
    CONFLICT("Conflict"),
    ALREADY_EXISTS("Already Exists"),
    ALREADY_JOINED("Already Joined"),
    OK("OK");
    private final String message;

    Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
