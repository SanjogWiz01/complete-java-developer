# AI-Powered Customer Support Chatbot (Java + Spring Boot)

This is a portfolio-ready starter project you can publish to your GitHub Projects section.

## Features
- REST API for chatbot interactions
- Basic NLP intent detection (Apache OpenNLP tokenizer)
- Lightweight sentiment analysis
- Session-based conversation memory (in-memory repository)
- Clean layered architecture (controller/service/repository/model)

## Tech Stack
- Java 17
- Spring Boot 3
- Apache OpenNLP
- Maven

## Run locally
```bash
cd ai-powered-chatbot
mvn spring-boot:run
```

## Example request
```bash
curl -X POST http://localhost:8080/api/v1/chat \
  -H 'Content-Type: application/json' \
  -d '{"message":"I want a refund for my order 123"}'
```

## Example response
```json
{
  "sessionId": "a-generated-session-id",
  "userMessage": "I want a refund for my order 123",
  "botReply": "I can assist with a refund request. Please provide your order ID and purchase date.",
  "detectedIntent": "REFUND",
  "confidence": 0.8,
  "sentiment": "NEUTRAL"
}
```

## Next upgrades (to make it truly production-grade)
1. Persist conversations to PostgreSQL
2. Add authentication and rate limiting
3. Add vector search + LLM integration (OpenAI/Azure/Open-source model)
4. Add FAQ retrieval from documents (RAG)
5. Deploy to Docker + cloud (Render/AWS/GCP)
