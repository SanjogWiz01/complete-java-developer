public class SerializationWorkshop {

    public static void main(String[] args) {
        System.out.println("=== SERIALIZATION WORKSHOP: Jackson & Gson ===\n");
        jacksonBasics();
        jacksonAnnotations();
        gsonBasics();
        customSerialization();
    }

    private static void jacksonBasics() {
        System.out.println("--- 1. Jackson Basics ---\n");
        System.out.println("Jackson is the default JSON library in Spring Boot.\n");

        System.out.println("Serialization (Java Object -> JSON):");
        System.out.println("  ObjectMapper mapper = new ObjectMapper();");
        System.out.println("  Game game = new Game(1, \"Dragon Quest\", \"RPG\");");
        System.out.println("  String json = mapper.writeValueAsString(game);");
        System.out.println("  // Output: {\"id\":1,\"name\":\"Dragon Quest\",\"genre\":\"RPG\"}\n");

        System.out.println("Deserialization (JSON -> Java Object):");
        System.out.println("  String json = \"{\\\"id\\\":1,\\\"name\\\":\\\"Dragon Quest\\\",\\\"genre\\\":\\\"RPG\\\"}\";");
        System.out.println("  Game game = mapper.readValue(json, Game.class);");
        System.out.println("  System.out.println(game.getName()); // Dragon Quest\n");

        System.out.println("For collections:");
        System.out.println("  List<Game> games = mapper.readValue(jsonArray, new TypeReference<List<Game>>() {});\n");
    }

    private static void jacksonAnnotations() {
        System.out.println("--- 2. Jackson Annotations ---\n");

        System.out.println("@JsonProperty(\"name\")");
        System.out.println("  - Renames a field in JSON.");
        System.out.println("  - @JsonProperty(\"player_name\") private String playerName;\n");

        System.out.println("@JsonIgnore");
        System.out.println("  - Excludes a field from JSON output.");
        System.out.println("  - @JsonIgnore private String password;\n");

        System.out.println("@JsonFormat(pattern = \"yyyy-MM-dd\")");
        System.out.println("  - Formats date fields.");
        System.out.println("  - @JsonFormat(pattern = \"yyyy-MM-dd HH:mm\") private LocalDateTime createdAt;\n");

        System.out.println("@JsonInclude(Include.NON_NULL)");
        System.out.println("  - Omits null values from output.");
        System.out.println("  - Place on class: @JsonInclude(Include.NON_NULL)\n");

        System.out.println("@NoArgsConstructor, @AllArgsConstructor, @Getter, @Setter");
        System.out.println("  - Jackson requires a no-arg constructor for deserialization.");
        System.out.println("  - Lombok annotations generate these automatically.\n");
    }

    private static void gsonBasics() {
        System.out.println("--- 3. Gson Basics ---\n");
        System.out.println("Gson is Google's JSON library. Alternative to Jackson.\n");
        System.out.println("Dependency:");
        System.out.println("  <dependency>");
        System.out.println("    <groupId>com.google.code.gson</groupId>");
        System.out.println("    <artifactId>gson</artifactId>");
        System.out.println("    <version>2.10.1</version>");
        System.out.println("  </dependency>\n");

        System.out.println("Serialization:");
        System.out.println("  Gson gson = new Gson();");
        System.out.println("  Game game = new Game(1, \"Puzzle Master\", \"Puzzle\");");
        System.out.println("  String json = gson.toJson(game);");
        System.out.println("  // {\"id\":1,\"name\":\"Puzzle Master\",\"genre\":\"Puzzle\"}\n");

        System.out.println("Deserialization:");
        System.out.println("  Game game = gson.fromJson(json, Game.class);");
        System.out.println("  System.out.println(game.getName()); // Puzzle Master\n");

        System.out.println("Differences from Jackson:");
        System.out.println("  - Gson does not use annotations by default (uses field names).");
        System.out.println("  - Jackson has more features and is the Spring Boot default.");
        System.out.println("  - Gson is simpler for quick projects.\n");
    }

    private static void customSerialization() {
        System.out.println("--- 4. Custom Serialization with CustomSerializers ---\n");
        System.out.println("Jackson custom serializer:");
        System.out.println("  public class GameSerializer extends JsonSerializer<Game> {");
        System.out.println("      @Override");
        System.out.println("      public void serialize(Game game, JsonGenerator gen, SerializerProvider sp)");
        System.out.println("          throws IOException {");
        System.out.println("          gen.writeStartObject();");
        System.out.println("          gen.writeStringField(\"title\", game.getName());");
        System.out.println("          gen.writeStringField(\"category\", game.getGenre());");
        System.out.println("          gen.writeEndObject();");
        System.out.println("      }");
        System.out.println("  }\n");

        System.out.println("Apply with: @JsonSerialize(using = GameSerializer.class)\n");

        System.out.println("=== End of Serialization Workshop ===");
    }
}
