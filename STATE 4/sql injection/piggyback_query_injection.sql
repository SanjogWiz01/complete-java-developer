-- Piggybacked Query Injection (Piggybacking)
-- Attacker appends an additional, independent SQL statement to the end of a
-- legitimate query without using UNION. The extra statement "piggybacks" on the
-- original, often for data modification rather than extraction.

-- Original valid query
SELECT * FROM products WHERE category = 'electronics';

-- Attacker adds "; UPDATE users SET balance = 0" to drain balances
SELECT * FROM products WHERE category = 'electronics'; UPDATE users SET balance = 0;

-- Appending a data-exfiltration read
SELECT * FROM products WHERE category = 'electronics'; SELECT * FROM admins;

-- Appending a destructive statement
SELECT * FROM products WHERE category = 'electronics'; TRUNCATE TABLE orders;

-- Difference from UNION: statements are executed sequentially, not combined in a set.
-- Prevention: forbid multiple statements per execution (no multi-queries) and use
-- prepared statements with strict input validation.
SELECT * FROM products WHERE category = ?;