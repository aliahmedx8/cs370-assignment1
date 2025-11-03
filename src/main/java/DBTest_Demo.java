import java.sql.*;

public class DBTest_Demo {
    // Level 4 — Laptop (public) -> Server (public) with TLS
    private static final String HOST = "18.189.21.239"; // server public IP
    private static final String DB   = "SE";
    private static final String USER = "SE";
    private static final String PASS = "SE2020";

    // VERIFY_CA uses your truststore that contains the server CA (ca.pem)
    private static final String JDBC_URL =
        "jdbc:mysql://" + HOST + ":3306/" + DB
        + "?useUnicode=true&characterEncoding=utf8"
        + "&serverTimezone=UTC"
        + "&sslMode=VERIFY_CA"
        + "&enabledTLSProtocols=TLSv1.2,TLSv1.3"
        + "&allowPublicKeyRetrieval=true"
        + "&connectTimeout=5000&socketTimeout=5000";

    public static void main(String[] args) {
        System.out.println("Example for MYSQL DB connection via Java (Level 4: WAN, SSL/TLS)");
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASS)) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT NOW()")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Current MySQL time: " + rs.getString(1));
                }
            }
            // Show TLS cipher (optional)
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SHOW SESSION STATUS LIKE 'Ssl_cipher'")) {
                if (rs.next()) System.out.println("SSL cipher: " + rs.getString(2));
            }
            System.out.println("MYSQL Connection Successful (Level 4: WAN, SSL/TLS)");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mysql DB connection fail");
        }
    }
}
