
import java.sql.Connection;
import DBManager.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.FileNotFoundException;  
import java.util.Scanner; 
import java.io.*;

public class CreateDatabases {

    String password;

    public CreateDatabases(String _password) {
        password = _password;
    }

    public static void main(String[] args) {

        String pass = "";
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter your MySQL Password: ");
            pass = userInput.readLine();        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        CreateDatabases cd = new CreateDatabases(pass);

        
        // run the queries to create the databases and tables
        cd.CreateOriginal();
        cd.CreatePartitioned();
        cd.CreateIndexed();
        cd.CreateShards();
    }

    // Get the query from the query folder
    public String getQuery(String queryName) {
        String query = "";
        try {
            // getting the file
            File queryFile = new File("queries/" + queryName + ".sql");
            // read in the query from the file
            Scanner sc = new Scanner(queryFile);
            query = sc.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // return the query
        return query;
    }

    // Runs the query
    public void runQuery(String queryFile) {
        
        // get the query from the file
        String query = getQuery(queryFile);

        // create the connection
        DBConnection dbConnection = new DBConnection();
        // allowMultiQueries = true allows for the connection to execute a file of queries rather than a single query
        try (Connection conn = dbConnection.getConnection("jdbc:mysql://localhost:3306/?allowMultiQueries=true", password)) {    
            // execute the query
            PreparedStatement statement = conn.prepareStatement(query);
            statement.executeUpdate();
            System.out.println("File " + queryFile + " Executed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CreateIndexed() {
        // indexedTable
        runQuery("IndexedTable");
    }

    public void CreateOriginal() {
        // Original Table
        runQuery("Original");
    }   

    public void CreatePartitioned() {
        // Run all of the partition queries to create the size 50 partition tables
        String queryFileForSize50 = "";
        for(int i = 1; i < 11; i++) {
            queryFileForSize50 = "PartitionedData50-" + i;
            runQuery(queryFileForSize50);
        }
        // Run all of the partition queries to create the size 250 partition tables
        String queryFileForSize250 = "";
        for(int i = 1; i < 3; i++) {
            queryFileForSize250 = "PartitionedData250-" + i;
            runQuery(queryFileForSize250);
        }
    }

    public void CreateShards() {
        // shard1 
        runQuery("shard1");
        // shard2
        runQuery("shard2");
        // shard3
        runQuery("shard3");
        //shard4
        runQuery("shard4");
    }

    

}