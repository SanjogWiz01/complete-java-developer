package com.example.chatbot.repository;

import com.example.chatbot.model.ConversationTurn;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConversationRepository {

    private final Map<String, List<ConversationTurn>> sessions = new ConcurrentHashMap<>();

    public void save(ConversationTurn turn) {
        sessions.computeIfAbsent(turn.sessionId(), key -> new ArrayList<>()).add(turn);
    }

    public List<ConversationTurn> findSessionTurns(String sessionId) {
        return sessions.getOrDefault(sessionId, List.of())
                .stream()
                .sorted(Comparator.comparing(ConversationTurn::timestamp))
                .toList();
    }
}
