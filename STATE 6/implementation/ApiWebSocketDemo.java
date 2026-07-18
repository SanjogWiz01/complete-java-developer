import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Base64;

public class ApiWebSocketDemo {

    public static void main(String[] args) {
        System.out.println("=== API WebSocket Demo ===\n");

        System.out.println("1. WebSocket Handshake Concept:");
        demonstrateWebSocketHandshake();

        System.out.println("\n2. Server-Sent Events (SSE) Concept:");
        demonstrateSSEConcept();

        System.out.println("\n3. Long Polling Concept:");
        demonstrateLongPollingConcept();

        System.out.println("\n4. HTTP/2 Server Push Concept:");
        demonstrateHTTP2Concept();

        System.out.println("\n5. WebSocket Frame Structure:");
        demonstrateFrameStructure();
    }

    private static void demonstrateWebSocketHandshake() {
        System.out.println("  WebSocket uses HTTP Upgrade mechanism:");
        System.out.println("  GET /chat HTTP/1.1");
        System.out.println("  Host: server.example.com");
        System.out.println("  Upgrade: websocket");
        System.out.println("  Connection: Upgrade");
        System.out.println("  Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==");
        System.out.println("  Sec-WebSocket-Version: 13");

        String wsKey = "dGhlIHNhbXBsZSBub25jZQ==";
        String acceptKey = generateWebSocketAcceptKey(wsKey);
        System.out.println("  Server response:");
        System.out.println("  HTTP/1.1 101 Switching Protocols");
        System.out.println("  Upgrade: websocket");
        System.out.println("  Connection: Upgrade");
        System.out.println("  Sec-WebSocket-Accept: " + acceptKey);
    }

    private static String generateWebSocketAcceptKey(String webSocketKey) {
        String magicGuid = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        String combined = webSocketKey + magicGuid;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(combined.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            return "Error computing accept key";
        }
    }

    private static void demonstrateSSEConcept() {
        System.out.println("  Server-Sent Events (SSE) format:");
        System.out.println("  Content-Type: text/event-stream");
        System.out.println("  Cache-Control: no-cache");
        System.out.println("  Connection: keep-alive");
        System.out.println();
        System.out.println("  Event format:");
        System.out.println("  event: message");
        System.out.println("  id: 12345");
        System.out.println("  retry: 5000");
        System.out.println("  data: {\"type\": \"update\", \"value\": 42}");
        System.out.println();
        System.out.println("  Java SSE Client concept:");
        System.out.println("  URL url = new URL(\"http://example.com/events\");");
        System.out.println("  HttpURLConnection con = (HttpURLConnection) url.openConnection();");
        System.out.println("  con.setRequestProperty(\"Accept\", \"text/event-stream\");");
        System.out.println("  BufferedReader reader = new BufferedReader(");
        System.out.println("      new InputStreamReader(con.getInputStream()));");
        System.out.println("  String line;");
        System.out.println("  while ((line = reader.readLine()) != null) {");
        System.out.println("      if (line.startsWith(\"data:\")) {");
        System.out.println("          String data = line.substring(5).trim();");
        System.out.println("          processEvent(data);");
        System.out.println("      }");
        System.out.println("  }");
    }

    private static void demonstrateLongPollingConcept() {
        System.out.println("  Long Polling vs Regular Polling vs WebSocket:");
        System.out.println();
        System.out.println("  Regular Polling:");
        System.out.println("    Client -> Server: GET /api/messages (every 5s)");
        System.out.println("    Server -> Client: {messages: []} (often empty)");
        System.out.println();
        System.out.println("  Long Polling:");
        System.out.println("    Client -> Server: GET /api/messages (hold connection)");
        System.out.println("    Server -> Client: Waits until data available, then responds");
        System.out.println("    Client -> Server: Immediately reconnects");
        System.out.println();
        System.out.println("  WebSocket:");
        System.out.println("    Client -> Server: HTTP Upgrade Request");
        System.out.println("    Server -> Client: 101 Switching Protocols");
        System.out.println("    Bidirectional: Full-duplex communication");
        System.out.println();
        System.out.println("  Long Polling Java implementation concept:");
        System.out.println("  while (running) {");
        System.out.println("      HttpURLConnection con = createConnection(\"/api/messages\");");
        System.out.println("      con.setReadTimeout(30000);");
        System.out.println("      String response = readResponse(con);");
        System.out.println("      if (!response.isEmpty()) {");
        System.out.println("          processMessage(response);");
        System.out.println("      }");
        System.out.println("  }");
    }

    private static void demonstrateHTTP2Concept() {
        System.out.println("  HTTP/2 Features:");
        System.out.println("  - Multiplexing: Multiple requests over single connection");
        System.out.println("  - Header Compression: HPACK compression");
        System.out.println("  - Server Push: Server can push resources proactively");
        System.out.println("  - Stream Prioritization: Request prioritization");
        System.out.println();
        System.out.println("  Java HTTP/2 support:");
        System.out.println("  HttpClient client = HttpClient.newBuilder()");
        System.out.println("      .version(HttpClient.Version.HTTP_2)");
        System.out.println("      .build();");
        System.out.println("  HttpRequest request = HttpRequest.newBuilder()");
        System.out.println("      .uri(URI.create(\"https://example.com\"))");
        System.out.println("      .build();");
        System.out.println("  HttpResponse<String> response = client.send(");
        System.out.println("      request, HttpResponse.BodyHandlers.ofString());");
    }

    private static void demonstrateFrameStructure() {
        System.out.println("  WebSocket Frame Structure:");
        System.out.println("  +---+---+---+---+---+---+---+---+");
        System.out.println("  |F|R|R|R| opcode|M| Payload Len |");
        System.out.println("  |I|S|S|S|  (4)  |A|     (7)     |");
        System.out.println("  |N|V|V|V|       |S|             |");
        System.out.println("  | |1|2|3|       |K|             |");
        System.out.println("  +---+---+---+---+---+---+---+---+");
        System.out.println();
        System.out.println("  Opcodes:");
        System.out.println("  0x0: Continuation frame");
        System.out.println("  0x1: Text frame");
        System.out.println("  0x2: Binary frame");
        System.out.println("  0x8: Connection close");
        System.out.println("  0x9: Ping");
        System.out.println("  0xA: Pong");
    }
}
