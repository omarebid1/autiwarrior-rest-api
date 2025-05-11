package com.autiwarrior.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatHistoryDto {

    private String sender;
    private String message;
    private LocalDateTime timestamp;

}
