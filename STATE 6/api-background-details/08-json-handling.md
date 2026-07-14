# JSON Handling in Java

JSON (JavaScript Object Notation) is the standard format for API data exchange.

## JSON Structure

```json
{
    "name": "Sanjog",
    "age": 21,
    "skills": ["Java", "Python", "SQL"],
    "address": {
        "city": "Kathmandu",
        "country": "Nepal"
    }
}
```

## Using Gson Library

### Java Object to JSON

```java
import com.google.gson.Gson;

Gson gson = new Gson();
User user = new User("Sanjog", 21);
String json = gson.toJson(user);
System.out.println(json);
// Output: {"name":"Sanjog","age":21}
```

### JSON to Java Object

```java
String json = "{\"name\":\"Sanjog\",\"age\":21}";
User user = gson.fromJson(json, User.class);
System.out.println(user.getName());
```

## Using Jackson Library

### Java Object to JSON

```java
import com.fasterxml.jackson.databind.ObjectMapper;

ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(user);
```

### JSON to Java Object

```java
User user = mapper.readValue(json, User.class);
```

## Manual JSON Parsing (No Libraries)

```java
String json = "{\"name\":\"Sanjog\",\"age\":21}";
// Use org.json or manual string parsing
JSONObject obj = new JSONObject(json);
String name = obj.getString("name");
int age = obj.getInt("age");
```

## Choosing a Library

| Library | Speed | Size | Ease of Use |
|---------|-------|------|-------------|
| Gson | Fast | Small | Easy |
| Jackson | Very Fast | Large | Moderate |
| org.json | Moderate | Small | Easy |
