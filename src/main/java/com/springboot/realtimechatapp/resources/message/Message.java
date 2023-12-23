package com.springboot.realtimechatapp.resources.message;

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
    private User user;

    @ManyToOne
    @JoinColumn(name = "chatid")
    private Chat chat;

    private String content;

    private Date timestamp;

    public Message(){}
    public Message(User sender, Chat chat, String msg){
        this.user = sender;
        this.chat = chat;
        this.content = msg;
        this.timestamp = new Date();
    }
}
