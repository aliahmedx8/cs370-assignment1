import java.io.File;
import java.sql.*;

public class DBTest_Demo {
    // Server & DB
    private static final String HOST = "18.189.21.239"; // Server PUBLIC IP
    private static final String DB   = "SE";
    private static final String USER = "SE";
    private static final String PASS = "SE2020";

    // Truststore (we'll create ca-truststore.p12 next)
    private static final String TRUSTSTORE_PATH = "C:/cs370-assignment1/certs/ca-truststore.p12";
    private static final String TRUSTSTORE_PASS = "changeit";
    private static final String TRUSTSTORE_TYPE = "PKCS12";

    // JDBC URL (set both VERIFY_CA and explicit truststore URL/Type/Pass)
    private static final String JDBC_URL =
        "jdbc:mysql://" + HOST + ":3306/" + DB
      + "?useUnicode=true&characterEncoding=utf8"
      + "&serverTimezone=UTC"
      + "&enabledTLSProtocols=TLSv1.3,TLSv1.2"
      + "&sslMode=VERIFY_CA"
      + "&trustCertificateKeyStoreUrl=file:///" + TRUSTSTORE_PATH.replace("\\", "/")
      + "&trustCertificateKeyStorePassword=" + TRUSTSTORE_PASS
      + "&trustCertificateKeyStoreType=" + TRUSTSTORE_TYPE
      + "&allowPublicKeyRetrieval=false"
      + "&connectTimeout=8000&socketTimeout=8000";

    public static void main(String[] args) {
        // Programmatic JVM truststore too (belt + suspenders)
        System.setProperty("javax.net.ssl.trustStore", TRUSTSTORE_PATH);
        System.setProperty("javax.net.ssl.trustStorePassword", TRUSTSTORE_PASS);
        System.setProperty("javax.net.ssl.trustStoreType", TRUSTSTORE_TYPE);

        // Optional: sanity echo so you can see what file it's using
        File f = new File(TRUSTSTORE_PATH);
        System.out.println("Truststore exists=" + f.exists() + " size=" + (f.exists() ? f.length() : 0));

        System.out.println("Example for MYSQL DB connection via Java (Level 4: WAN with SSL/TLS)");
        System.out.println("TLS mode: VERIFY_CA (PKCS12 truststore)");

        final String sql = "SELECT CONCAT('TLS OK @ ', NOW(), ' (server: ', @@hostname, ')')";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASS);
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
