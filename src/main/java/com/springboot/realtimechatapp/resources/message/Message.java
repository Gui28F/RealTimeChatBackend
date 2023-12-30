package com.springboot.realtimechatapp.resources.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.realtimechatapp.resources.chat.Chat;
import com.springboot.realtimechatapp.resources.user.User;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonProperty("user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chatid")
    private Chat chat;

    @JsonProperty("content")
    private String content;

    @JsonProperty("timestamp")
    private Date timestamp;

    public Message(){}
    public Message(User sender, Chat chat, String msg){
        this.user = sender;
        this.chat = chat;
        this.content = msg;
        this.timestamp = new Date();
    }

    @JsonProperty("chat")
    public Long chatId(){
        return this.chat.getID();
    }
}
