CREATE DATABASE IF NOT EXISTS jyoti_bank;
USE jyoti_bank;

CREATE TABLE IF NOT EXISTS customers (
  customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  date_of_birth DATE,
  national_id VARCHAR(50) UNIQUE,
  email VARCHAR(150) UNIQUE,
  phone VARCHAR(20),
  address_line1 VARCHAR(255),
  address_line2 VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(100),
  postal_code VARCHAR(20),
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('ADMIN','CUSTOMER') NOT NULL,
  customer_id BIGINT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_login TIMESTAMP NULL,
  CONSTRAINT fk_users_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE IF NOT EXISTS accounts (
  account_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  account_number VARCHAR(20) NOT NULL UNIQUE,
  customer_id BIGINT NOT NULL,
  account_type ENUM('SAVINGS','CURRENT','FIXED_DEPOSIT') NOT NULL,
  balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  status ENUM('ACTIVE','BLOCKED','CLOSED','DORMANT') NOT NULL DEFAULT 'ACTIVE',
  interest_rate DECIMAL(5,4) DEFAULT 0.0000,
  minimum_balance DECIMAL(15,2) DEFAULT 0.00,
  opened_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_accounts_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE IF NOT EXISTS transactions (
  transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reference_number VARCHAR(50) NOT NULL,
  account_id BIGINT NOT NULL,
  transaction_type ENUM('DEPOSIT','WITHDRAWAL','TRANSFER','INTEREST','FEE') NOT NULL,
  amount DECIMAL(15,2) NOT NULL,
  balance_before DECIMAL(15,2) NOT NULL,
  balance_after DECIMAL(15,2) NOT NULL,
  description VARCHAR(255),
  status ENUM('SUCCESS','FAILED','PENDING') NOT NULL DEFAULT 'SUCCESS',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_tx_account_created (account_id, created_at),
  INDEX idx_tx_ref (reference_number),
  CONSTRAINT fk_transactions_account FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

CREATE TABLE IF NOT EXISTS fixed_deposits (
  fd_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  linked_account_id BIGINT NOT NULL,
  fd_account_id BIGINT NOT NULL,
  principal_amount DECIMAL(15,2) NOT NULL,
  interest_rate DECIMAL(5,4) NOT NULL,
  tenure_months INT NOT NULL,
  maturity_amount DECIMAL(15,2) NOT NULL,
  start_date DATE NOT NULL,
  maturity_date DATE NOT NULL,
  status ENUM('ACTIVE','MATURED','CLOSED','BROKEN') NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_fd_linked_account FOREIGN KEY (linked_account_id) REFERENCES accounts(account_id),
  CONSTRAINT fk_fd_account FOREIGN KEY (fd_account_id) REFERENCES accounts(account_id)
);
