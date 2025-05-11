package com.autiwarrior.dao;

import com.autiwarrior.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender.id = :user1 AND m.receiver.id = :user2) OR " +
            "(m.sender.id = :user2 AND m.receiver.id = :user1) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatHistory(@Param("user1") Integer user1, @Param("user2") Integer user2);

    @Query("SELECT m FROM ChatMessage m WHERE m.sender.userId = :userId OR m.receiver.userId = :userId ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatMessagesInvolvingUser(@Param("userId") Long userId);


}
