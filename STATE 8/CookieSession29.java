/*
 * CookieSession29.java
 *
 * Topic: Session lifecycle listeners.
 *
 * HttpSessionListener is notified when a session is created or destroyed,
 * including the implicit destroy that happens on timeout.
 *
 * Register the listener with @WebListener, or declare it in web.xml.
 */
package com.example.web;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@WebListener
public class CookieSession29 implements HttpSessionListener, HttpSessionAttributeListener {

    private static final Map<String, Long> CREATED_AT = new ConcurrentHashMap<>();
    private static final AtomicLong TOTAL = new AtomicLong();

    public static Map<String, Long> snapshot() {
        return Map.copyOf(CREATED_AT);
    }

    public static long total() {
        return TOTAL.get();
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        CREATED_AT.put(event.getSession().getId(), event.getSession().getCreationTime());
        TOTAL.incrementAndGet();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        CREATED_AT.remove(event.getSession().getId());
    }

    @Override
    public void attributeAdded(HttpSessionEvent event, String name) {
        if (isIgnored(name)) {
            return;
        }
        log("added", event, name);
    }

    @Override
    public void attributeReplaced(HttpSessionEvent event, String name) {
        if (isIgnored(name)) {
            return;
        }
        log("replaced", event, name);
    }

    @Override
    public void attributeRemoved(HttpSessionEvent event, String name) {
        if (isIgnored(name)) {
            return;
        }
        log("removed", event, name);
    }

    private boolean isIgnored(String name) {
        return name != null && name.startsWith("org.springframework");
    }

    private void log(String action, HttpSessionEvent event, String name) {
        System.out.println("[session] " + action + " attribute '" + name
                + "' on session " + event.getSession().getId());
    }
}
