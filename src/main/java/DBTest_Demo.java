import java.sql.*;

public class DBTest_Demo {
    // Level 1 — Localhost (Server EC2, No SSL)
    private static final String HOST = "127.0.0.1";
    private static final String DB   = "SE";
    private static final String USER = "SE";
    private static final String PASS = "SE2020";

    private static final String JDBC_URL =
        "jdbc:mysql://" + HOST + ":3306/" + DB
        + "?useUnicode=true&characterEncoding=utf8"
        + "&serverTimezone=UTC"
        + "&sslMode=DISABLED&useSSL=false"
        + "&allowPublicKeyRetrieval=true"
        + "&connectTimeout=5000&socketTimeout=5000";

    public static void main(String[] args) {
        System.out.println("Example for MYSQL DB connection via Java (Level 1: Localhost, No SSL)");
        int offset = 0;
        if (args.length == 1) {
            try { offset = Integer.parseInt(args[0]); } catch (Exception ignore) {}
        }
        String sql = "SELECT DATE_ADD(NOW(), INTERVAL ? HOUR)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println(offset + " hour(s) ahead of MySQL on " + HOST + " is: " + rs.getString(1));
                }
            }
            System.out.println("MYSQL Connection Successful (Localhost Level 1)");
        } catch (Exception e) {
            System.err.println("MySQL connection/query failed: " + e.getMessage());
            System.out.println("mysql DB connection fail");
        }
    }
}

