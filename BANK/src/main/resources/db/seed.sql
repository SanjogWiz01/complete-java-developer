USE jyoti_bank;

INSERT INTO customers (
  first_name, last_name, date_of_birth, national_id, email, phone, address_line1, city, state, postal_code
) VALUES
('Jyoti', 'Sharma', '1994-03-12', 'NID-10001', 'jyoti.sharma@example.com', '+977-9800000001', 'Putalisadak', 'Kathmandu', 'Bagmati', '44600'),
('Ravi', 'Thapa', '1990-08-25', 'NID-10002', 'ravi.thapa@example.com', '+977-9800000002', 'Lakeside', 'Pokhara', 'Gandaki', '33700');

INSERT INTO users (username, password_hash, role, customer_id, is_active) VALUES
('admin', '$2a$12$yN546rEl2b5ZzXajsJSE9.2bzg/zWRnP4LggyWkJa48RgMkd2X5re', 'ADMIN', NULL, TRUE),
('jyoti', '$2a$12$yN546rEl2b5ZzXajsJSE9.2bzg/zWRnP4LggyWkJa48RgMkd2X5re', 'CUSTOMER', 1, TRUE),
('ravi', '$2a$12$yN546rEl2b5ZzXajsJSE9.2bzg/zWRnP4LggyWkJa48RgMkd2X5re', 'CUSTOMER', 2, TRUE);

INSERT INTO accounts (account_number, customer_id, account_type, balance, status, interest_rate, minimum_balance) VALUES
('SAV20260821123401', 1, 'SAVINGS', 25000.00, 'ACTIVE', 0.0450, 500.00),
('CUR20260821123402', 2, 'CURRENT', 80000.00, 'ACTIVE', 0.0200, 1000.00);
