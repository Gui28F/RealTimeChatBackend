package com.springboot.realtimechatapp.resources.chat;

import com.springboot.realtimechatapp.resources.user.User;
import jakarta.persistence.*;

import java.util.Map;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name="chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatID;

    private String name;
    @ManyToMany
    @JoinTable(
            name = "chat_participants",
            joinColumns = @JoinColumn(name = "chatid"),
            inverseJoinColumns = @JoinColumn(name = "userid")
    )
    @MapKeyJoinColumn(name = "userid")
    private Set<User> participants;


    public Long getID(){
        return this.chatID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(chatID, chat.chatID);
    }
}
