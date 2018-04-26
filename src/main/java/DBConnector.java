import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

public class DBConnector implements AutoCloseable {

    private static DBConnector instance = null;
    private final Driver driver;

    protected DBConnector() {
        driver = GraphDatabase.driver(Constants.CONNECTION_PROTOCOL + "://" + Constants.HOST_URL, AuthTokens.basic(Constants.USERNAME, Constants.PASSWORD));
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

}