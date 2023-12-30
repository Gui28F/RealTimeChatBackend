package com.springboot.realtimechatapp.resources.chat;

import com.springboot.realtimechatapp.resources.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    @Autowired
    public ChatService(ChatRepository chatRepository){
        this.chatRepository = chatRepository;
    }

    public void addMessage(Long chatId, Message msg){
        Optional<Chat> chatOp = chatRepository.findById(chatId);
        Chat chat = chatOp.get();
        chat.addMsg(msg);
        chatRepository.save(chat);
    }
    public void addChat(Chat chat){
        chatRepository.save(chat);
    }

    public Optional<Chat> getChat(Long chatId){
        return chatRepository.findById(chatId);
    }
}
