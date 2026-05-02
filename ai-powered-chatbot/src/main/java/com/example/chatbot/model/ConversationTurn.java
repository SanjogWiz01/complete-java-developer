package com.example.chatbot.model;

import java.time.Instant;

public record ConversationTurn(String sessionId,
                               String userMessage,
                               String botReply,
                               String intent,
                               Instant timestamp) {
}
