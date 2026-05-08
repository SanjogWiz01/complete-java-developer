# Java Classes in Detail

## 1. What Is a Class?
A **class** is a blueprint for creating objects. It defines:
- fields (data)
- methods (operations)
- constructors (initialization)
- access rules and contracts

```java
public class Student {
    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void study() {
        System.out.println(name + " is studying.");
    }
}
```

## 2. Class Members
### Instance Members
Belong to each object instance:
- instance fields
- instance methods

### Static Members
Belong to class itself:
- static fields
- static methods
- static blocks

```java
class Counter {
    static int totalCount = 0;
    int localCount = 0;
}
```

## 3. Access Modifiers
- `public`: accessible everywhere
- `protected`: same package + subclasses
- (default/package-private): same package only
- `private`: within same class only

Use the most restrictive access possible.

## 4. Constructors
Constructors initialize new objects.

Types:
- default constructor (compiler-provided if none written)
- no-arg constructor
- parameterized constructor
- constructor overloading

Rules:
- constructor name must match class name
- constructors have no return type

## 5. `this` and `super`
- `this` refers to current object.
- `super` refers to parent class members.

Used for disambiguation, constructor chaining, and inheritance behavior.

## 6. Nested and Inner Classes
Java supports:
- static nested classes
- non-static inner classes
- local classes
- anonymous classes

Useful for logically grouping helper functionality.

## 7. Abstract Classes
An abstract class cannot be instantiated directly and may include abstract methods.

```java
abstract class Animal {
    abstract void sound();
}
```

Child classes must implement abstract methods (unless also abstract).

## 8. Final Classes and Methods
- `final class`: cannot be extended.
- `final method`: cannot be overridden.
- `final field`: assigned once.

Good for immutability and API stability.

## 9. Class Loading and Initialization
Java loads classes when first used.
Initialization order (simplified):
1. static fields and static blocks
2. instance fields and instance initializers
3. constructor body

Understanding this helps avoid subtle bugs.

## 10. SOLID-Oriented Class Design Tips
- Single Responsibility: one class, one clear purpose.
- Open/Closed: open to extension, closed to modification.
- Favor composition over inheritance.
- Keep classes cohesive and focused.
