-- Second-Order SQL Injection
-- A two-stage attack. Input is safely stored on first write, then a vulnerable
-- read/update step later concatenates that stored data into a new SQL statement.

-- Stage 1: "safe" insert of malicious input that is NOT executed now.
-- The value 'bob''; DROP TABLE orders;--' is stored literally.
INSERT INTO users (username) VALUES ('bob'';

-- Stage 2: later, the stored value is concatenated into a second vulnerable query.
-- This query now executes the injected DROP because the stored text becomes SQL.
SELECT * FROM orders WHERE customer = 'bob''; DROP TABLE orders;--';

-- Anonymous offender: updating a row built from attacker-controlled stored text.
UPDATE users SET email = '<attacker value>' WHERE id = 1;

-- Prevention: treat stored data as untrusted for SQL purposes and always
-- parameterize every statement that re-reads it.
SELECT * FROM users WHERE id = ?;