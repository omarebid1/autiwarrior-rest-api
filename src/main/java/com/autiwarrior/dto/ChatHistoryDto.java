package com.autiwarrior.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatHistoryDto {
    private Integer userId;
    private String fullName;
    private String email;
    private String profilePictureUrl;
    private String lastMessage;
    private LocalDateTime timestamp;
    private long unreadCount;
}
