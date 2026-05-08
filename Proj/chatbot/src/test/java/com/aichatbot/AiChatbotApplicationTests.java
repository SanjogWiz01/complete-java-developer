package com.aichatbot;

import com.aichatbot.dto.ChatRequest;
import com.aichatbot.dto.ChatResponse;
import com.aichatbot.service.NlpService;
import com.aichatbot.service.ResponseGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AiChatbotApplicationTests {

    @Autowired
    private NlpService nlpService;

    @Autowired
    private ResponseGeneratorService responseGeneratorService;

    @Test
    void contextLoads() {
    }

    @Test
    void testGreetingIntentDetection() {
        String intent = nlpService.detectIntent("Hello, I need help");
        assertThat(intent).isEqualTo("GREETING");
    }

    @Test
    void testOrderStatusIntentDetection() {
        String intent = nlpService.detectIntent("Where is my order? I want to track order 12345");
        assertThat(intent).isEqualTo("ORDER_STATUS");
    }

    @Test
    void testRefundIntentDetection() {
        String intent = nlpService.detectIntent("I want a refund for my purchase");
        assertThat(intent).isEqualTo("REFUND_REQUEST");
    }

    @Test
    void testNegativeSentiment() {
        String sentiment = nlpService.analyzeSentiment("I am so angry and frustrated with this terrible service");
        assertThat(sentiment).isEqualTo("NEGATIVE");
    }

    @Test
    void testPositiveSentiment() {
        String sentiment = nlpService.analyzeSentiment("This is amazing and I love the service");
        assertThat(sentiment).isEqualTo("POSITIVE");
    }

    @Test
    void testOrderNumberExtraction() {
        var entities = nlpService.extractEntities("My order number is ORD-98765");
        assertThat(entities).containsKey("ORDER_NUMBER");
        assertThat(entities.get("ORDER_NUMBER")).isEqualTo("98765");
    }

    @Test
    void testEmailExtraction() {
        var entities = nlpService.extractEntities("My email is user@example.com");
        assertThat(entities).containsKey("EMAIL");
        assertThat(entities.get("EMAIL")).isEqualTo("user@example.com");
    }

    @Test
    void testConfidenceScore() {
        double confidence = nlpService.calculateConfidence("track my order shipment", "ORDER_STATUS");
        assertThat(confidence).isGreaterThan(0.5).isLessThanOrEqualTo(1.0);
    }

    @Test
    void testEscalationLogic() {
        boolean escalate = responseGeneratorService.shouldEscalate("COMPLAINT", "NEGATIVE", 1);
        assertThat(escalate).isTrue();
    }
}
