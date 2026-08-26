/*
 * 13_Spring_Boot_Design_Patterns.java
 *
 * How design patterns naturally appear in Spring Boot.
 *
 * 1. Singleton-like beans:
 *    Spring beans are singleton-scoped by default.
 *
 * 2. Factory:
 *    ApplicationContext creates and manages objects.
 *
 * 3. Dependency Injection / IoC:
 *    Dependencies are supplied by the container.
 *
 * 4. Proxy:
 *    Spring AOP, transactions, security, etc. commonly use proxies.
 *
 * 5. Template Method:
 *    Spring's template-style APIs encapsulate repetitive workflow.
 *
 * 6. Strategy:
 *    Interfaces and multiple implementations can be selected/injected.
 */
package com.example.springboot;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

interface PricingStrategy {
    double price(double base);
}

@Component
class RegularPricing implements PricingStrategy {
    public double price(double base) {
        return base;
    }
}

@Component
class StudentPricing implements PricingStrategy {
    public double price(double base) {
        return base * 0.90;
    }
}

@Service
class PricingService {

    private final List<PricingStrategy> strategies;

    PricingService(List<PricingStrategy> strategies) {
        this.strategies = strategies;
    }

    public double regular(double base) {
        return strategies.stream()
                .filter(s -> s instanceof RegularPricing)
                .findFirst()
                .orElseThrow()
                .price(base);
    }

    public double student(double base) {
        return strategies.stream()
                .filter(s -> s instanceof StudentPricing)
                .findFirst()
                .orElseThrow()
                .price(base);
    }
}

/*
 * Better production design:
 * Prefer @Qualifier or a map keyed by strategy name instead of instanceof.
 *
 * The main lesson is that Spring does not require you to manually implement
 * every Gang-of-Four pattern. The framework itself uses many patterns.
 */
