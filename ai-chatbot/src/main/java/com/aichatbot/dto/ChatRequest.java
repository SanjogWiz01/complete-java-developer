package com.aichatbot.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ChatRequest {

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 500, message = "Message too long")
    private String message;

    private String sessionId;
}
