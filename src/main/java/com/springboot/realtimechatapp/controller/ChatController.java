package com.springboot.realtimechatapp.controller;

import com.springboot.realtimechatapp.model.ChatMessage;
import com.springboot.realtimechatapp.resources.user.User;
import com.springboot.realtimechatapp.resources.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;


public class ChatController {
    /*private final UserRepository users;

    @Autowired
    public ChatController(UserRepository users){
        this.users = users;
    }
    @MessageMapping("/chat.register")
    @SendTo("/chat/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        User a = new User("id", "teste", "password");
        users.save(a);
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat.send")
    @SendTo("/chat/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }*/
}