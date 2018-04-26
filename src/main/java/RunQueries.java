import com.google.gson.Gson;
import faker.Person;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jaspreet.kaur on 4/26/18.
 */
public class RunQueries {
    DBConnector driver;

    public RunQueries(DBConnector instance) {
        driver = instance;
    }


    public void addFriendsToDB(List<Person> friendsList) {
        Gson gson = new Gson();
        for (Person p : friendsList) {
//            System.out.println(p.toString());
            String createFriend = "CREATE (" + p.getUsername() + ":PERSON " + p.toJSON() + ")";
//            System.out.println(createFriend);
            driver.runCommand(createFriend);
        }

    }

    public void createFriendsRelationsship(Map<String, Set<String>> friendsMap) {
        Gson gson = new Gson();
        Iterator it = friendsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Set<Person>> pair = (Map.Entry) it.next();
            String candidate = pair.getKey();
            Set<String> friendsSet = friendsMap.get(candidate);
            for (String s : friendsSet) {
                String createRelationship = "MATCH (u:PERSON {username:'" + candidate + "'}), (v:PERSON {username:'" + s + "'})\n" +
                        "CREATE (u)-[:FRIENDS]->(v)";
                System.out.println(createRelationship);

                driver.runCommand(createRelationship);
            }

        }

    }

    public static void main(String s[]) {
        RunQueries runQueries = new RunQueries(DBConnector.getInstance());
        runQueries.runSampleQuery();
    }

    private void runSampleQuery() {
//        driver.runCommand("CREATE (a:Greeting) " +
//                "SET a.message = $message " +
//                "RETURN a.message + ', from node ' + id(a)", "helllllllooo");

        driver.runCommand("CREATE (a:Greeting) " +
                "SET a.message = \"prabhjot\" " +
                "RETURN a.message + ', from node ' + id(a)");

    }

    public void setIndexes() {
//        CREATE INDEX ON :User(username)
//        CREATE INDEX ON :Role(name)
//        MATCH (u:User {username:'admin'}), (r:Role {name:'ROLE_WEB_USER'})
//        CREATE (u)-[:HAS_ROLE]->(r)

    }
}
