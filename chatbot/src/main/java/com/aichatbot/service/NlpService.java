package com.aichatbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class NlpService {

    private static final Logger log = LoggerFactory.getLogger(NlpService.class);

    private static final Map<String, List<String>> INTENT_PATTERNS = new LinkedHashMap<>();

    static {
        INTENT_PATTERNS.put("GREETING", Arrays.asList(
            "hello", "hi", "hey", "good morning", "good afternoon",
            "good evening", "howdy", "greetings", "what's up"
        ));
        INTENT_PATTERNS.put("ORDER_STATUS", Arrays.asList(
            "order status", "where is my order", "track order", "order tracking",
            "delivery status", "when will my order arrive", "order update", "shipment"
        ));
        INTENT_PATTERNS.put("REFUND_REQUEST", Arrays.asList(
            "refund", "money back", "return", "cancel order", "get my money",
            "charge back", "reimburse", "return policy"
        ));
        INTENT_PATTERNS.put("PRODUCT_INFO", Arrays.asList(
            "product", "item", "specification", "feature", "detail", "describe",
            "tell me about", "what is", "how does", "price", "cost", "how much"
        ));
        INTENT_PATTERNS.put("TECHNICAL_SUPPORT", Arrays.asList(
            "not working", "broken", "error", "issue", "problem", "bug",
            "crash", "glitch", "help me fix", "troubleshoot", "doesn't work", "failed"
        ));
        INTENT_PATTERNS.put("ACCOUNT_HELP", Arrays.asList(
            "account", "password", "login", "sign in", "forgot password",
            "reset password", "email change", "username", "profile"
        ));
        INTENT_PATTERNS.put("SHIPPING_INFO", Arrays.asList(
            "shipping", "delivery", "ship to", "shipping cost", "free shipping",
            "delivery time", "how long", "shipping options", "expedited"
        ));
        INTENT_PATTERNS.put("COMPLAINT", Arrays.asList(
            "complaint", "unhappy", "dissatisfied", "disappointed", "terrible",
            "awful", "worst", "bad experience", "frustrated", "angry"
        ));
        INTENT_PATTERNS.put("FAREWELL", Arrays.asList(
            "bye", "goodbye", "see you", "take care", "farewell",
            "thanks bye", "that's all", "done", "exit"
        ));
    }

    public String detectIntent(String message) {
        String normalized = message.toLowerCase().trim();
        String bestIntent = "GENERAL_INQUIRY";
        int maxMatches = 0;

        for (Map.Entry<String, List<String>> entry : INTENT_PATTERNS.entrySet()) {
            int matches = 0;
            for (String pattern : entry.getValue()) {
                if (normalized.contains(pattern)) matches++;
            }
            if (matches > maxMatches) {
                maxMatches = matches;
                bestIntent = entry.getKey();
            }
        }

        log.debug("Detected intent: {} for message: {}", bestIntent, message);
        return bestIntent;
    }

    public double calculateConfidence(String message, String intent) {
        if ("GENERAL_INQUIRY".equals(intent)) return 0.3;
        String normalized = message.toLowerCase();
        List<String> patterns = INTENT_PATTERNS.getOrDefault(intent, Collections.emptyList());
        long matches = patterns.stream().filter(normalized::contains).count();
        double confidence = Math.min(0.95, 0.5 + (matches * 0.15));
        return Math.round(confidence * 100.0) / 100.0;
    }

    public Map<String, String> extractEntities(String message) {
        Map<String, String> entities = new HashMap<>();
        Pattern orderPattern = Pattern.compile("(?:ORD-|#|order\\s+)?(\\d{5,10})", Pattern.CASE_INSENSITIVE);
        var orderMatcher = orderPattern.matcher(message);
        if (orderMatcher.find()) entities.put("ORDER_NUMBER", orderMatcher.group(1));
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        var emailMatcher = emailPattern.matcher(message);
        if (emailMatcher.find()) entities.put("EMAIL", emailMatcher.group());
        return entities;
    }

    public String analyzeSentiment(String message) {
        String normalized = message.toLowerCase();
        List<String> negativeWords = Arrays.asList(
            "angry", "frustrated", "terrible", "awful", "hate", "worst",
            "bad", "horrible", "unacceptable", "disgusting", "furious"
        );
        List<String> positiveWords = Arrays.asList(
            "great", "excellent", "amazing", "love", "fantastic", "wonderful",
            "happy", "satisfied", "perfect", "brilliant", "awesome"
        );
        long negCount = negativeWords.stream().filter(normalized::contains).count();
        long posCount = positiveWords.stream().filter(normalized::contains).count();
        if (negCount > posCount) return "NEGATIVE";
        if (posCount > negCount) return "POSITIVE";
        return "NEUTRAL";
    }
}
