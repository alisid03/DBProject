package DBManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    private static String dbURL = "jdbc:mysql://localhost:3306/mytestdb";
        
    public Connection getConnection() {
        try {
            String username = "root";
            String password = System.getenv("DB_PASS");
            if(password == null) {
                password = "Psa2023!";
            }
            Connection conn = DriverManager.getConnection(dbURL, username, password);
            return conn;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}