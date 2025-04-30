package com.autiwarrior.dto;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class ChatMessageModel implements Serializable {
    private String content;
    private String sender;
    private Integer userId;
    private Integer groupId;
    private Date creationDate;
    private String messageType;
    private Integer messageId;
}