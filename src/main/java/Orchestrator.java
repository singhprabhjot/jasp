import faker.CreateFakeData;
import faker.Person;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jaspreet.kaur on 4/26/18.
 */
public class Orchestrator {

    public static void main(String[] args) {
        Orchestrator orchestrator = new Orchestrator();
        orchestrator.runMe();
    }

    private void runMe() {
        createFakeData();
        startDB();
        insertFriendsIntoNeo4j(CreateFakeData.personsList);
        createRelationshipBetweenFriends(CreateFakeData.friendsMap);
        runQueries();
        stopDB();
    }

    private void stopDB() {

    }

    private void startDB() {

    }

    private void runQueries() {

    }
    private void insertFriendsIntoNeo4j(List<Person> personsList) {
        RunQueries runQueries = new RunQueries(DBConnector.getInstance());
        runQueries.addFriendsToDB(personsList);

    }
    private void createRelationshipBetweenFriends(Map<String, Set<String>> friendsMap) {
        RunQueries runQueries=new RunQueries(DBConnector.getInstance());
        runQueries.createFriendsRelationsship(friendsMap);
    }



    private void createFakeData() {
        CreateFakeData fakeData = new CreateFakeData(100);
        fakeData.createFriends();
        fakeData.createRelationship();
    }
}
