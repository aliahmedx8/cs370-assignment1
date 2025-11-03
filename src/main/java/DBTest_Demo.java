import java.sql.*;

public class DBTest_Demo {
    /*
     * Assignment 1 — Level 4 (WAN with SSL/TLS)
     * Windows laptop -> MySQL on Server EC2
     *
     * This version uses a Java truststore (JKS) that contains your server CA.
     * URL uses sslMode=VERIFY_CA + trustCertificateKeyStoreUrl/Password.
     * You can still pass "noverify" to test tls without CA verification (REQUIRED).
     */

    // --- Server & DB credentials ---
    private static final String HOST = "18.189.21.239"; // Server PUBLIC IP
    private static final String DB   = "SE";
    private static final String USER = "SE";
    private static final String PASS = "SE2020";

    // --- Truststore generated from server-ca.pem ---
    // Make sure this file exists (we create it with keytool in the steps below).
    // Use "file:/" URL syntax for Windows paths.
    private static final String TRUSTSTORE_URL  = "file:/C:/cs370-assignment1/certs/ca-truststore.jks";
    private static final String TRUSTSTORE_PASS = "changeit"; // use same when creating the keystore

    // --- Common JDBC parameters ---
    private static final String COMMON =
            "?useUnicode=true&characterEncoding=utf8"
          + "&serverTimezone=UTC"
          + "&enabledTLSProtocols=TLSv1.3,TLSv1.2"
          + "&connectTimeout=8000&socketTimeout=8000";

    private static String buildJdbcUrl(boolean verifyCA) {
        if (verifyCA) {
            // Verified TLS via Java truststore (preferred/reliable on Windows)
            return "jdbc:mysql://" + HOST + ":3306/" + DB
                 + COMMON
                 + "&sslMode=VERIFY_CA"
                 + "&trustCertificateKeyStoreUrl=" + TRUSTSTORE_URL
                 + "&trustCertificateKeyStorePassword=" + TRUSTSTORE_PASS
                 + "&allowPublicKeyRetrieval=false";
        } else {
            // Diagnostic fallback: TLS without CA verification
            return "jdbc:mysql://" + HOST + ":3306/" + DB
                 + COMMON
                 + "&sslMode=REQUIRED"
                 + "&allowPublicKeyRetrieval=false";
        }
    }

    public static void main(String[] args) {
        System.out.println("Example for MYSQL DB connection via Java (Level 4: WAN with SSL/TLS)");

        int offsetHours = 0;
        boolean verifyCA = true; // default VERIFY_CA
        for (String a : args) {
            if ("noverify".equalsIgnoreCase(a)) verifyCA = false;
            try { offsetHours = Integer.parseInt(a); } catch (Exception ignore) {}
        }

        final String jdbcUrl = buildJdbcUrl(verifyCA);
        System.out.println("TLS mode: " + (verifyCA ? "VERIFY_CA (truststore)" : "REQUIRED (no verify)"));

        // Simple query + show TLS details
        final String sql = "SELECT CONCAT('TLS OK @ ', NOW(), ' (server: ', @@hostname, ')')";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) System.out.println(rs.getString(1));
            }

            try (Statement st = conn.createStatement()) {
                try (ResultSet r1 = st.executeQuery("SHOW STATUS LIKE 'Ssl_version'")) {
                    if (r1.next()) System.out.println("SSL Version: " + r1.getString(2));
                }
                try (ResultSet r2 = st.executeQuery("SHOW STATUS LIKE 'Ssl_cipher'")) {
                    if (r2.next()) System.out.println("SSL Cipher : " + r2.getString(2));
                }
            }

            System.out.println("MYSQL Connection Successful (Level 4: WAN with SSL/TLS)");
        } catch (Exception e) {
            System.err.println("MySQL connection/query failed: " + e.getMessage());
            System.out.println("mysql DB connection fail (Level 4)");
        }
    }
}
