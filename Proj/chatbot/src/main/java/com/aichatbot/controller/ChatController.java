package com.aichatbot.controller;

import com.aichatbot.dto.ChatRequest;
import com.aichatbot.dto.ChatResponse;
import com.aichatbot.service.ChatbotService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final ChatbotService chatbotService;

    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@Valid @RequestBody ChatRequest request) {
        log.info("Received chat request: {}", request.getMessage());
        ChatResponse response = chatbotService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "AI Customer Support Chatbot",
            "version", "1.0.0"
        ));
    }

    @GetMapping("/intents")
    public ResponseEntity<Map<String, Object>> getSupportedIntents() {
        return ResponseEntity.ok(Map.of(
            "intents", new String[]{
                "GREETING", "ORDER_STATUS", "REFUND_REQUEST", "PRODUCT_INFO",
                "TECHNICAL_SUPPORT", "ACCOUNT_HELP", "SHIPPING_INFO",
                "COMPLAINT", "FAREWELL", "GENERAL_INQUIRY"
            }
        ));
    }
}
