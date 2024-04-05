import java.util.Random;

import DBManager.Data;
import DBManager.HorizontalPartitioningManager;

public class App {

    private static final String OG = "MOCK_DATA";

    private static final String PD = "PARTITIONED_DATA_";

    private static final String PD1 = "PARTITIONED_DATA_1";

    private static final String PD2 = "PARTITIONED_DATA_2";

    private static final String PD3 = "PARTITIONED_DATA_3";

    private static final String PD4 = "PARTITIONED_DATA_4";

    private static final String PD5 = "PARTITIONED_DATA_5";

    private static final String PD6 = "PARTITIONED_DATA_6";

    private static final String PD7 = "PARTITIONED_DATA_7";

    private static final String PD8 = "PARTITIONED_DATA_8";

    private static final String PD9 = "PARTITIONED_DATA_9";

    private static final String PD10 = "PARTITIONED_DATA_10";

    private static final String PD_250_1 = "PARTITIONED_DATA_250_1";

    private static final String PD_250_2 = "PARTITIONED_DATA_250_2";

    public static void main(String[] args) throws Exception {

        HorizontalPartitioningManager partition = new HorizontalPartitioningManager();
        Random rand = new Random();
        
        //See how long it takes to get all
        System.out.println("----------------------------------------------");
        System.out.println("RETRIEVING ALL");
        System.out.println("Getting all 1000 items");
        long startTimeGetAllFrom1000 = System.nanoTime();
        partition.retrieveAll(OG);
        long endTimeGetAllFrom1000 = System.nanoTime();
        System.out.println("Got all items in: " + (endTimeGetAllFrom1000 - startTimeGetAllFrom1000) + " nanoseconds");
        System.out.println();

        System.out.println("Getting 1000 items using parition of 50");
        long startTimeGetAllFrom50 = System.nanoTime();
        getAllFrom50Paritioned(partition);
        long endTimeGetAllFrom50 = System.nanoTime();
        System.out.println("Got all items in: " + (endTimeGetAllFrom50 - startTimeGetAllFrom50) + " nanoseconds");
        System.out.println();

        System.out.println("Getting 1000 items using parition of 250");
        long startTimeGetAllFrom250 = System.nanoTime();
        getAllFrom250Partition(partition);
        long endTimeGetAllFrom250 = System.nanoTime();
        System.out.println("Got all items in: " + (endTimeGetAllFrom250 - startTimeGetAllFrom250) + " nanoseconds");
        System.out.println("----------------------------------------------");

        System.out.println("RETRIEVING ONE ITEM");

        //Create a random number and perform the CRUD operations with it
        int id = rand.nextInt(498)+1;
        //perform retreive
        System.out.println("Getting data at index: " + id + " for table with 1000 item");
        long startTimeGetFrom1000 = System.nanoTime();
        Data dataFrom1000 = partition.getDataById(id, OG);
        long endTimeGetFrom1000 = System.nanoTime();
        System.out.println("Got item in: " + (endTimeGetFrom1000 - startTimeGetFrom1000) + " nanoseconds");
        System.out.println();

        System.out.println("Getting data at index " + id + " for table with 50 items");
        long startTimeGetFrom50 = System.nanoTime();
        Data dataFrom50 = getFrom50Parition(partition, id);
        long endTimeGetFrom50 = System.nanoTime();
        System.out.println("Got item in: " + (endTimeGetFrom50 - startTimeGetFrom50) + " nanoseconds");
        System.out.println();

        System.out.println("Getting data at index " + id + " for table with 250 items");
        long startTimeGetFrom250 = System.nanoTime();
        Data dataFrom250 = getFrom250Parition(partition, id);
        long endTimeGetFrom250 = System.nanoTime();
        System.out.println("Got item in: " + (endTimeGetFrom250 - startTimeGetFrom250) + " nanoseconds");
        System.out.println("----------------------------------------------");

        System.out.println("UPDATING ELEMENT");
        Data updatedDataFrom100 = new Data(dataFrom1000.getId(),"UPDATED","UPDATED","UPDATED","UPDATED","UPDATED","UPDATED");
        System.out.println("Updating element in 1000 table");
        long startTimeToUpdate1000 = System.nanoTime();
        partition.updateData(updatedDataFrom100, OG);
        long endTimeToUpdate1000 = System.nanoTime();
        System.out.println("Updated item in: " + (endTimeToUpdate1000 - startTimeToUpdate1000) + " nanoseconds");
        System.out.println();

        Data updatedDataFrom50 = new Data(dataFrom50.getId(),"UPDATED","UPDATED","UPDATED","UPDATED","UPDATED","UPDATED");
        System.out.println("Updating element in 50 table");
        long startTimeToUpdate50 = System.nanoTime();
        update50Partition(partition, updatedDataFrom50, id);
        long endTimeToUpdate50 = System.nanoTime();
        System.out.println("Updated item in: " + (endTimeToUpdate50 - startTimeToUpdate50) + " nanoseconds");
        System.out.println();
        
        Data updatedDataFrom250 = new Data(dataFrom250.getId(),"UPDATED","UPDATED","UPDATED","UPDATED","UPDATED","UPDATED");
        System.out.println("Updating element in 250 table");
        long startTimeToUpdate250 = System.nanoTime();
        update250Partition(partition, updatedDataFrom250, id);
        long endTimeToUpdate250 = System.nanoTime();
        System.out.println("Updated item in: " + (endTimeToUpdate250 - startTimeToUpdate250) + " nanoseconds");
        System.out.println();

        System.out.println("----------------------------------------------");
        System.out.println("DELETING DATA");
        System.out.println("DELETING DATA IN 1000 TABLE AT INDEX: " + id);
        long startTimeToDelete1000 = System.nanoTime();
        partition.deleteData(id, OG);
        long endTimeToDelete1000 = System.nanoTime();
        System.out.println("Deleted item in: " + (endTimeToDelete1000-startTimeToDelete1000) + " nanoseconds");
        System.out.println();

        System.out.println("DELETING DATA IN 50 TABLE AT INDEX: " + id);
        long startTimeToDelete50 = System.nanoTime();
        delete50Parition(partition, id);
        long endTimeToDelete50 = System.nanoTime();
        System.out.println("Deleted item in: " + (endTimeToDelete50-startTimeToDelete50) + " nanoseconds");
        System.out.println();

        System.out.println("DELETING DATA IN 250 TABLE AT INDEX: " + id);
        long startTimeToDelete250 = System.nanoTime();
        delete250Parition(partition, id);
        long endTimeToDelete250 = System.nanoTime();
        System.out.println("Deleted item in: " + (endTimeToDelete250-startTimeToDelete250) + " nanoseconds");
        System.out.println();
        System.out.println("----------------------------------------------");
        
        System.out.println("INSERTING DATA");
        System.out.println("Inserting data in 1000 table at index: " + id);
        long startTimeToCreate1000 = System.nanoTime();
        partition.insertData(dataFrom1000, OG);
        long endTimeToCreate1000 = System.nanoTime();
        System.out.println("Inserted  item in: " + (endTimeToCreate1000-startTimeToCreate1000) + " nanoseconds");
        System.out.println();

        System.out.println("Inserting data in 50 table at index: " + id);
        long startTimeToCreate50 = System.nanoTime();
        create50Partition(partition, dataFrom50, id);
        long endTimeToCreate50 = System.nanoTime();
        System.out.println("Inserted  item in: " + (endTimeToCreate50-startTimeToCreate50) + " nanoseconds");
        System.out.println();

        System.out.println("Inserting data in 250 table at index: " + id);
        long startTimeToCreate250 = System.nanoTime();
        create250Partition(partition, dataFrom250, id);
        long endTimeToCreate250 = System.nanoTime();
        System.out.println("Inserted  item in: " + (endTimeToCreate250-startTimeToCreate250) + " nanoseconds");
        System.out.println();
        /*
         * Come up with the code to test the timing of performing all the CRUD operations against the different partitions.
         */
        System.out.println("Hello, World!");
    }

