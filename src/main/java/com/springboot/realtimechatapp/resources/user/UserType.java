package com.springboot.realtimechatapp.resources.user;

public enum UserType {
    USER("USER");
    private final String type;

    UserType(String string) {
        type = string;
    }

    @Override
    public String toString() {
        return type;
    }
}
