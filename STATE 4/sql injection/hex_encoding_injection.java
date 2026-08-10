// Hex Encoding SQL Injection via JDBC
// Attacker encodes payload as hex to bypass filters that block literal keywords.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class hex_encoding_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: filter blocks the word 'admin' but hex bypasses it
        String hex = "0x61646d696e"; // HEX('admin') = 61646d696e
        String query = "SELECT * FROM users WHERE username = " + hex;

        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("Hex bypass: " + rs.getString("username"));
        }

        // Hex-encoded string inside a string literal context
        String inner = "username = 0x6a6f686e"; // HEX('john')
        String mixed = "SELECT * FROM users WHERE " + inner;
        ResultSet mrs = st.executeQuery(mixed);
        while (mrs.next()) {
            System.out.println("Hex value matched: " + mrs.getString("username"));
        }

        rs.close();
        mrs.close();
        st.close();
        con.close();
    }
}
