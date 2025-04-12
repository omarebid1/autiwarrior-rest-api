package com.autiwarrior.controller;

import com.autiwarrior.dao.ChatMessageRepository;
import com.autiwarrior.entities.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private ChatMessageRepository repo;

    @PostMapping
    public ChatMessage send(@RequestBody ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return repo.save(message);
    }

    @GetMapping("/{user1}/{user2}")
    public List<ChatMessage> history(@PathVariable Long user1, @PathVariable Long user2) {
        return repo.findChatHistory(user1, user2);
    }
}