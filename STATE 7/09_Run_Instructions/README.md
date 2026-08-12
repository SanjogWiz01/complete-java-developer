# How to Run

## Basic Java files
From the folder containing a `.java` file:

```bash
javac FileName.java
java ClassName
```

For package-based examples, compile from the project root:

```bash
javac -d out path/to/File.java
java -cp out package.ClassName
```

## Hibernate
Requirements:
- JDK 17+
- Maven

Run from `02_Hibernate`:

```bash
mvn compile
mvn exec:java -Dexec.mainClass=unit7.hibernate.HibernateInventoryApp
```

If the `exec` plugin is not configured, run the class from your IDE after Maven downloads dependencies.

## Spring Boot
Requirements:
- JDK 17+
- Maven

Run from `04_Spring_Boot_Basics`:

```bash
mvn spring-boot:run
```

Test:
```text
GET  http://localhost:8080/api/products
POST http://localhost:8080/api/products
DELETE http://localhost:8080/api/products/1
```

POST JSON:
```json
{"name":"Monitor","price":15000}
```

## Recommended IDE
IntelliJ IDEA, Eclipse or VS Code with Java and Maven support.
