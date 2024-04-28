package DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

public class HorizontalPartitioningManager {
    
    private static final String PD = "PARTITIONED_DATA_";

    String password;
    public HorizontalPartitioningManager(String _password) {
        password = _password;
    }

    // get every entry
    public List<Data> retrieveAll(String db_name){

        DBConnection dbConnection = new DBConnection();
        // create the query
        try(Connection conn = dbConnection.getConnection(null, password)) {
            String sql = "SELECT * FROM %s";
            sql = String.format(sql, db_name);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<Data> dataList = new ArrayList<Data>();
            // get every entry
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
            }
            conn.close();
            return dataList;
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }

    }

    // insert the data
    public void insertData(Data data, String tableName) {
        DBConnection dbConnection = new DBConnection();
        // create the query
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

    // get the data by id
    public Data getDataById(int id, String table_name) {
        DBConnection dbConnection = new DBConnection();
        // create the query
        try (Connection conn = dbConnection.getConnection(null, password)) {
            String sql = "SELECT * FROM %s WHERE id = ?";
            sql = String.format(sql, table_name);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            // get the entry to return
            if (result.next()) {
                Data data = new Data();
                data.setId(result.getInt("id"));
                data.setFirst_name(result.getString("first_name"));
                data.setLast_name(result.getString("last_name"));
                data.setEmail(result.getString("email"));
                data.setGender(result.getString("gender"));
                data.setMajor(result.getString("major"));
                data.setAddress(result.getString("address"));
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // update the data
    public void updateData(Data data, String tableName) {
        DBConnection dbConnection = new DBConnection();
        // create the query
        try (Connection conn = dbConnection.getConnection(null, password)) {
            String sql = "UPDATE %s SET first_name = ?, last_name = ?, email = ?, gender = ?, major = ?, address = ? WHERE id = ?";
            sql = String.format(sql, tableName);
            // pass in parameters
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

    // delete the data    
    public void deleteData(int id, String tableName) {
        // create connection and query
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
    
    // partition size 50, retrieve all
    public void getAllFrom50Paritioned(){
        for(int i = 1; i <= 10; i++) {
            String db_name = PD+i;
            retrieveAll(db_name);
        }
        for(int i = 1; i <= 10; i++) {
            String db_name = PD+i;
            retrieveAll(db_name);
        }
    }


    // partition size 250, retrieve all
    public void getAllFrom250Partition() {
        for(int i = 1; i<=2; i++) {
            String db_name = PD+"250_"+i;
            retrieveAll(db_name);
        }
        for(int i = 1; i<=2; i++) {
            String db_name = PD+"250_"+i;
            retrieveAll(db_name);
        }
    }


    
    // partition size 50, retrieve entry
    public Data getFrom50Parition(int index) {
        int id = index%50+1; 
        int table = index/50 + 1;
        String db_name = PD + table;
        Data data = getDataById(id, db_name);
        return data;
    }

    // partition size 50, retrieve entry
    public Data getFrom250Parition(int index) {
        int id = index%250+1; 
        int table = index/250+1;
        String db_name = PD + "250_" + table;
        return getDataById(id, db_name);
    }

    // partition size 50, update entry
    public void update50Partition(Data data, int index) {
        int table = index/50 + 1;
        String db_name = PD + table;
        updateData(data, db_name);
    }

    // partition size 250, update entry
    public void update250Partition(Data data, int index) {
        int table = index/250 + 1;
        String db_name = PD + "250_" + table;
        updateData(data, db_name);
    }

    // partition size 50, delete entry
    public void delete50Parition(int index) {
        int id = index%50+1; 
        int table = index/50 + 1;
        String db_name = PD+table;
        deleteData(id, db_name);
    }

    // partition size 250, retrieve entry
    public void delete250Parition(int index) {
        int id = index%250+1; 
        int table = index/250 + 1;
        String db_name = PD+"250_"+table;
        deleteData(id, db_name);
    }

    // partition size 50, create entry
    public void create50Partition(Data data, int index) {
        int table = index/50 + 1;
        String db_name = PD+table;
        insertData(data, db_name);
    }

    // partition size 250, create entry
    public void create250Partition(Data data, int index) {
        int table = index/250 + 1;
        String db_name = PD+"250_"+table;
        insertData(data, db_name);
    }


}


