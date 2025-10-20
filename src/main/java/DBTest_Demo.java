import java.sql.*;

public class DBTest_Demo {

    public static void main(String[] args) {
        System.out.println("Example for MYSQL DB connection via Java (Level 1: Localhost, No SSL)");

        String HOST = "127.0.0.1";   // Level 1 → local MySQL on same EC2
        String DB   = "SE";
        String USER = "SE";
        String PASS = "SE2020";

        String URL = "jdbc:mysql://" + HOST + ":3306/" + DB
                   + "?useSSL=false&allowPublicKeyRetrieval=true"
                   + "&serverTimezone=UTC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt  = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT NOW();");
            if (rs.next()) {
                System.out.println("0 hour(s) ahead of MySQL on " + HOST + " is: " + rs.getString(1));
                System.out.println("MYSQL Connection Successful (Localhost Level 1)");
            }

        } catch (Exception e) {
            System.out.println("MySQL connection/query failed: " + e.getMessage());
            System.out.println("mysql DB connection fail");
        }
    }
}
