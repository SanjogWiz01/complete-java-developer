# Java Objects vs Classes + Detailed Examples

## 1. Quick Difference
- **Class**: blueprint/template
- **Object**: real instance created from blueprint

Example:
- Class = House design
- Object = Actual built house

## 2. Side-by-Side Example
```java
class Book {
    String title;
    String author;

    Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    void display() {
        System.out.println(title + " by " + author);
    }
}

public class Main {
    public static void main(String[] args) {
        Book b1 = new Book("Effective Java", "Joshua Bloch");
        Book b2 = new Book("Clean Code", "Robert C. Martin");

        b1.display();
        b2.display();
    }
}
```

- `Book` is class definition.
- `b1` and `b2` are separate objects with independent state.

## 3. Memory View (Conceptual)
- Stack: references (`b1`, `b2`)
- Heap: object data (`title`, `author` values)

Each object occupies separate memory on heap.

## 4. Multiple Objects, Same Class
A single class can produce many objects:
- same behavior
- different state

This is the core of object-oriented modeling.

## 5. Real-World Modeling Example
`BankAccount` class can create thousands of account objects:
- common operations: deposit/withdraw/check balance
- unique state per account: account number, owner, balance

## 6. Encapsulation Example
```java
class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        if (initialBalance < 0) throw new IllegalArgumentException("Negative balance");
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid deposit");
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}
```

- Data is hidden (`private balance`).
- Controlled access through methods.

## 7. Inheritance and Polymorphism Link
Classes enable inheritance hierarchies, and objects enable runtime polymorphism.

```java
class Shape {
    double area() { return 0; }
}

class Circle extends Shape {
    double radius;
    Circle(double radius) { this.radius = radius; }
    @Override
    double area() { return Math.PI * radius * radius; }
}
```

```java
Shape s = new Circle(2.0);
System.out.println(s.area());
```

Reference type is `Shape`, actual object type is `Circle`.

## 8. Common Mistakes
- Confusing class definition with object instance.
- Using public fields everywhere (breaks encapsulation).
- Ignoring `equals/hashCode` when objects are used in collections.
- Overusing inheritance instead of composition.

## 9. Interview-Style Summary
If asked: "What is the difference between class and object in Java?"

A strong answer:
- A class is a logical template containing fields and methods.
- An object is a physical runtime instance of that class, with actual memory and data.
- Many objects can be created from one class.
