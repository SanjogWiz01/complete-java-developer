package com.example.snakegame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class SnakeGameApp extends Application {
    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 20;
    private static final long MOVE_INTERVAL_NS = 120_000_000L;

    private final Deque<Point2D> snake = new ArrayDeque<>();
    private final Random random = new Random();

    private Direction direction = Direction.RIGHT;
    private Direction nextDirection = Direction.RIGHT;
    private Point2D apple;
    private boolean gameOver;
    private int score;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        resetGame();

        Scene scene = new Scene(new StackPane(canvas));
        scene.setOnKeyPressed(event -> handleInput(event.getCode()));

        stage.setTitle("Snake Game - JavaFX");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        scene.requestFocus();

        new AnimationTimer() {
            private long lastMove = 0;

            @Override
            public void handle(long now) {
                if (!gameOver && now - lastMove >= MOVE_INTERVAL_NS) {
                    direction = nextDirection;
                    update();
                    lastMove = now;
                }
                render(gc);
            }
        }.start();
    }

    private void handleInput(KeyCode code) {
        if (gameOver && code == KeyCode.R) {
            resetGame();
            return;
        }

        if (code == KeyCode.UP && direction != Direction.DOWN) {
            nextDirection = Direction.UP;
        } else if (code == KeyCode.DOWN && direction != Direction.UP) {
            nextDirection = Direction.DOWN;
        } else if (code == KeyCode.LEFT && direction != Direction.RIGHT) {
            nextDirection = Direction.LEFT;
        } else if (code == KeyCode.RIGHT && direction != Direction.LEFT) {
            nextDirection = Direction.RIGHT;
        }
    }

    private void update() {
        Point2D head = snake.peekFirst();
        Point2D newHead = head.add(direction.dx, direction.dy);

        if (isWallCollision(newHead) || snake.contains(newHead)) {
            gameOver = true;
            return;
        }

        snake.addFirst(newHead);

        if (newHead.equals(apple)) {
            score += 10;
            spawnApple();
        } else {
            snake.removeLast();
        }
    }

    private boolean isWallCollision(Point2D point) {
        return point.getX() < 0 || point.getY() < 0 || point.getX() >= WIDTH || point.getY() >= HEIGHT;
    }

    private void spawnApple() {
        Point2D candidate;
        do {
            candidate = new Point2D(random.nextInt(WIDTH), random.nextInt(HEIGHT));
        } while (snake.contains(candidate));
        apple = candidate;
    }

    private void render(GraphicsContext gc) {
        gc.setFill(Color.web("#111827"));
        gc.fillRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        gc.setFill(Color.web("#ef4444"));
        drawCell(gc, apple);

        boolean first = true;
        for (Point2D segment : snake) {
            gc.setFill(first ? Color.web("#22c55e") : Color.web("#16a34a"));
            drawCell(gc, segment);
            first = false;
        }

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(18));
        gc.fillText("Score: " + score, 10, 22);

        if (gameOver) {
            gc.setFill(Color.rgb(0, 0, 0, 0.6));
            gc.fillRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(30));
            gc.fillText("Game Over", WIDTH * TILE_SIZE / 2.0 - 85, HEIGHT * TILE_SIZE / 2.0 - 10);
            gc.setFont(Font.font(18));
            gc.fillText("Press R to restart", WIDTH * TILE_SIZE / 2.0 - 75, HEIGHT * TILE_SIZE / 2.0 + 25);
        }
    }

    private void drawCell(GraphicsContext gc, Point2D point) {
        gc.fillRoundRect(point.getX() * TILE_SIZE + 1, point.getY() * TILE_SIZE + 1, TILE_SIZE - 2, TILE_SIZE - 2, 8, 8);
    }

    private void resetGame() {
        snake.clear();
        snake.addFirst(new Point2D(5, 5));
        snake.addLast(new Point2D(4, 5));
        snake.addLast(new Point2D(3, 5));
        direction = Direction.RIGHT;
        nextDirection = Direction.RIGHT;
        score = 0;
        gameOver = false;
        spawnApple();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        final int dx;
        final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }
}
