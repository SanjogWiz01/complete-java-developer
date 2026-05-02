package com.example.chatbot.service;

import com.example.chatbot.model.Intent;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class NlpService {

    private static final List<Intent> INTENTS = List.of(
            new Intent("ORDER_STATUS", Set.of("order", "status", "tracking", "track"),
                    "Sure — I can help with order tracking. Please share your order ID."),
            new Intent("REFUND", Set.of("refund", "return", "money", "cancel"),
                    "I can assist with a refund request. Please provide your order ID and purchase date."),
            new Intent("ACCOUNT", Set.of("account", "password", "login", "sign"),
                    "For account help, please tell me whether this is a login or password issue."),
            new Intent("PRICING", Set.of("price", "pricing", "cost", "plan"),
                    "Our plans vary by usage. I can share pricing details based on your needs.")
    );

    private static final Map<String, Integer> SENTIMENT_LEXICON = Map.of(
            "great", 2, "good", 1, "awesome", 2, "love", 2,
            "bad", -1, "terrible", -2, "angry", -2, "problem", -1
    );

    public IntentMatch detectIntent(String text) {
        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(text.toLowerCase(Locale.ROOT));
        Intent bestIntent = null;
        int bestScore = 0;

        for (Intent intent : INTENTS) {
            int score = 0;
            for (String token : tokens) {
                if (intent.keywords().contains(token)) {
                    score++;
                }
            }
            if (score > bestScore) {
                bestScore = score;
                bestIntent = intent;
            }
        }

        if (bestIntent == null) {
            return new IntentMatch("GENERAL", 0.2,
                    "Thanks for reaching out. Could you share more details so I can route this correctly?");
        }

        double confidence = Math.min(0.95, 0.4 + (bestScore * 0.2));
        return new IntentMatch(bestIntent.name(), confidence, bestIntent.responseTemplate());
    }

    public String analyzeSentiment(String text) {
        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(text.toLowerCase(Locale.ROOT));
        int score = 0;
        for (String token : tokens) {
            score += SENTIMENT_LEXICON.getOrDefault(token, 0);
        }
        if (score > 0) {
            return "POSITIVE";
        }
        if (score < 0) {
            return "NEGATIVE";
        }
        return "NEUTRAL";
    }

    public record IntentMatch(String intent, double confidence, String responseTemplate) {
    }
}
