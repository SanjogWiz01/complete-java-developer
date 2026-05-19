# Java Unit 1 Notes

## 1.1 Java Architecture, Classpath, and Sample Program

### Java Architecture

Java follows the principle "write once, run anywhere".

Main parts:

- **JDK (Java Development Kit)**: Tools for developers. It includes `javac`, `java`, libraries, and the JRE.
- **JRE (Java Runtime Environment)**: Environment required to run Java programs.
- **JVM (Java Virtual Machine)**: Executes Java bytecode.
- **Bytecode**: Platform-independent code generated after compiling `.java` files.

Execution flow:

```text
Java source code (.java)
        |
        v
Compiler: javac
        |
        v
Bytecode (.class)
        |
        v
JVM runs bytecode
```

### Classpath

The classpath tells Java where to find compiled classes and external libraries.

Compile:

```bash
javac CoreJavaDemo.java
```

Run:

```bash
java CoreJavaDemo
```

Run with classpath:

```bash
java -cp . CoreJavaDemo
```

Use `;` to separate classpath entries on Windows:

```bash
java -cp ".;lib/mysql.jar" CoreJavaDemo
```

Use `:` on Linux/macOS:

```bash
java -cp ".:lib/mysql.jar" CoreJavaDemo
```

### Sample Program Structure

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello Java");
    }
}
```

- `public class HelloWorld`: Class declaration.
- `main`: Program entry point.
- `System.out.println`: Prints output.

## 1.2 Classes, Objects, and Constructors

### Class

A class is a blueprint for creating objects.

```java
class Student {
    String name;
    int age;
}
```

### Object

An object is an instance of a class.

```java
Student s1 = new Student();
```

### Constructor

A constructor initializes an object.

```java
class Student {
    String name;

    Student(String name) {
        this.name = name;
    }
}
```

Constructor rules:

- Constructor name must match the class name.
- It has no return type.
- It runs automatically when an object is created.

## 1.3 Packages and Data Types

### Packages

A package groups related classes and avoids name conflicts.

Example:

```java
package school;

public class Student {
}
```

Compile from the source root:

```bash
javac school/Student.java
```

Run a class inside a package:

```bash
java school.Student
```

Import a package:

```java
import java.util.ArrayList;
```

### Data Types

Java has two main data type groups.

Primitive data types:

| Type | Example | Use |
| --- | --- | --- |
| `byte` | `byte b = 10;` | Small integer |
| `short` | `short s = 100;` | Small integer |
| `int` | `int age = 20;` | Integer |
| `long` | `long distance = 100000L;` | Large integer |
| `float` | `float price = 10.5f;` | Decimal |
| `double` | `double total = 99.99;` | Decimal |
| `char` | `char grade = 'A';` | Single character |
| `boolean` | `boolean active = true;` | True or false |

Non-primitive data types:

- `String`
- Arrays
- Classes
- Interfaces
- Collections

## 1.4 Conditional Statements

Conditional statements control program flow.

### if statement

```java
if (age >= 18) {
    System.out.println("Adult");
}
```

### if-else statement

```java
if (marks >= 40) {
    System.out.println("Pass");
} else {
    System.out.println("Fail");
}
```

### else-if ladder

```java
if (marks >= 80) {
    System.out.println("A");
} else if (marks >= 60) {
    System.out.println("B");
} else {
    System.out.println("C");
}
```

### switch statement

```java
switch (day) {
    case 1:
        System.out.println("Sunday");
        break;
    case 2:
        System.out.println("Monday");
        break;
    default:
        System.out.println("Invalid day");
}
```

## 1.5 Access Modifiers

Access modifiers control visibility.

| Modifier | Same Class | Same Package | Subclass | Other Package |
| --- | --- | --- | --- | --- |
| `private` | Yes | No | No | No |
| default | Yes | Yes | No | No |
| `protected` | Yes | Yes | Yes | No |
| `public` | Yes | Yes | Yes | Yes |

Example:

```java
class Account {
    private double balance;

    public double getBalance() {
        return balance;
    }
}
```

## 1.6 Exception Handling

Exceptions are runtime problems that interrupt normal program flow.

Common keywords:

- `try`: Code that may cause an exception.
- `catch`: Handles the exception.
- `finally`: Runs whether an exception occurs or not.
- `throw`: Manually throws an exception.
- `throws`: Declares that a method may throw an exception.

Example:

```java
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Cannot divide by zero");
} finally {
    System.out.println("Finished");
}
```

## 1.7 Java Collections

Collections store and manage groups of objects.

Common collection types:

| Collection | Description |
| --- | --- |
| `ArrayList` | Ordered list, allows duplicates |
| `LinkedList` | Ordered list, efficient insert/delete |
| `HashSet` | Unique values, no guaranteed order |
| `HashMap` | Key-value pairs |
| `Queue` | First-in-first-out processing |
| `Stack` | Last-in-first-out processing |

Example:

```java
import java.util.ArrayList;

ArrayList<String> names = new ArrayList<>();
names.add("Asha");
names.add("Ram");
System.out.println(names);
```

