package unit7.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SimpleWebServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/health", exchange -> {
            String json = "{\"status\":\"UP\",\"service\":\"inventory\"}";
            send(exchange, 200, json, "application/json");
        });

        server.createContext("/api/products", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, "{\"error\":\"Method Not Allowed\"}", "application/json");
                return;
            }
            String json = "[{\"id\":1,\"name\":\"Keyboard\"},{\"id\":2,\"name\":\"Mouse\"}]";
            send(exchange, 200, json, "application/json");
        });

        server.start();
        System.out.println("Server running at http://localhost:8080");
    }

    private static void send(HttpExchange exchange, int status, String body, String type)
            throws IOException {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", type);
        exchange.sendResponseHeaders(status, data.length);
        try (var out = exchange.getResponseBody()) {
            out.write(data);
        }
    }
}
