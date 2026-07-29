import java.sql.*;
public class jsbc_main{
    public static void main(String[] args){
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database_name","your_username","your_password");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM your_table_name");
            while(rs.next()){
                System.out.println(rs.getInt(1)+" "+rs.getString(2));
            }
            con.close();
        }catch(Exception e){ 
            print.stackTrace(e);
        }
    }