package com.aichatbot.service;

import com.aichatbot.dto.ChatRequest;
import com.aichatbot.dto.ChatResponse;
import com.aichatbot.model.ChatMessage;
import com.aichatbot.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final NlpService nlpService;
    private final ResponseGeneratorService responseGeneratorService;
    private final ChatMessageRepository chatMessageRepository;

    // Track conversation message counts per session
    private final ConcurrentHashMap<String, Integer> sessionMessageCount = new ConcurrentHashMap<>();

    /**
     * Process incoming chat message and generate AI response.
     */
    public ChatResponse processMessage(ChatRequest request) {
        String sessionId = resolveSessionId(request.getSessionId());
        String userMessage = request.getMessage().trim();

        log.info("Processing message for session {}: {}", sessionId, userMessage);

        // NLP Processing Pipeline
        String intent = nlpService.detectIntent(userMessage);
        double confidence = nlpService.calculateConfidence(userMessage, intent);
        Map<String, String> entities = nlpService.extractEntities(userMessage);
        String sentiment = nlpService.analyzeSentiment(userMessage);

        log.debug("Intent: {}, Confidence: {}, Sentiment: {}, Entities: {}",
            intent, confidence, sentiment, entities);

        // Increment session message count
        int messageCount = sessionMessageCount.merge(sessionId, 1, Integer::sum);

        // Determine if escalation is needed
        boolean shouldEscalate = responseGeneratorService.shouldEscalate(intent, sentiment, messageCount);

        // Generate response
        String botResponse = shouldEscalate
            ? responseGeneratorService.getEscalationMessage()
            : responseGeneratorService.generateResponse(intent, entities, sentiment);

        // Persist conversation
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
        if (sessionId == null || sessionId.isBlank()) {
            return UUID.randomUUID().toString();
        }
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
