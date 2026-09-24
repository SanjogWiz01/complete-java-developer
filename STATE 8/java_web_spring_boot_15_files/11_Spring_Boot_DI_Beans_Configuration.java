/*
 * 11_Spring_Boot_DI_Beans_Configuration.java
 *
 * Core Spring concepts:
 * - @Component
 * - @Service
 * - @Repository
 * - @Controller / @RestController
 * - @Configuration
 * - @Bean
 * - Constructor injection
 */
package com.example.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
class ClockService {
    public long currentTime() {
        return System.currentTimeMillis();
    }
}

interface MessageSender {
    void send(String message);
}

@Component
class ConsoleMessageSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println(message);
    }
}

@Configuration
class AppConfig {

    @Bean
    String applicationName() {
        return "Cookie Session Pattern Demo";
    }
}

@Service
class NotificationService {

    private final MessageSender sender;
    private final ClockService clock;
    private final String applicationName;

    // Constructor injection makes dependencies explicit and testable.
    NotificationService(
            MessageSender sender,
            ClockService clock,
            String applicationName) {

        this.sender = sender;
        this.clock = clock;
        this.applicationName = applicationName;
    }

    public void notifyUser(String message) {
        sender.send(applicationName + " [" + clock.currentTime() + "]: " + message);
    }
}

/*
 * Spring Boot scans components and creates a dependency graph.
 * Avoid field injection when constructor injection is practical.
 */
