package com.example.chatbot.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(@NotBlank(message = "message is required") String message,
                          String sessionId) {
}
