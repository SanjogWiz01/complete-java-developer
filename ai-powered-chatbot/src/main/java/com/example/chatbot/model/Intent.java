package com.example.chatbot.model;

import java.util.Set;

public record Intent(String name, Set<String> keywords, String responseTemplate) {
}
