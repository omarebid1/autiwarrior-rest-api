package com.autiwarrior.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "chat_message_socket")
public class ChatMessage_Socket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;
    private String sender;
    private Integer userId;
    private Integer groupId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    private String messageType;
}

