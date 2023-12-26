package com.springboot.realtimechatapp.resources.user;

import com.springboot.realtimechatapp.Result;
import com.springboot.realtimechatapp.resources.chat.Chat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

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
            name = "user_chats",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "chatid")
    )

    private Set<Chat> chats;
    public User() {}

    public User(String userID, String username, String password){
        this.userID = userID;
        this.username = username;
        this.hashedPassword = password;
        this.chats = new HashSet<>();
    }
    public String getUsername() {
        return this.username;
    }

    public String getID() {
        return this.userID;
    }

    public Result<Void> joinChat(Chat chat){
        if(this.chats.contains(chat))
            return Result.error(Result.ErrorCode.CONFLICT);
        this.chats.add(chat);
        return Result.error(Result.ErrorCode.CONFLICT);
    }

    public String getHashedPassword(){
        return this.hashedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID);
    }
}
