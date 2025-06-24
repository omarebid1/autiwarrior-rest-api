package com.autiwarrior.controller;

import com.autiwarrior.dao.ChatMessageRepository;
import com.autiwarrior.dao.UserRepository;
import com.autiwarrior.dto.ChatHistoryDto;
import com.autiwarrior.dto.ChatMessageDto;
import com.autiwarrior.entities.ChatMessage;
import com.autiwarrior.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentChats(@RequestParam String email) {
        Optional<User> currentUserOpt = userRepo.findByEmail(email);
        if (currentUserOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid email");
        }

        List<ChatMessage> recentMessages = messageRepo.findRecentChatsByEmail(email);

        List<ChatHistoryDto> response = recentMessages.stream()
                .map(msg -> {
                    User partner = msg.getSender().getEmail().equals(email)
                            ? msg.getReceiver()
                            : msg.getSender();

                    String fullName = partner.getFirstName() + " " + partner.getLastName();
                    String pictureUrl = partner.getProfilePictureUrl();

                    // Fetch unread message count from partner to current user
                    long unreadCount = messageRepo.countUnreadMessagesByEmail(partner.getEmail(), email);

                    return new ChatHistoryDto(
                            partner.getUserId(),
                            fullName,
                            partner.getEmail(),
                            pictureUrl,
                            msg.getContent(),
                            msg.getTimestamp(),
                            unreadCount
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/history")
    @Transactional
    public ResponseEntity<?> getAllMessagesBetweenUsers(
            @RequestParam String receiverEmail,
            @RequestParam String partnerEmail) {

        Optional<User> receiverOpt = userRepo.findByEmail(receiverEmail);
        Optional<User> partnerOpt = userRepo.findByEmail(partnerEmail);

        if (receiverOpt.isEmpty() || partnerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid email(s)");
        }

        User receiver = receiverOpt.get();
        User partner = partnerOpt.get();

        Long receiverId = Long.valueOf(receiver.getUserId());
        Long partnerId = Long.valueOf(partner.getUserId());

        // ✅ Mark unread messages from partner → receiver as read
        messageRepo.markMessagesAsRead(partnerId, receiverId);

        // ✅ Now fetch full conversation
        List<ChatMessage> messages = messageRepo.findMessagesBetweenUsers(receiverId, partnerId);

        List<ChatHistoryDto> response = messages.stream()
                .map(msg -> {
                    User sender = msg.getSender();
                    return new ChatHistoryDto(
                            sender.getUserId(),
                            sender.getFirstName() + " " + sender.getLastName(),
                            sender.getEmail(),
                            sender.getProfilePictureUrl(),
                            msg.getContent(),
                            msg.getTimestamp(),
                            0 // optional: unread count not needed here
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}