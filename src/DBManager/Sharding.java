package DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Sharding {

    private static String shard1URL = "jdbc:mysql://localhost:3306/shard1";
    private static String shard2URL = "jdbc:mysql://localhost:3306/shard2";
    private static String shard3URL = "jdbc:mysql://localhost:3306/shard3";
    private static String shard4URL = "jdbc:mysql://localhost:3306/shard4"; 

    private static int NUM_SHARDS = 4;
    
    String password;

    public Sharding(String _password) {
        password = _password;
    }

    // Hashing function to determine which of the four shards to enter into
    // Hashes on the id
    public int hash(int id) {
        return id % 4;
    }

    // determines which shard based upon the hash value 
    public String determineShard(int id) {
        int n = hash(id);

        if(n == 3) {
            return shard4URL;
        }
        if(n == 2) {
            return shard3URL;
        }
        if(n == 1) {
            return shard2URL;
        }
        else {
            return shard1URL;
        }
    }


    // insert the data!
    // hash the id of the data inserting to determine the shard, and insert into that shard
    public void insertData(Data data) {
        // get the id and hash it to determine the shard.
        int id = data.getId();
        String shardURL = determineShard(id);

        // create the connection
        DBConnection dbConnection = new DBConnection();

        // pass in the shardurl
        try (Connection shard = dbConnection.getConnection(shardURL, password)) {
            // create the SQL statement
            String sql = "INSERT INTO MOCK_DATA (id, first_name, last_name, email, gender, major, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement statement = shard.prepareStatement(sql);
            statement.setInt(1, data.getId());
            statement.setString(2, data.getFirst_name());
            statement.setString(3, data.getLast_name());
            statement.setString(4, data.getEmail());
            statement.setString(5, data.getGender());
            statement.setString(6, data.getMajor());
            statement.setString(7, data.getAddress());
            statement.executeUpdate();
            // System.out.println("Record Inserted Successfully");
            // close the shard connection when complete
            shard.close();
        // catch errors!
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    public void deleteData(int id) {
        
        // get the id and hash it to determine the shard.
        String shardURL = determineShard(id);

        DBConnection dbConnection = new DBConnection();

        try (Connection conn = dbConnection.getConnection(shardURL, password)) {
            String sql = "DELETE FROM MOCK_DATA WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            // System.out.println("Record deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public void updateData(Data data) {
        // get the id and hash it to determine the shard.
        String shardURL = determineShard(data.getId());

        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.getConnection(shardURL, password)) {
            String sql = "UPDATE MOCK_DATA SET first_name = ?, last_name = ?, email = ?, gender = ?, major = ?, address = ? WHERE id = ?";
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
    

    public Data getDataById(int id) {
        DBConnection dbConnection = new DBConnection();
        String shardURL = determineShard(id);
        try (Connection conn = dbConnection.getConnection(shardURL, password)) {
            String sql = "SELECT * FROM MOCK_DATA WHERE id = ?";
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
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Data> retrieveAll(){

        DBConnection dbConnection = new DBConnection();
        List<Data> dataList = new ArrayList<Data>();
        for(int i = 0; i < NUM_SHARDS; i++) {
            String connectionURL = determineShard(i);
            try(Connection conn = dbConnection.getConnection(connectionURL, password)) {
                String sql = "SELECT * FROM MOCK_DATA";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
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
            } catch (SQLException exc) {
                throw new RuntimeException(exc);
            }
        }
        return dataList;



    }
}
