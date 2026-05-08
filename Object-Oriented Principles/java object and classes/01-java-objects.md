# Java Objects in Detail

## 1. What Is an Object?
In Java, an **object** is an instance of a class. It is a runtime entity that combines:
- **State** (data/fields)
- **Behavior** (methods)
- **Identity** (a unique reference in memory)

Example idea:
- Class: `Car`
- Object: `myCar` created from `Car`

## 2. Object Creation Lifecycle
Objects are commonly created using `new`:

```java
Car myCar = new Car();
```

Steps happening conceptually:
1. Memory is allocated on the heap.
2. Fields are initialized (default or explicit values).
3. Constructor runs.
4. Reference is returned and stored in a variable.

## 3. Object State and Behavior
```java
class Car {
    String model;   // state
    int speed;      // state

    void accelerate(int amount) { // behavior
        speed += amount;
    }
}
```

- `model` and `speed` represent current state.
- `accelerate()` changes object state.

## 4. References vs Objects
In Java, variables of class type store **references**, not the raw object.

```java
Car a = new Car();
Car b = a;
b.speed = 60;
System.out.println(a.speed); // 60
```

Both `a` and `b` point to the same object.

## 5. Equality: `==` vs `.equals()`
- `==` compares references (same memory target)
- `.equals()` compares logical content (if overridden)

```java
String s1 = new String("Java");
String s2 = new String("Java");
System.out.println(s1 == s2);      // false
System.out.println(s1.equals(s2)); // true
```

## 6. Object Identity Methods from `Object`
Every class inherits from `java.lang.Object` and gets methods like:
- `toString()`
- `equals(Object o)`
- `hashCode()`
- `getClass()`

These are fundamental for logging, collections, comparisons, and reflection.

## 7. Garbage Collection (GC)
Java automatically reclaims memory for objects that are no longer reachable.

Important notes:
- You cannot manually free objects like in C/C++.
- GC timing is non-deterministic.
- Avoid relying on finalization behavior.

## 8. Mutable vs Immutable Objects
- **Mutable**: state can change after creation (`ArrayList`, custom setters)
- **Immutable**: state cannot change (`String`, many value objects)

Why immutability matters:
- safer sharing across threads
- easier reasoning
- fewer side effects

## 9. Object Passing in Methods
Java passes arguments by value, including references by value.

```java
void rename(Car c) {
    c.model = "Updated"; // affects original object
}
```

Reassigning `c` inside method does not change caller's reference.

## 10. Best Practices for Object Design
- Keep fields private.
- Expose behavior through methods.
- Validate constructor input.
- Prefer immutability where practical.
- Override `toString`, `equals`, and `hashCode` consistently when needed.
