package com.example.chatbot.service;

import com.example.chatbot.dto.ChatResponse;
import com.example.chatbot.model.ConversationTurn;
import com.example.chatbot.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ChatbotService {

    private final NlpService nlpService;
    private final ConversationRepository conversationRepository;

    public ChatbotService(NlpService nlpService, ConversationRepository conversationRepository) {
        this.nlpService = nlpService;
        this.conversationRepository = conversationRepository;
    }

    public ChatResponse chat(String sessionId, String userMessage) {
        String resolvedSessionId = sessionId == null || sessionId.isBlank()
                ? UUID.randomUUID().toString()
                : sessionId;

        NlpService.IntentMatch intentMatch = nlpService.detectIntent(userMessage);
        String sentiment = nlpService.analyzeSentiment(userMessage);

        String reply = decorateForSentiment(intentMatch.responseTemplate(), sentiment);

        conversationRepository.save(new ConversationTurn(
                resolvedSessionId,
                userMessage,
                reply,
                intentMatch.intent(),
                Instant.now()
        ));

        return new ChatResponse(
                resolvedSessionId,
                userMessage,
                reply,
                intentMatch.intent(),
                intentMatch.confidence(),
                sentiment
        );
    }

    private String decorateForSentiment(String baseReply, String sentiment) {
        return switch (sentiment) {
            case "NEGATIVE" -> "I am sorry for the frustration. " + baseReply;
            case "POSITIVE" -> "Glad to hear from you! " + baseReply;
            default -> baseReply;
        };
    }
}
