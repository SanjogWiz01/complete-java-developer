# 🤖 AI-Powered Customer Support Chatbot

> An intelligent customer support chatbot built with **Java**, **Spring Boot**, and **NLP** (Natural Language Processing) to handle customer queries automatically and escalate when needed.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green?style=flat-square&logo=springboot)
![Apache OpenNLP](https://img.shields.io/badge/Apache_OpenNLP-2.3-blue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

---

## ✨ Features

- **🧠 NLP Intent Detection** — Identifies 10 customer support intents (orders, refunds, shipping, technical help, etc.)
- **😊 Sentiment Analysis** — Detects positive, negative, and neutral tones and adapts responses
- **🔍 Entity Extraction** — Extracts order numbers, emails, and other key data from messages
- **📈 Confidence Scoring** — Rates how confident the model is about each detected intent
- **🔄 Smart Escalation** — Automatically routes frustrated customers to human agents
- **💬 Real-time Chat UI** — Beautiful dark-themed frontend with typing indicators and quick replies
- **📊 Conversation Persistence** — All chats stored in H2 database with full history
- **🚀 REST API** — Clean JSON API for integration with any frontend or mobile app
- **✅ CI/CD Pipeline** — GitHub Actions workflow for automated testing and builds

---

## 🏗️ Project Structure

```
ai-customer-support-chatbot/
├── src/
│   ├── main/
│   │   ├── java/com/aichatbot/
│   │   │   ├── AiChatbotApplication.java     # Main entry point
│   │   │   ├── controller/
│   │   │   │   ├── ChatController.java        # REST API endpoints
│   │   │   │   └── WebController.java         # Frontend page routing
│   │   │   ├── service/
│   │   │   │   ├── NlpService.java            # Intent detection, sentiment, entity extraction
│   │   │   │   ├── ResponseGeneratorService.java  # Generates bot responses
│   │   │   │   └── ChatbotService.java        # Orchestrates the full pipeline
│   │   │   ├── model/
│   │   │   │   └── ChatMessage.java           # JPA entity for conversation history
│   │   │   ├── dto/
│   │   │   │   ├── ChatRequest.java           # Incoming message payload
│   │   │   │   └── ChatResponse.java          # Outgoing response payload
│   │   │   └── repository/
│   │   │       └── ChatMessageRepository.java # JPA data access layer
│   │   └── resources/
│   │       ├── templates/index.html           # Chat frontend (Thymeleaf)
│   │       └── application.properties         # App configuration
│   └── test/
│       └── java/com/aichatbot/
│           └── AiChatbotApplicationTests.java # Unit tests
├── .github/workflows/ci.yml                   # GitHub Actions CI/CD
├── pom.xml                                    # Maven dependencies
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Git

### Clone & Run

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/ai-customer-support-chatbot.git
cd ai-customer-support-chatbot

# 2. Build the project
mvn clean install

# 3. Run the application
mvn spring-boot:run

# 4. Open in browser
open http://localhost:8080
```

---

## 🔌 API Reference

### Send a Message
```http
POST /api/chat/message
Content-Type: application/json

{
  "message": "Where is my order #12345?",
  "sessionId": "session-abc-123"
}
```

**Response:**
```json
{
  "message": "I found order #12345! It is currently being processed...",
  "intent": "ORDER_STATUS",
  "confidence": 0.80,
  "sessionId": "session-abc-123",
  "timestamp": "2024-01-15T10:30:00",
  "isEscalated": false
}
```

### Health Check
```http
GET /api/chat/health
```

### List Supported Intents
```http
GET /api/chat/intents
```

---

## 🧠 Supported Intents

| Intent | Example Phrases |
|--------|----------------|
| `GREETING` | "Hello", "Hi there", "Good morning" |
| `ORDER_STATUS` | "Where is my order?", "Track my package" |
| `REFUND_REQUEST` | "I want a refund", "Return policy", "Money back" |
| `PRODUCT_INFO` | "Tell me about...", "How much does X cost?" |
| `TECHNICAL_SUPPORT` | "It's not working", "I'm getting an error" |
| `ACCOUNT_HELP` | "Reset my password", "Change email" |
| `SHIPPING_INFO` | "How long does shipping take?", "Delivery options" |
| `COMPLAINT` | "Terrible experience", "I'm very unhappy" |
| `FAREWELL` | "Goodbye", "Thanks, bye" |
| `GENERAL_INQUIRY` | Anything else |

---

## 🔧 Configuration

Edit `src/main/resources/application.properties`:

```properties
server.port=8080
chatbot.max-session-messages=20
chatbot.escalation-threshold=5
chatbot.name=SupportBot
```

---

## 🧪 Running Tests

```bash
mvn test
```

Tests cover:
- Intent detection accuracy
- Sentiment analysis
- Entity extraction (order numbers, emails)
- Confidence scoring
- Escalation logic

---

## 🛣️ Roadmap

- [ ] Integrate OpenAI GPT or Anthropic Claude API for smarter responses
- [ ] Add Apache OpenNLP trained models for better intent classification
- [ ] WebSocket support for real-time streaming responses
- [ ] Multi-language support
- [ ] Admin dashboard for conversation analytics
- [ ] PostgreSQL support for production deployments
- [ ] Docker containerization

---

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

## 👤 Author

Built with ❤️ using Java, Spring Boot, and NLP.
