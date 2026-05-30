# JDBC Theory, Setup, and Practice Guide

This file explains how the example Java files work and what to learn from them.

## JDBC Architecture

JDBC has four common layers:

1. Java application code.
2. JDBC API from the JDK, mainly the `java.sql` package.
3. Database-specific JDBC driver.
4. The database server or database file.

Your Java code calls standard JDBC interfaces. The driver translates those calls
into the database's own protocol.

## Driver Requirement

The JDK includes the JDBC API, but it does not include every database driver.
To run these examples against a real database, add the correct driver jar.

Examples:

```text
MySQL:       mysql-connector-j
PostgreSQL: postgresql
SQLite:     sqlite-jdbc
H2:         h2
Oracle:     ojdbc
```

The Java files use only standard `java.sql` APIs, so they compile with the JDK.
Running them requires a matching driver on the classpath.

## Environment Variables Used By Examples

The examples read these values:

```text
DB_URL
DB_USER
DB_PASSWORD
```

If they are not set, the code uses H2-style defaults:

```text
DB_URL=jdbc:h2:mem:jdbc_summary;DB_CLOSE_DELAY=-1
DB_USER=sa
DB_PASSWORD=
```

You can change those defaults in the source code or set environment variables.

PowerShell example:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/school"
$env:DB_USER = "root"
$env:DB_PASSWORD = "your_password"
```

## Compile and Run

From inside this folder:

```powershell
javac *.java
```

Run with a JDBC driver on the classpath. Example with H2:

```powershell
java -cp ".;C:\path\to\h2.jar" JdbcConnectionDemo
java -cp ".;C:\path\to\h2.jar" PreparedStatementCrudDemo
java -cp ".;C:\path\to\h2.jar" TransactionDemo
java -cp ".;C:\path\to\h2.jar" StudentDaoDemo
```

On macOS or Linux, use `:` instead of `;` in the classpath.

## SQL Operation Types

### DDL

DDL means Data Definition Language. It changes database structure.

Examples:

```sql
CREATE TABLE students (...);
ALTER TABLE students ADD COLUMN email VARCHAR(100);
DROP TABLE students;
```

In JDBC, DDL is usually run with `execute` or `executeUpdate`.

### DML

DML means Data Manipulation Language. It changes data.

Examples:

```sql
INSERT INTO students (...);
UPDATE students SET ...;
DELETE FROM students WHERE ...;
```

In JDBC, DML is usually run with `executeUpdate`, which returns the number of
affected rows.

### Query

Query operations read data.

```sql
SELECT id, name, email FROM students;
```

In JDBC, queries are usually run with `executeQuery`, which returns a
`ResultSet`.

## Transactions

A transaction is a group of SQL statements that should succeed or fail as one
unit.

Example: transferring money between accounts.

1. Subtract money from account A.
2. Add money to account B.
3. Commit only if both commands work.
4. Roll back if any command fails.

In JDBC:

```java
connection.setAutoCommit(false);
try {
    // SQL command 1
    // SQL command 2
    connection.commit();
} catch (SQLException ex) {
    connection.rollback();
}
```

## Common Mistakes

- Forgetting to add the database driver jar.
- Using the wrong JDBC URL.
- Forgetting to start the database server.
- Using `Statement` with user input.
- Not closing `ResultSet`, `Statement`, or `Connection`.
- Forgetting to call `rollback` after a failed transaction.
- Assuming all databases support exactly the same SQL syntax.

## Learning Path

1. Run `JdbcConnectionDemo.java`.
2. Study `PreparedStatementCrudDemo.java`.
3. Modify the CRUD example to add a new column.
4. Run `TransactionDemo.java` and intentionally break one SQL statement.
5. Study `StudentDaoDemo.java` to see how database logic can be separated from
   application logic.

## Practice Exercises

- Add a `phone` column to the `students` table.
- Write a method that finds a student by email.
- Write a method that deletes all students with no email address.
- Add input validation before inserting records.
- Create a transaction that inserts a student and an audit log row together.

