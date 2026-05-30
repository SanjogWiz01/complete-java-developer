# Java Database Connectivity Summary

This folder is a compact learning set for Java database connectivity with JDBC.
JDBC means Java Database Connectivity. It is the standard Java API used to connect
Java programs with relational databases such as MySQL, PostgreSQL, Oracle, SQL
Server, SQLite, and H2.

## What JDBC Does

JDBC lets Java code:

- Open a connection to a database.
- Send SQL commands.
- Read query results.
- Insert, update, and delete data.
- Manage transactions.
- Handle database errors.
- Close database resources correctly.

## Main JDBC Classes

### DriverManager

`DriverManager` finds a JDBC driver and opens a database connection.

```java
Connection connection = DriverManager.getConnection(url, user, password);
```

The database URL decides which driver is used. Examples:

```text
jdbc:mysql://localhost:3306/school
jdbc:postgresql://localhost:5432/school
jdbc:h2:mem:school
jdbc:sqlite:school.db
```

### Connection

`Connection` represents one active connection to the database. It is used to
create statements, control transactions, and close the database session.

Important methods:

```java
connection.prepareStatement(sql);
connection.createStatement();
connection.setAutoCommit(false);
connection.commit();
connection.rollback();
connection.close();
```

### Statement

`Statement` runs plain SQL. It is useful for fixed SQL such as creating a table,
but it should not be used with user input.

```java
Statement statement = connection.createStatement();
statement.executeUpdate("CREATE TABLE students (id INT, name VARCHAR(100))");
```

### PreparedStatement

`PreparedStatement` is the preferred way to run SQL with values. It prevents SQL
injection and handles escaping for you.

```java
PreparedStatement statement =
        connection.prepareStatement("INSERT INTO students (id, name) VALUES (?, ?)");
statement.setInt(1, 1);
statement.setString(2, "Anita");
statement.executeUpdate();
```

### ResultSet

`ResultSet` stores rows returned by a `SELECT` query.

```java
ResultSet resultSet = statement.executeQuery();
while (resultSet.next()) {
    int id = resultSet.getInt("id");
    String name = resultSet.getString("name");
}
```

## Basic JDBC Flow

1. Add the database JDBC driver to the classpath.
2. Prepare the database URL, username, and password.
3. Open a `Connection`.
4. Create a `Statement` or `PreparedStatement`.
5. Execute SQL.
6. Read the `ResultSet` if the SQL is a query.
7. Close all resources.

## Resource Management

Always use try-with-resources for JDBC objects. It closes resources even if an
exception happens.

```java
try (Connection connection = DriverManager.getConnection(url, user, password);
     PreparedStatement statement = connection.prepareStatement(sql);
     ResultSet resultSet = statement.executeQuery()) {

    while (resultSet.next()) {
        System.out.println(resultSet.getString("name"));
    }
}
```

## Best Practices

- Use `PreparedStatement` for SQL with values.
- Never build SQL by directly joining user input into strings.
- Close `Connection`, `Statement`, and `ResultSet`.
- Use transactions when multiple SQL commands must succeed together.
- Store database credentials outside source code in environment variables or
  configuration files.
- Use a connection pool in real applications.
- Keep SQL errors visible while learning, but avoid leaking passwords or private
  connection details in production logs.

## Files In This Folder

- `README.md`: Core JDBC theory.
- `JDBC_THEORY_README.md`: Deeper notes, setup guide, mistakes, and exercises.
- `JdbcConnectionDemo.java`: Opens a connection and prints database metadata.
- `PreparedStatementCrudDemo.java`: Demonstrates create, insert, read, update,
  and delete.
- `TransactionDemo.java`: Demonstrates commit and rollback.
- `StudentDaoDemo.java`: Demonstrates a small DAO pattern for cleaner code.

