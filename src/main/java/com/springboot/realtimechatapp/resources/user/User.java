package com.springboot.realtimechatapp.resources.user;

import com.springboot.realtimechatapp.resources.Error;
import com.springboot.realtimechatapp.resources.chat.Chat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
@Entity
@Table(name="users")
public class User implements Serializable {
    private static final String ID_TEMPLATE = "%s#%d";
    @Id
    private String userID;
    private String username;
    private String hashedPassword;
    @ManyToMany
    @JoinTable(
            name = "chats",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "chatid"))
    private Map<Long, Chat> chats;
    public User() {}

    public User(String userID, String username, String password){
        this.userID = userID;
        this.username = username;
        this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        this.chats = new HashMap<>();
    }
    public String getUsername() {
        return this.username;
    }

    public String getID() {
        return this.userID;
    }

    public Error joinChat(Chat chat){
        if(this.chats.containsKey(chat.getID()))
            return Error.ALREADY_JOINED;
        this.chats.put(chat.getID(), chat);
        return Error.OK;
    }
}
