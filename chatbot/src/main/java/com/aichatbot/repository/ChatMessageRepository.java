package com.aichatbot.repository;

import com.aichatbot.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySessionIdOrderByTimestampAsc(String sessionId);

    @Query("SELECT c.intent, COUNT(c) FROM ChatMessage c GROUP BY c.intent ORDER BY COUNT(c) DESC")
    List<Object[]> findIntentFrequency();

    long countBySessionId(String sessionId);
}
