package utilities;

import org.neo4j.driver.v1.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

public class DBConnector implements AutoCloseable {

    private static DBConnector instance = null;
    private final Driver driver;
    GraphDatabaseService graphDb;

    protected DBConnector() {
        driver = GraphDatabase.driver(Constants.CONNECTION_PROTOCOL + "://" + Constants.HOST_URL, AuthTokens.basic(Constants.USERNAME, Constants.PASSWORD));
        File f = new File(Constants.DB_CONFIG_DIRECTORY);
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(f);


    }

    public static DBConnector getInstance() {
        if (instance == null) {
            instance = new DBConnector();
        }
        return instance;
    }


    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void runCommand(String command, String message) {
        try (Session session = driver.session()) {
            String greeting = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction tx) {
                    StatementResult result = tx.run(command,
                            parameters("message", message));
                    return result.single().get(0).asString();
                }
            });
        }
    }

    public void runCommand(String command) {
        try (Session session = driver.session()) {
            String greeting = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction tx) {
                    StatementResult result = tx.run(command);
                    return null;
                }
            });
        }
    }

    public GraphDatabaseService getGraphDBConnector() {
        return graphDb;
    }

    public List<Record> runTx(String command) {
        List<Record> records;
        try (Session session = driver.session()) {
            records = session.writeTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction tx) {
                    StatementResult result = tx.run(command);
                    List<Record> recordList = result.list();
                    System.out.println(recordList.size());
                    return recordList;
                }
            });
        }
        return records;
    }

}