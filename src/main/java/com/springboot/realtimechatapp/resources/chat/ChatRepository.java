package com.springboot.realtimechatapp.resources.chat;

import com.springboot.realtimechatapp.resources.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