    public static void getAllFrom50Paritioned(HorizontalPartitioningManager partition){
        for(int i = 1; i <= 10; i++) {
            String db_name = PD+i;
            partition.retrieveAll(db_name);
        }
        for(int i = 1; i <= 10; i++) {
            String db_name = PD+i;
            partition.retrieveAll(db_name);
        }
    }

    public static void getAllFrom250Partition(HorizontalPartitioningManager partition) {
        for(int i = 1; i<=2; i++) {
            String db_name = PD+"250_"+i;
            partition.retrieveAll(db_name);
        }
        for(int i = 1; i<=2; i++) {
            String db_name = PD+"250_"+i;
            partition.retrieveAll(db_name);
        }
    }


    public static Data getFrom50Parition(HorizontalPartitioningManager parition, int index) {
        int id = index%50+1; 
        int table = index/50 + 1;
        String db_name = PD + table;
        Data data = parition.getDataById(id, db_name);
        return data;
    }


    public static Data getFrom250Parition(HorizontalPartitioningManager partition, int index) {
        int id = index%250+1; 
        int table = index/250+1;
        String db_name = PD + "250_" + table;
        return partition.getDataById(id, db_name);
    }

    public static void update50Partition(HorizontalPartitioningManager partition, Data data, int index) {
        int table = index/50 + 1;
        String db_name = PD + table;
        partition.updateData(data, db_name);
    }

    public static void update250Partition(HorizontalPartitioningManager partition, Data data, int index) {
        int table = index/250 + 1;
        String db_name = PD + "250_" + table;
        partition.updateData(data, db_name);
    }

    public static void delete50Parition(HorizontalPartitioningManager parition, int index) {
        int id = index%50+1; 
        int table = index/50 + 1;
        String db_name = PD+table;
        parition.deleteData(id, db_name);
    }

    public static void delete250Parition(HorizontalPartitioningManager parition, int index) {
        int id = index%250+1; 
        int table = index/250 + 1;
        String db_name = PD+"250_"+table;
        parition.deleteData(id, db_name);
    }

    public static void create50Partition(HorizontalPartitioningManager partition,Data data, int index) {
        int table = index/50 + 1;
        String db_name = PD+table;
        partition.insertData(data, db_name);
    }

    public static void create250Partition(HorizontalPartitioningManager partition,Data data, int index) {
        int table = index/250 + 1;
        String db_name = PD+"250_"+table;
        partition.insertData(data, db_name);
    }

}
