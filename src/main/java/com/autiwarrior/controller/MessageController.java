package com.autiwarrior.controller;

import com.autiwarrior.dao.ChatMessageRepository;
import com.autiwarrior.dao.UserRepository;
import com.autiwarrior.dto.ChatMessageDto;
import com.autiwarrior.entities.ChatMessage;
import com.autiwarrior.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final ChatMessageRepository messageRepo;
    private final UserRepository userRepo;

    @PostMapping
    public ResponseEntity<?> send(@RequestBody Map<String, Object> payload) {
        String senderEmail = payload.get("senderEmail").toString();
        String receiverEmail = payload.get("receiverEmail").toString();
        String content = payload.get("content").toString();

        Optional<User> senderOpt = userRepo.findByEmail(senderEmail);
        Optional<User> receiverOpt = userRepo.findByEmail(receiverEmail);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid sender or receiver email");
        }

        ChatMessage message = new ChatMessage();
        message.setSender(senderOpt.get());
        message.setReceiver(receiverOpt.get());
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        ChatMessage savedMessage = messageRepo.save(message);
        ChatMessageDto response = new ChatMessageDto(savedMessage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{senderEmail}/{receiverEmail}")
    public ResponseEntity<?> history(@PathVariable String senderEmail, @PathVariable String receiverEmail) {
        // Find the sender and receiver by email
        Optional<User> senderOpt = userRepo.findByEmail(senderEmail);
        Optional<User> receiverOpt = userRepo.findByEmail(receiverEmail);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid sender or receiver email");
        }

        // Fetch chat history
        List<ChatMessage> history = messageRepo.findChatHistory(senderOpt.get().getUserId(), receiverOpt.get().getUserId());

        // Map to DTOs
        List<ChatMessageDto> response = history.stream()
                .map(ChatMessageDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}