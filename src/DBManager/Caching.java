package DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Caching {

    String password;

    public Caching(String _password) {
        password = _password;
    }


    public Map<Integer, Data> cache = new HashMap<>();
    public List<Data> retrieveAll(String db_name){

        if (!cache.isEmpty()) {
            return new ArrayList<>(cache.values()); // Return data from cache if it's populated
        }
        DBConnection dbConnection = new DBConnection();

        try(Connection conn = dbConnection.getConnection(null, password)) {
            String sql = "SELECT * FROM %s";
            sql = String.format(sql, db_name);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<Data> dataList = new ArrayList<Data>();

            while(rs.next()) {
                Data data = new Data();
                data.setId(rs.getInt("id"));
                data.setFirst_name(rs.getString("first_name"));
                data.setLast_name(rs.getString("last_name"));
                data.setEmail(rs.getString("email"));
                data.setGender(rs.getString("gender"));
                data.setMajor(rs.getString("major"));
                data.setAddress(rs.getString("address"));

                dataList.add(data);
                cache.put(data.getId(), data);
            }
            conn.close();
            return dataList;
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }

    }

    public void insertData(Data data, String tableName) {
        DBConnection dbConnection = new DBConnection();

        try (Connection conn = dbConnection.getConnection(null, password)) {
            String sql = "INSERT INTO %s (id, first_name, last_name, email, gender, major, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            sql = String.format(sql, tableName);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, data.getId());
            statement.setString(2, data.getFirst_name());
            statement.setString(3, data.getLast_name());
            statement.setString(4, data.getEmail());
            statement.setString(5, data.getGender());
            statement.setString(6, data.getMajor());
            statement.setString(7, data.getAddress());
            statement.executeUpdate();
            // System.out.println("Record inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Data getDataById(int id, String table_name) {
        
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.getConnection(null, password)) {
            String sql = "SELECT * FROM %s WHERE id = ?";
            sql = String.format(sql, table_name);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Data data = new Data();
                data.setId(result.getInt("id"));
                data.setFirst_name(result.getString("first_name"));
                data.setLast_name(result.getString("last_name"));
                data.setEmail(result.getString("email"));
                data.setGender(result.getString("gender"));
                data.setMajor(result.getString("major"));
                data.setAddress(result.getString("address"));
                // Add the retrieved data to the cache
                cache.put(id, data);

                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public void updateData(Data data, String tableName) {
        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.getConnection(null, password)) {
            String sql = "UPDATE %s SET first_name = ?, last_name = ?, email = ?, gender = ?, major = ?, address = ? WHERE id = ?";
            sql = String.format(sql, tableName);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, data.getFirst_name());
            statement.setString(2, data.getLast_name());
            statement.setString(3, data.getEmail());
            statement.setString(4, data.getGender());
            statement.setString(5, data.getMajor());
            statement.setString(6, data.getAddress());
            statement.setInt(7, data.getId());
            statement.executeUpdate();
            // System.out.println("Record updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void deleteData(int id, String tableName) {
        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.getConnection(null, password)) {
            String sql = "DELETE FROM %s WHERE id = ?";
            sql = String.format(sql, tableName);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            // System.out.println("Record deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
