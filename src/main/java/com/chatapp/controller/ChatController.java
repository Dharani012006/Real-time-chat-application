package com.chatapp.controller;

import com.chatapp.model.ChatMessage;
import com.chatapp.model.ChatMessageEntity;
import com.chatapp.repository.ChatMessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ChatController {

    private final ChatMessageRepository repository;

    public ChatController(ChatMessageRepository repository) {
        this.repository = repository;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {

        if ("CHAT".equals(message.getType())) {
            ChatMessageEntity entity = new ChatMessageEntity(
                    message.getSender(),
                    message.getContent(),
                    message.getTime(),
                    message.getType()
            );

            repository.save(entity);
        }

        return message;
    }

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public ChatMessage typing(ChatMessage message) {
        return message;
    }

    @GetMapping("/api/messages")
    @org.springframework.web.bind.annotation.ResponseBody
    public List<ChatMessageEntity> getMessages() {
        return repository.findAllByOrderByIdAsc();
    }
}