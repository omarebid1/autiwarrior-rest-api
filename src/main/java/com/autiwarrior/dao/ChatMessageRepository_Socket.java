package com.autiwarrior.dao;
import com.autiwarrior.entities.ChatMessage;
import com.autiwarrior.entities.ChatMessage_Socket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository_Socket extends JpaRepository<ChatMessage_Socket, Integer> {


}