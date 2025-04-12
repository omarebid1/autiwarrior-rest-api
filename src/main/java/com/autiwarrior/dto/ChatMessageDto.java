package com.autiwarrior.dto;

import com.autiwarrior.entities.ChatMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {

    private String senderName;
    private String senderRole;

    private String receiverName;
    private String receiverRole;

    private String content;
    private LocalDateTime timestamp;

    public ChatMessageDto(ChatMessage message) {
        this.senderName = message.getSender().getFirstName() + " " + message.getSender().getLastName();
        this.senderRole = message.getSender().getRole().name();

        this.receiverName = message.getReceiver().getFirstName() + " " + message.getReceiver().getLastName();
        this.receiverRole = message.getReceiver().getRole().name();

        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
    }

    // Getters/setters if needed (or use Lombok)
}
