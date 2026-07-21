# 13 - Testing Gauntlet: Testing APIs

## Game Name: Testing Gauntlet

## What You Learn
- Testing pyramid concept
- Unit testing with JUnit 5 + Mockito
- Integration testing with MockMvc
- REST Assured for API testing
- Testing best practices

## Key Concepts

### Testing Pyramid
```
        /\
       /  \      E2E Tests (few, slow)
      /    \
     /------\    Integration Tests (moderate)
    /        \
   /----------\  Unit Tests (many, fast)
```

### 1. Unit Testing with JUnit 5 + Mockito
```java
@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository repository;

    @InjectMocks
    private GameService gameService;

    @Test
    void shouldReturnGameWhenIdExists() {
        Game game = new Game(1L, "RPG Quest", "RPG");
        when(repository.findById(1L)).thenReturn(Optional.of(game));

        Game result = gameService.findById(1L);

        assertNotNull(result);
        assertEquals("RPG Quest", result.getName());
        verify(repository, times(1)).findById(1L);
    }
}
```

### 2. MockMvc Integration Testing
```java
@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnGameById() throws Exception {
        mockMvc.perform(get("/api/games/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("RPG Quest"));
    }
}
```

### 3. REST Assured
```java
given()
    .baseUri("http://localhost:8080")
    .header("Accept", "application/json")
.when()
    .get("/api/games/1")
.then()
    .statusCode(200)
    .body("name", equalTo("RPG Quest"));
```

### Best Practices
1. Follow AAA: Arrange, Act, Assert
2. One thing per test method
3. Meaningful test names
4. Mock external dependencies
5. Test happy path AND error cases
6. Use @DataJpaTest for repository tests
7. Use @WebMvcTest for controller tests

## Run This File
```bash
javac 13_TestingGauntlet.java
java TestingGauntlet
```

## Next Topic
[14 - Pagination Forge](14_PaginationForge.md) - Performance & Pagination
