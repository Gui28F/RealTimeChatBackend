package com.springboot.realtimechatapp.resources.message;

import com.springboot.realtimechatapp.resources.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository  extends JpaRepository<Message, Long> {
}
