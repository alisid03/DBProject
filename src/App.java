import java.util.Random;
import DBManager.*;
import java.io.*;

public class App {

    private static final String OG = "MOCK_DATA";
    private static final String INDEXED = "MOCK_DATA_INDEXED";

    static String password;

    public static void main(String[] args) throws Exception {
        
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter your MySQL Password: ");
        String pass = userInput.readLine();
        setPassword(pass);

        // Running the CRUD Tests
        RunTests();

    }

    // RUNNING TESTS
    public static void setPassword(String _password) {
        password = _password;
    }  

    public static void RunTests() {

        // Getting the algorithm managers
        HorizontalPartitioningManager partition = new HorizontalPartitioningManager(password);
        Sharding sharding = new Sharding(password);

        Random rand = new Random();
        
        // Establishing some id's that will be used for testing
        int idForGet = rand.nextInt(498)+1;
        int id = rand.nextInt(498)+1;

        // Establishing unversal data to test with
        Data dataToTestWith = new Data(id, "FIRST", "LAST", "EMAIL", "GENDER", "MAJOR", "ADDRESS");
        Data dataToUpdate = new Data(id, "FIRST-1", "LAST-1", "EMAIL-1", "GENDER-1", "MAJOR-1", "ADDRESS-1");

        // For some reason the first query to execute always executes longer than the rest, no matter what the query is or the type.
        // Thus, this will be a random query that will act as the first query so that the actual tests will have correct execution times.
        sharding.getDataById(100);

        // Running the tests on all CRUD Operations.
        RunRetrieveTest(partition, sharding);
        RunGetByIndexTest(idForGet, partition, sharding);
        RunDeleteTest(id, partition, sharding);
        RunInsertTest(dataToTestWith, partition, sharding);
        RunUpdateTest(dataToUpdate, id, partition, sharding);
        
        System.out.println("\n");
    }

    // Retreive test: Displays the time it takes for each algorithm to retreive every entry in the database.
    public static void RunRetrieveTest(HorizontalPartitioningManager partition, Sharding sharding) {

        // End - Start = the time a query takes
        long start;
        long end;
        
        System.out.println("\n\nCURRENT TEST: RETRIEVING ALL ENTRIES\n");
        
        // Original Table
        start = System.nanoTime();
        partition.retrieveAll(OG);
        end = System.nanoTime();
        System.out.println("Original Table:          " + (end - start) + "   nanoseconds");
        
        // Partition Size of 50
        start = System.nanoTime();
        partition.getAllFrom50Paritioned();
        end = System.nanoTime();
        System.out.println("Partition Size of 50:    " + (end - start) + "   nanoseconds");

        // Partition Size of 250
        start = System.nanoTime();
        partition.getAllFrom250Partition();
        end = System.nanoTime();
        System.out.println("Partition Size of 250:   " + (end - start) + "   nanoseconds");
        
        // Using Database Sharding
        start = System.nanoTime();
        sharding.retrieveAll();
        end = System.nanoTime();
        System.out.println("Sharding:                " + (end - start) + "   nanoseconds");

        // Original Table with Indexing
        start = System.nanoTime();
        partition.retrieveAll(INDEXED);
        end = System.nanoTime();
        System.out.println("Indexing:                " + (end - start) + "   nanoseconds");

    }

    // Get by ID test: Displays the time it takes for each algorithm to retreive a row at a particular index
    public static void RunGetByIndexTest(int id, HorizontalPartitioningManager partition, Sharding sharding) {
        
        // End - Start = the time a query takes
        long start;
        long end;

        System.out.println("\n\nCURRENT TEST: RETRIEVING ROW BY ID: " + id + "\n");

        // Original Table
        start = System.nanoTime();
        Data dataFrom1000 = partition.getDataById(id, OG);
        end = System.nanoTime();
        System.out.println("Original Table:         " + (end - start) + "   nanoseconds");
        
        // Partition Size of 50
        start = System.nanoTime();
        Data dataFrom50 = partition.getFrom50Parition(id);
        end = System.nanoTime();
        System.out.println("Partition Size of 50:   " + (end - start) + "   nanoseconds");

        // Partition Size of 250
        start = System.nanoTime();
        Data dataFrom250 = partition.getFrom250Parition(id);
        end = System.nanoTime();
        System.out.println("Partition Size of 250:  " + (end - start) + "   nanoseconds");

        // Database Sharding
        start = System.nanoTime();
        Data shardingData = sharding.getDataById(id);
        end = System.nanoTime();
        System.out.println("Sharding:               " + (end - start) + "   nanoseconds");

        // Original Table with Indexing
        start = System.nanoTime();
        Data dataFrom1000Indexed = partition.getDataById(id, INDEXED);
        end = System.nanoTime();
        System.out.println("Indexing:               " + (end - start) + "   nanoseconds");

    }   

