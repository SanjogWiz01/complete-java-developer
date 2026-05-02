package com.example.chatbot.dto;

public record ChatResponse(String sessionId,
                           String userMessage,
                           String botReply,
                           String detectedIntent,
                           double confidence,
                           String sentiment) {
}
