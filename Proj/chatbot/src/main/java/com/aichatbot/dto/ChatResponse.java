package com.aichatbot.dto;

import java.time.LocalDateTime;

public class ChatResponse {
    private String message;
    private String intent;
    private double confidence;
    private String sessionId;
    private LocalDateTime timestamp;
    private boolean isEscalated;

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final ChatResponse r = new ChatResponse();
        public Builder message(String v) { r.message = v; return this; }
        public Builder intent(String v) { r.intent = v; return this; }
        public Builder confidence(double v) { r.confidence = v; return this; }
        public Builder sessionId(String v) { r.sessionId = v; return this; }
        public Builder timestamp(LocalDateTime v) { r.timestamp = v; return this; }
        public Builder isEscalated(boolean v) { r.isEscalated = v; return this; }
        public ChatResponse build() { return r; }
    }

    public String getMessage() { return message; }
    public String getIntent() { return intent; }
    public double getConfidence() { return confidence; }
    public String getSessionId() { return sessionId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isEscalated() { return isEscalated; }
}
