# Snake Game (Core Java + JavaFX)

A playable Snake game built with JavaFX demonstrating:
- Real-time keyboard input
- Game loop and animation via `AnimationTimer`
- Collision detection (walls + self)
- Apple spawning and score tracking
- Restart support (`R` key after game over)

## Requirements
- JDK 24
- Maven 3.8+

## Run locally
From this folder:

```bash
cd projects/snake-game
mvn clean javafx:run
```

> **Important:** This project is JavaFX, not Spring Boot.
> Do **not** use `mvn spring-boot:run`.

## Controls
- Arrow keys: Move snake
- R: Restart after game over

## Troubleshooting
### Error: `No plugin found for prefix 'spring-boot'`
You ran the Spring Boot command in a JavaFX project.
Use:

```bash
mvn clean javafx:run
```

### Verify tools
```bash
java -version
mvn -version
```
