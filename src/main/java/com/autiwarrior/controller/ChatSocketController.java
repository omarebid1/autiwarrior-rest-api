package com.autiwarrior.controller;

import com.autiwarrior.dao.ChatMessageRepository;
import com.autiwarrior.entities.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ChatMessageRepository repo;

    @MessageMapping("/chat")
    public void receive(@Payload ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        repo.save(message);
        template.convertAndSend("/topic/messages/" + message.getReceiverId(), message);
    }
}