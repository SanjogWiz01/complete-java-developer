# Password Generator

A small Java CLI project that creates random passwords from selected character groups.

## Run

```bash
javac src/Main.java
java -cp src Main
```

## Features

- Supports lowercase letters, uppercase letters, digits, and symbols.
- Validates password length.
- Uses `SecureRandom`.
- Guarantees at least one selected character group.
