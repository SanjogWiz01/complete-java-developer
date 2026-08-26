/*
 * 14_Spring_Boot_Spring_Session_Redis.java
 *
 * Distributed session concept.
 *
 * Use Spring Session when session state needs to be externalized from
 * a single application server, commonly into Redis.
 *
 * Dependencies:
 * - spring-boot-starter-web
 * - spring-boot-starter-session-data-redis
 * - Redis client is provided by the starter dependency chain.
 *
 * application.properties example:
 *
 * spring.session.timeout=30m
 * spring.data.redis.host=localhost
 * spring.data.redis.port=6379
 *
 * Depending on the Spring Boot version and chosen starter, exact property
 * names and auto-configuration can differ. Check the current Spring Boot
 * reference for the version used by your project.
 */
package com.example.springboot;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistributedSessionController {

    @GetMapping("/distributed/session")
    public String session(HttpSession session) {

        Integer visits = (Integer) session.getAttribute("visits");

        if (visits == null) {
            visits = 0;
        }

        visits++;
        session.setAttribute("visits", visits);

        return "Session ID=" + session.getId()
                + ", visits=" + visits;
    }
}

/*
 * Architecture:
 *
 * Browser
 *   |
 *   | SESSION cookie
 *   v
 * Load Balancer
 *   |
 *   +----> Spring Boot instance A
 *   |
 *   +----> Spring Boot instance B
 *              |
 *              v
 *             Redis
 *
 * The session data is centralized, so requests can reach different
 * application instances without losing session state.
 */
