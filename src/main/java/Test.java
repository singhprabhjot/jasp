import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

/**
 * Created by jaspreet.kaur on 4/26/18.
 */
public class Test {

    public Test() {
        startDatabase();
    }

    private void startDatabase() {
        // This method starts the neo4j embedded database
        File f = new File(Constants.DB_DIRECTORY);
        GraphDatabaseService graphDb = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(f)
                .loadPropertiesFromFile(Constants.DB_CONFIG_DIRECTORY + "neo4j.conf")
                .newGraphDatabase();
        registerShutdownHook(graphDb);
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public static void main(String[] args) {
        Test t = new Test();
        System.out.println("hi");
    }

}
