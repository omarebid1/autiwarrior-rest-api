package com.autiwarrior.dto;

import lombok.Data;

@Data
public class ChatMessageSocketPayload {
    private String senderEmail;
    private String receiverEmail;
    private String content;

}
