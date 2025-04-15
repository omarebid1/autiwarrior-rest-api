package com.autiwarrior.controller;

import com.autiwarrior.dao.ChatMessageRepository;
import com.autiwarrior.dao.UserRepository;
import com.autiwarrior.dto.ChatMessageDto;
import com.autiwarrior.dto.ChatMessageSocketPayload;
import com.autiwarrior.entities.ChatMessage;
import com.autiwarrior.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SimpMessagingTemplate template;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @MessageMapping("/chat")
    public void receive(@Payload ChatMessageSocketPayload payload) {
        Optional<User> senderOpt = userRepository.findByEmail(payload.getSenderEmail());
        Optional<User> receiverOpt = userRepository.findByEmail(payload.getReceiverEmail());

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            System.out.println("Sender or Receiver email not found!");
            return;
        }

        ChatMessage message = new ChatMessage();
        message.setSender(senderOpt.get());
        message.setReceiver(receiverOpt.get());
        message.setContent(payload.getContent());
        message.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(message);

        ChatMessageDto response = new ChatMessageDto(message);

        // Send the message to the receiver’s topic
        template.convertAndSend("/topic/messages/" + receiverOpt.get().getUserId(), response);
    }
}
