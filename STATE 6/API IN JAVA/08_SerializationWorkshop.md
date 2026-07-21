# 08 - Serialization Workshop: Jackson & Gson

## Game Name: Serialization Workshop

## What You Learn
- Jackson basics (Spring Boot default)
- Jackson annotations for customization
- Gson library basics
- Custom serialization

## Key Concepts

### 1. Jackson Basics
Jackson is the default JSON library in Spring Boot.

**Serialization (Object -> JSON):**
```java
ObjectMapper mapper = new ObjectMapper();
Game game = new Game(1, "Dragon Quest", "RPG");
String json = mapper.writeValueAsString(game);
// {"id":1,"name":"Dragon Quest","genre":"RPG"}
```

**Deserialization (JSON -> Object):**
```java
String json = "{\"id\":1,\"name\":\"Dragon Quest\",\"genre\":\"RPG\"}";
Game game = mapper.readValue(json, Game.class);
```

### 2. Jackson Annotations
| Annotation | Purpose |
|-----------|---------|
| @JsonProperty("name") | Rename field in JSON |
| @JsonIgnore | Exclude field from output |
| @JsonFormat(pattern="yyyy-MM-dd") | Format date fields |
| @JsonInclude(NON_NULL) | Omit null values |

### 3. Gson Alternative
```java
Gson gson = new Gson();
String json = gson.toJson(game);      // Serialize
Game game = gson.fromJson(json, Game.class); // Deserialize
```

| Feature | Jackson | Gson |
|---------|---------|------|
| Default in Spring | Yes | No |
| Annotations | Rich | Limited |
| Complexity | More features | Simpler |

### 4. Custom Serializer
```java
public class GameSerializer extends JsonSerializer<Game> {
    @Override
    public void serialize(Game game, JsonGenerator gen, SerializerProvider sp)
        throws IOException {
        gen.writeStartObject();
        gen.writeStringField("title", game.getName());
        gen.writeEndObject();
    }
}
```

## Run This File
```bash
javac 08_SerializationWorkshop.java
java SerializationWorkshop
```

## Next Topic
[09 - Error Handling Fortress](09_ErrorHandlingFortress.md) - API Error Handling
