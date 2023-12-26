package com.springboot.realtimechatapp.resources.user;

import com.springboot.realtimechatapp.resources.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u.chats FROM User u WHERE u.userID = :userId")
    List<Chat> findAllChatsByUserId(@Param("userId") String userId);
}
