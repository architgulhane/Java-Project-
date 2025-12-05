import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Database credentials details
    private static final String URL = "jdbc:mysql://localhost:3306/LibraryDB?useSSL=false&serverTimezone=UTC";
    // IMPORTANT: Change these to your actual MySQL username and password
    private static final String USER = "root"; 
    private static final String PASSWORD = "YOUR_PASSWORD";

    public static Connection getConnection() throws SQLException {
        // In newer JDBC drivers, Class.forName() is often optional, 
        // but good to include for compatibility.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
