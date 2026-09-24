/*
 * 05_Session_Timeout_Listener.java
 *
 * Demonstrates session lifecycle events and timeout.
 */
package com.example.web;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionLifecycleListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        System.out.println("Session created: " + event.getSession().getId());

        // Example timeout: 15 minutes.
        event.getSession().setMaxInactiveInterval(15 * 60);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("Session destroyed: " + event.getSession().getId());
    }
}

/*
 * Spring Boot alternative:
 *
 * server.servlet.session.timeout=15m
 *
 * The session timeout is server-side configuration; the session ID
 * is commonly carried by a browser cookie.
 */
