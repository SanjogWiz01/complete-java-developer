package com.aichatbot.service;

import com.aichatbot.dto.ChatRequest;
import com.aichatbot.dto.ChatResponse;
import com.aichatbot.model.ChatMessage;
import com.aichatbot.repository.ChatMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatbotService {

    private static final Logger log = LoggerFactory.getLogger(ChatbotService.class);

    private final NlpService nlpService;
    private final ResponseGeneratorService responseGeneratorService;
    private final ChatMessageRepository chatMessageRepository;
    private final ConcurrentHashMap<String, Integer> sessionMessageCount = new ConcurrentHashMap<>();

    public ChatbotService(NlpService nlpService,
                          ResponseGeneratorService responseGeneratorService,
                          ChatMessageRepository chatMessageRepository) {
        this.nlpService = nlpService;
        this.responseGeneratorService = responseGeneratorService;
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatResponse processMessage(ChatRequest request) {
        String sessionId = resolveSessionId(request.getSessionId());
        String userMessage = request.getMessage().trim();

        log.info("Processing message for session {}: {}", sessionId, userMessage);

        String intent = nlpService.detectIntent(userMessage);
        double confidence = nlpService.calculateConfidence(userMessage, intent);
        Map<String, String> entities = nlpService.extractEntities(userMessage);
        String sentiment = nlpService.analyzeSentiment(userMessage);

        log.debug("Intent: {}, Confidence: {}, Sentiment: {}", intent, confidence, sentiment);

        int messageCount = sessionMessageCount.merge(sessionId, 1, Integer::sum);
        boolean shouldEscalate = responseGeneratorService.shouldEscalate(intent, sentiment, messageCount);

        String botResponse = shouldEscalate
            ? responseGeneratorService.getEscalationMessage()
            : responseGeneratorService.generateResponse(intent, entities, sentiment);

        persistMessage(userMessage, botResponse, sessionId, intent, confidence);

        return ChatResponse.builder()
            .message(botResponse)
            .intent(intent)
            .confidence(confidence)
            .sessionId(sessionId)
            .timestamp(LocalDateTime.now())
            .isEscalated(shouldEscalate)
            .build();
    }

    private String resolveSessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) return UUID.randomUUID().toString();
        return sessionId;
    }

    private void persistMessage(String userMessage, String botResponse,
                                String sessionId, String intent, double confidence) {
        try {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUserMessage(userMessage);
            chatMessage.setBotResponse(botResponse);
            chatMessage.setSessionId(sessionId);
            chatMessage.setIntent(intent);
            chatMessage.setConfidenceScore(confidence);
            chatMessageRepository.save(chatMessage);
        } catch (Exception e) {
            log.error("Failed to persist chat message: {}", e.getMessage());
        }
    }
}
