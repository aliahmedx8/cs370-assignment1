import java.sql.*;

public class DBTest_Demo {

    // ===== Level 4 (WAN + SSL/TLS) =====
    private static final String HOST = "72.89.99.62";   // iqbal's PUBLIC IP
    private static final String DB   = "SE";
    private static final String USER = "SE";
    private static final String PASS = "SE2020";

    // SSL/TLS enforced on server; client requires SSL too
    private static final String JDBC_URL =
        "jdbc:mysql://" + HOST + ":3306/" + DB
      + "?useUnicode=true&characterEncoding=utf8"
      + "&serverTimezone=UTC"
      + "&useSSL=true&requireSSL=true&verifyServerCertificate=false";

    public int testconnection_mysql(int hrOffset) {
        String sql = "SELECT DATE_ADD(NOW(), INTERVAL ? HOUR)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASS)) {

            // main demo query
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, hrOffset);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String ts = rs.getString(1);
                        System.out.println(hrOffset + " hour(s) ahead of MySQL on " + HOST + " is: " + ts);
                    }
                }
            }

            // show TLS details for proof
            try (Statement st = conn.createStatement()) {
                try (ResultSet v = st.executeQuery("SHOW STATUS LIKE 'Ssl_version'")) {
                    if (v.next()) System.out.println("TLS version in use: " + v.getString(2));
                }
                try (ResultSet c = st.executeQuery("SHOW STATUS LIKE 'Ssl_cipher'")) {
                    if (c.next()) System.out.println("TLS cipher in use: " + c.getString(2));
                }
            }

            return 0;

        } catch (Exception e) {
            System.err.println("MySQL connection/query failed: " + e.getMessage());
            return 1;
        }
    }

    public static void main(String[] args) {
        System.out.println("Example for MYSQL DB connection via Java (SSL/TLS)");
        System.out.println("Copyright: Bon Sy");
        System.out.println("Free to use this at your own risk!");

        int offset = 0;
        if (args.length == 1) {
            try { offset = Integer.parseInt(args[0]); } catch (Exception ignored) {}
        }

        DBTest_Demo app = new DBTest_Demo();
        int rc = app.testconnection_mysql(offset);

        if (rc == 0) {
            System.out.println("MYSQL Remote Connection Successful Completion");
        } else {
            System.out.println("mysql DB connection fail");
        }
    }
}
