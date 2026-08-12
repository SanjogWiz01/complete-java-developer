# 7.6 Design Patterns: Singleton, Factory and Abstract Factory

Design patterns are reusable approaches to recurring software-design problems.

## 1. Singleton
Ensures one logical instance and provides a shared access point.

### Real uses
- application configuration
- shared registry
- carefully designed resource manager

### Warning
Singletons introduce global state and can complicate testing. Dependency injection is often preferable in modern applications.

## 2. Factory
Moves object creation behind a method/class so client code depends on an abstraction rather than concrete construction details.

### Real use
A payment system can select `EsewaPayment`, `KhaltiPayment`, `CardPayment`, etc. without exposing construction logic to the caller.

## 3. Abstract Factory
Creates families of related objects without requiring the client to know concrete classes.

### Real use
A cross-platform UI library can create Windows-style or Linux-style button/text-field families.

## Pattern comparison
| Pattern | Main purpose |
|---|---|
| Singleton | Control one logical instance |
| Factory | Create one product through an abstraction |
| Abstract Factory | Create related product families |

The accompanying Java files are runnable demonstrations with realistic business examples.
