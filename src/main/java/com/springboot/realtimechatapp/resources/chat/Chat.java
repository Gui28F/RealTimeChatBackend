package com.springboot.realtimechatapp.resources.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.realtimechatapp.Result;
import com.springboot.realtimechatapp.resources.message.Message;
import com.springboot.realtimechatapp.resources.user.User;
import jakarta.persistence.*;

import java.util.*;


@Entity
@Table(name="chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatID;
    @JsonProperty("name")
    private String name;
    @ManyToMany
    @JoinTable(
            name = "chat_participants",
            joinColumns = @JoinColumn(name = "chatid"),
            inverseJoinColumns = @JoinColumn(name = "userid")
    )
    @MapKeyJoinColumn(name = "userid")
    @JsonProperty("participants")
    private Set<User> participants;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @JsonProperty("messages")
    private List<Message> messages;
    public Chat(){}
    public Chat(String chatName){
        this.name = chatName;
        this.participants = new HashSet<>();
        this.messages = new LinkedList<>();
    }

    public Long getID(){
        return this.chatID;
    }

    public void addMsg(Message msg){
        this.messages.add(msg);
    }

    public Result<Void> addParticipant(User user){
        if(this.participants.contains(user))
            return Result.error(Result.ErrorCode.CONFLICT);
        this.participants.add(user);
        return Result.ok();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(chatID, chat.chatID);
    }
}
