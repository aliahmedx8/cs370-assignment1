import java.sql.*;

public class DBTest_Demo {
    // ===== Level 4 — WAN + TLS (VERIFY_CA) =====
    // If you change server IP or hostname, update HOST below.
    private static final String HOST = "18.189.21.239"; // MySQL server public IP (EC2)
    private static final String DB   = "SE";
    private static final String USER = "SE";
    private static final String PASS = "SE2020";

    // Option A: carry truststore in the JDBC URL (no -D flags needed)
    // Windows path must be file:/ and forward slashes:
    private static final String TRUSTSTORE_URL =
        "file:/C:/Users/iqbal/.ssh/mysql-truststore.jks";
    private static final String TRUSTSTORE_PASS = "changeit";

    // VERIFY_CA = verify server cert is signed by your CA
    // (Use VERIFY_IDENTITY if you also want hostname verification of 'HOST')
    private static final String JDBC_URL =
        "jdbc:mysql://" + HOST + ":3306/" + DB
      + "?useUnicode=true&characterEncoding=utf8"
      + "&serverTimezone=UTC"
      + "&enabledTLSProtocols=TLSv1.2,TLSv1.3"
      + "&sslMode=VERIFY_CA"
      + "&allowPublicKeyRetrieval=true"
      + "&trustCertificateKeyStoreUrl=" + TRUSTSTORE_URL
      + "&trustCertificateKeyStorePassword=" + TRUSTSTORE_PASS;

    public static void main(String[] args) {
        System.out.println("Example for MYSQL DB connection via Java (Level 4: WAN, SSL/TLS)");

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
            System.out.println("MYSQL Connection Successful (Level 4: WAN, SSL/TLS)");

        } catch (Exception e) {
            System.err.println("MySQL connection/query failed: " + e.getMessage());
            e.printStackTrace(System.out);
            System.out.println("mysql DB connection fail");
        }
    }
}
