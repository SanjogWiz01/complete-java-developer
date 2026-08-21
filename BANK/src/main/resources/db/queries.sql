USE jyoti_bank;

-- Customer profile with account count
SELECT c.customer_id, c.first_name, c.last_name, c.email, COUNT(a.account_id) AS account_count
FROM customers c
LEFT JOIN accounts a ON a.customer_id = c.customer_id
WHERE c.customer_id = 1
GROUP BY c.customer_id, c.first_name, c.last_name, c.email;

-- Account statement (latest 20)
SELECT t.transaction_id, t.reference_number, t.transaction_type, t.amount, t.balance_after, t.status, t.created_at
FROM transactions t
JOIN accounts a ON a.account_id = t.account_id
WHERE a.account_number = 'SAV20260821123401'
ORDER BY t.created_at DESC
LIMIT 20;

-- Total bank deposit liabilities
SELECT SUM(balance) AS total_customer_balance
FROM accounts
WHERE status = 'ACTIVE';

-- Dormancy candidate report (no activity in 365 days)
SELECT a.account_id, a.account_number, MAX(t.created_at) AS last_tx_time
FROM accounts a
LEFT JOIN transactions t ON t.account_id = a.account_id
GROUP BY a.account_id, a.account_number
HAVING last_tx_time IS NULL OR last_tx_time < NOW() - INTERVAL 365 DAY;
