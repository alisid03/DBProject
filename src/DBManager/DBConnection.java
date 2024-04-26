package DBManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;


public class DBConnection {

    private static String dbURL = "jdbc:mysql://localhost:3306/mytestdb";

    public Connection getConnection(String url, String _password) {
        
        String db;
        if(url == null) {
            db = dbURL;
        } else {
            db = url;
        }


        try {
            String username = "root";
            String password = System.getenv("DB_PASS");
            if(password == null) {
                password = _password;
            }
            Connection conn = DriverManager.getConnection(db, username, password);
            return conn;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}