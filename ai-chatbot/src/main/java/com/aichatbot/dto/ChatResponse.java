package com.aichatbot.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ChatResponse {
    private String message;
    private String intent;
    private double confidence;
    private String sessionId;
    private LocalDateTime timestamp;
    private boolean isEscalated;
}
