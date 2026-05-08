package com.aichatbot.service;

import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Generates appropriate responses based on detected intents.
 * In production, this can be enhanced with LLM APIs (OpenAI, Claude, etc.)
 */
@Service
public class ResponseGeneratorService {

    private static final Map<String, List<String>> RESPONSES = new HashMap<>();
    private final Random random = new Random();

    static {
        RESPONSES.put("GREETING", Arrays.asList(
            "Hello! Welcome to our support center. How can I assist you today? 😊",
            "Hi there! I'm your AI support assistant. What can I help you with?",
            "Hey! Great to see you. I'm here to help — what do you need assistance with?"
        ));

        RESPONSES.put("ORDER_STATUS", Arrays.asList(
            "I'd be happy to check your order status! Could you please provide your order number (e.g., ORD-12345)?",
            "Sure! To track your order, I'll need your order number. You can find it in your confirmation email.",
            "Let me look that up for you! Please share your order number and I'll get the latest status right away."
        ));

        RESPONSES.put("REFUND_REQUEST", Arrays.asList(
            "I understand you'd like a refund. Our refund policy allows returns within 30 days of purchase. "
            + "Please share your order number and reason for the return, and I'll process this for you.",
            "I'm sorry to hear you'd like a refund. I can help with that! "
            + "Please provide your order number and we'll get the process started within 24 hours.",
            "Absolutely, I can assist with your refund request. "
            + "Could you share your order number and a brief reason? Refunds typically process in 3-5 business days."
        ));

        RESPONSES.put("PRODUCT_INFO", Arrays.asList(
            "I'd love to tell you more about our products! Which specific product or category are you interested in?",
            "Great question! Could you specify which product you'd like more details about? "
            + "I can share specifications, pricing, and availability.",
            "Sure! Please let me know the product name or category, and I'll provide you with all the details."
        ));

        RESPONSES.put("TECHNICAL_SUPPORT", Arrays.asList(
            "I'm sorry you're experiencing technical difficulties! Let's get this resolved. "
            + "Can you describe the issue in detail, including any error messages you're seeing?",
            "Technical issues can be frustrating — let me help! "
            + "Please tell me what's happening, what device/OS you're using, and when this started.",
            "I'll help you troubleshoot this right away! Could you describe the problem step by step? "
            + "The more details you provide, the faster we can find a solution."
        ));

        RESPONSES.put("ACCOUNT_HELP", Arrays.asList(
            "I can help with your account! For security purposes, I'll need to verify your identity. "
            + "Could you provide the email address associated with your account?",
            "Account issues are my specialty! What specifically do you need help with — "
            + "password reset, email update, or something else?",
            "Sure, let's get your account sorted out. "
            + "Please share your registered email and I'll walk you through the next steps."
        ));

        RESPONSES.put("SHIPPING_INFO", Arrays.asList(
            "Great question about shipping! We offer Standard (5-7 days), Express (2-3 days), "
            + "and Overnight shipping. Free standard shipping on orders over $50!",
            "Here's our shipping info: Standard delivery takes 5-7 business days, "
            + "Express takes 2-3 days. We ship to 50+ countries worldwide!",
            "We have several shipping options available! Standard ($4.99, 5-7 days), "
            + "Express ($12.99, 2-3 days), and Overnight ($24.99). Orders over $50 get free standard shipping."
        ));

        RESPONSES.put("COMPLAINT", Arrays.asList(
            "I sincerely apologize for the poor experience you've had. "
            + "Your feedback is very important to us. Could you share more details so I can make this right?",
            "I'm truly sorry to hear this. You deserve better, and I want to resolve this immediately. "
            + "Please tell me what happened and I'll escalate this to our priority team.",
            "I apologize for the inconvenience. Your satisfaction is our top priority. "
            + "Let me connect you with a senior support specialist who can resolve this right away."
        ));

        RESPONSES.put("FAREWELL", Arrays.asList(
            "Thank you for reaching out! Have a wonderful day! 👋",
            "It was a pleasure helping you! Don't hesitate to reach out if you need anything. Take care!",
            "Goodbye! I hope I was able to help. Have a great day! 😊"
        ));

        RESPONSES.put("GENERAL_INQUIRY", Arrays.asList(
            "Thanks for reaching out! I can help with orders, shipping, refunds, products, "
            + "technical support, and account issues. What do you need help with?",
            "I'm here to help! I can assist with order tracking, returns, product info, "
            + "technical issues, and more. What's on your mind?",
            "Happy to help! Could you provide more details about what you're looking for? "
            + "I handle support for orders, products, accounts, and technical issues."
        ));
    }

    /**
     * Generate a response based on the detected intent.
     */
    public String generateResponse(String intent, Map<String, String> entities, String sentiment) {
        List<String> responseList = RESPONSES.getOrDefault(intent, RESPONSES.get("GENERAL_INQUIRY"));
        String baseResponse = responseList.get(random.nextInt(responseList.size()));

        // Personalize based on extracted entities
        if (entities.containsKey("ORDER_NUMBER") && "ORDER_STATUS".equals(intent)) {
            return String.format(
                "I found order #%s in our system! It is currently being processed and is estimated "
                + "to arrive within 3-5 business days. You'll receive an email with tracking info shortly.",
                entities.get("ORDER_NUMBER")
            );
        }

        // Add empathy for negative sentiment
        if ("NEGATIVE".equals(sentiment) && !intent.equals("COMPLAINT")) {
            baseResponse = "I understand your frustration, and I'm here to help. " + baseResponse;
        }

        return baseResponse;
    }

    /**
     * Determine if the conversation should be escalated to a human agent.
     */
    public boolean shouldEscalate(String intent, String sentiment, int messageCount) {
        return "COMPLAINT".equals(intent) && "NEGATIVE".equals(sentiment)
            || messageCount > 5 && "GENERAL_INQUIRY".equals(intent)
            || "NEGATIVE".equals(sentiment) && messageCount > 3;
    }

    /**
     * Generate escalation message.
     */
    public String getEscalationMessage() {
        return "I'm connecting you with a human support specialist who can better assist you. "
            + "Expected wait time: 2-3 minutes. Thank you for your patience! 🔄";
    }
}
