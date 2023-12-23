package com.springboot.realtimechatapp.resources.chat;

import com.springboot.realtimechatapp.resources.user.User;
import jakarta.persistence.*;

import java.util.Map;


@Entity
@Table(name="chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatID;

    private String name;
    @ManyToMany(mappedBy = "chats")
    private Map<String,User> participants;


    public Long getID(){
        return this.chatID;
    }
}