    // Deletion test: Displays the time it takes for each algorithm to delete a row at a particular index
    public static void RunDeleteTest(int id, HorizontalPartitioningManager partition, Sharding sharding) {
        
        // End - Start = the time a query takes
        long start;
        long end;

        System.out.println("\n\nCURRENT TEST: DELETING ROW BY ID: " + id + "\n");

        // Original Table
        start = System.nanoTime();
        partition.deleteData(id, OG);
        end = System.nanoTime();
        System.out.println("Original Table:         " + (end - start) + "   nanoseconds");

        // Partition Size of 50
        start = System.nanoTime();
        partition.delete50Parition(id);
        end = System.nanoTime();
        System.out.println("Partition Size of 50:   " + (end - start) + "   nanoseconds");

        // Partition Size of 250
        start = System.nanoTime();
        partition.delete250Parition(id);
        end = System.nanoTime();
        System.out.println("Partition Size of 250:  " + (end - start) + "   nanoseconds");

        // Database Sharding
        start = System.nanoTime();
        sharding.deleteData(id);
        end = System.nanoTime();
        System.out.println("Sharding:               " + (end - start) + "   nanoseconds");
        
        // Original Table with Indexing
        start = System.nanoTime();
        partition.deleteData(id, INDEXED);
        end = System.nanoTime();
        System.out.println("Indexing:               " + (end - start) + "   nanoseconds");

    }

    // Insertion test: Displays the time it takes for each algorithm to insert a row at a particular index
    public static void RunInsertTest(Data data, HorizontalPartitioningManager partition, Sharding sharding) {
        
        // End - Start = the time a query takes
        long start;
        long end;
        
        System.out.println("\n\nCURRENT TEST: INSERTING INTO THE DATABASE\n");
        
        // Original Table
        start = System.nanoTime();
        partition.insertData(data, OG);
        end = System.nanoTime();
        System.out.println("Original Table:          " + (end - start) + "   nanoseconds");
        
        // Partition Size of 50
        start = System.nanoTime();
        partition.create50Partition(data, data.getId());
        end = System.nanoTime();
        System.out.println("Partition Size of 50:    " + (end - start) + "   nanoseconds");

        // Partition Size of 250 
        start = System.nanoTime();
        partition.create250Partition(data, data.getId());
        end = System.nanoTime();
        System.out.println("Partition Size of 250:   " + (end - start) + "   nanoseconds");
        
        // Database Sharding
        start = System.nanoTime();
        sharding.insertData(data);
        end = System.nanoTime();
        System.out.println("Sharding:                " + (end - start) + "   nanoseconds");

        // Original Table with Indexing
        start = System.nanoTime();
        partition.insertData(data, INDEXED);
        end = System.nanoTime();
        System.out.println("Indexing:                " + (end - start) + "   nanoseconds");
    }

    // Update test: Displays the time it takes for each algorithm to update a row at a particular index
    public static void RunUpdateTest(Data data, int id, HorizontalPartitioningManager partition, Sharding sharding) {
        
        // End - Start = the time a query takes
        long start;
        long end;
        
        System.out.println("\n\nCURRENT TEST: UPDATING A ROW IN THE DATABASE\n");
        
        // Original Table
        start = System.nanoTime();
        partition.updateData(data, OG);
        end = System.nanoTime();
        System.out.println("Original Table:          " + (end - start) + "   nanoseconds");
        
        // Partition Size of 50
        start = System.nanoTime();
        partition.update50Partition(data, id);
        end = System.nanoTime();
        System.out.println("Partition Size of 50:    " + (end - start) + "   nanoseconds");

        // Partition Size of 250
        start = System.nanoTime();
        partition.update250Partition(data, id);
        end = System.nanoTime();
        System.out.println("Partition Size of 250:   " + (end - start) + "   nanoseconds");
        
        // Database Sharding
        start = System.nanoTime();
        sharding.updateData(data);
        end = System.nanoTime();
        System.out.println("Sharding:                " + (end - start) + "   nanoseconds");

        // Original Table with Indexing
        start = System.nanoTime();
        partition.updateData(data, INDEXED);
        end = System.nanoTime();
        System.out.println("Indexing:                " + (end - start) + "   nanoseconds");


    }


}
