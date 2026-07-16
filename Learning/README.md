# Java JDBC Learning Guide

## What is JDBC?

JDBC (Java Database Connectivity) is a Java API that allows Java applications to interact with databases. It provides a standard interface for connecting to relational databases, executing SQL queries, and processing results.

## Key Components

- **DriverManager**: Manages database drivers and establishes connections
- **Connection**: Represents a session with a database
- **Statement**: Used to execute SQL queries
- **PreparedStatement**: Pre-compiled SQL statement for better performance
- **ResultSet**: Holds data retrieved from the database

## JDBC Architecture

```
Java Application
       |
   JDBC API
       |
   JDBC Driver Manager
       |
   Database Driver (MySQL, PostgreSQL, Oracle, etc.)
       |
   Database
```

## Steps to Connect to a Database

1. Load the JDBC driver
2. Establish a connection
3. Create a statement
4. Execute a query
5. Process the results
6. Close the connection

## Common JDBC Operations

- **CRUD**: Create, Read, Update, Delete
- **Batch Processing**: Execute multiple statements at once
- **Transaction Management**: Commit or rollback operations
- **Connection Pooling**: Reuse database connections

## Resources

- [Oracle JDBC Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/package-summary.html)
- [MySQL JDBC Connector](https://dev.mysql.com/doc/connector-j/)
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)
