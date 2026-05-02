package com.aichatbot.controller;

import com.aichatbot.dto.ChatRequest;
import com.aichatbot.dto.ChatResponse;
import com.aichatbot.service.ChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatbotService chatbotService;

    /**
     * POST /api/chat/message
     * Send a message to the chatbot and get a response.
     */
    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@Valid @RequestBody ChatRequest request) {
        log.info("Received chat request: {}", request.getMessage());
        ChatResponse response = chatbotService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/chat/health
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "AI Customer Support Chatbot",
            "version", "1.0.0"
        ));
    }

    /**
     * GET /api/chat/intents
     * List all supported intents.
     */
    @GetMapping("/intents")
    public ResponseEntity<Map<String, Object>> getSupportedIntents() {
        return ResponseEntity.ok(Map.of(
            "intents", new String[]{
                "GREETING", "ORDER_STATUS", "REFUND_REQUEST", "PRODUCT_INFO",
                "TECHNICAL_SUPPORT", "ACCOUNT_HELP", "SHIPPING_INFO",
                "COMPLAINT", "FAREWELL", "GENERAL_INQUIRY"
            },
            "description", "Supported customer support intent categories"
        ));
    }
}
