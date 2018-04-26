import com.google.gson.Gson;
import faker.Person;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jaspreet.kaur on 4/26/18.
 */
public class DBHelper {
    DBConnector driver;

    public DBHelper() {
        this.driver = DBConnector.getInstance();
    }

    public void addFriendsToDB(List<Person> friendsList) {
        for (Person p : friendsList) {
//            System.out.println(p.toString());
            String createFriend = "CREATE (" + p.getUsername() + ":PERSON " + p.toJSON() + ")";
//            System.out.println(createFriend);
            driver.runCommand(createFriend);
        }
        System.out.println("Adding friends to NEO4J: Done:");

    }

    public void createFriendsRelationShip(Map<String, Set<String>> friendsMap) {
        Gson gson = new Gson();
        Iterator it = friendsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Set<Person>> pair = (Map.Entry) it.next();
            String candidate = pair.getKey();
            Set<String> friendsSet = friendsMap.get(candidate);
            for (String s : friendsSet) {
                String createRelationship = "MATCH (u:PERSON {username:'" + candidate + "'}), (v:PERSON {username:'" + s + "'})\n" +
                        "CREATE (u)-[:FRIENDS]->(v)";
//              System.out.println(createRelationship);
                driver.runCommand(createRelationship);
            }

        }
        System.out.println("Create Relationships: Done");
    }


    public void createIndexOnField(String fieldName) {
        driver.runCommand(" CREATE INDEX ON :" + Constants.NAMESPACE_LABEL + "(" + fieldName + ")");
        System.out.println("Create Index  field [" + fieldName + "] : Done ");
    }

    public void deleteLabelIfExists() {
        driver.runCommand("MATCH (n:" + Constants.NAMESPACE_LABEL + ") DETACH DELETE n");
        System.out.println("Deletion of label [" + Constants.NAMESPACE_LABEL + "]: Done");

    }
}
