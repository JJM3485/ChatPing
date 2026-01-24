package com.example.chatping.controller;

import com.example.chatping.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public String chat(@RequestBody Map<String, Object> request, Principal principal) {
        String message = (String) request.get("message");

        Integer characterType = (Integer) request.getOrDefault("characterType", 2);

        String email = principal.getName();

        return chatService.chat(email, message, characterType);
    }
}
