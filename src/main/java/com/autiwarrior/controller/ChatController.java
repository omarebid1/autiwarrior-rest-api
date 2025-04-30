package com.autiwarrior.controller;
import com.autiwarrior.dao.ChatMessageRepository_Socket;
import com.autiwarrior.dto.ChatMessageModel;
import com.autiwarrior.entities.ChatMessage;
import com.autiwarrior.entities.ChatMessage_Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
@CrossOrigin(origins = "*")  // يسمح بكل origins

@Controller
@Slf4j
public class ChatController {

    @Autowired
    ChatMessageRepository_Socket chatMessageRepository;
    @Autowired
    private ChatMessageRepository_Socket chatMessageRepository_Socket;

    @MessageMapping("/gifts")
    @SendTo("/topic/messages")
    public ChatMessageModel send(@Payload ChatMessageModel messageModel) {
        log.info("received chat message");

        ChatMessage_Socket message = new ChatMessage_Socket();
        Date messageTimeStamp = new Date();

        message.setContent(messageModel.getContent());
        message.setUserId(messageModel.getUserId());
        message.setSender(messageModel.getSender());
        message.setGroupId(messageModel.getGroupId());
        message.setCreationDate(messageTimeStamp);
        message.setMessageType(messageModel.getMessageType());

        ChatMessage_Socket savedImage = chatMessageRepository_Socket.save(message);

        messageModel.setCreationDate(messageTimeStamp);
        messageModel.setMessageId(savedImage.getId());

        return messageModel;
    }
}

