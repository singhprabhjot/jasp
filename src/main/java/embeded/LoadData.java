package embeded;

import faker.CreateFakeGotCharacters;
import faker.CreateFakePersons;
import faker.Person;
import org.neo4j.graphdb.*;
import utilities.Constants;
import utilities.DBConnector;

import java.util.*;

/**
 * Created by jaspreet.Kaur on 4/26/18.
 * This class is used to load fake data into NEO4J
 */

public class LoadData {
    GraphDatabaseService graphDB;

    public LoadData() {
        graphDB = DBConnector.getInstance().getGraphDBConnector();
    }

    public enum Labels implements Label {
        USER,
        GOT_CHARACTERS;
    }


    public enum RelationshipTypes implements RelationshipType {
        IS_FRIEND,
        LIKES_CHARACTER;
    }


    public void loadPersonData(int numberOfPerson, int gOTCharactersCount) {
        Person p = new Person();

        Transaction tx = null;
        tx = graphDB.beginTx();

        CreateFakePersons fakeData = new CreateFakePersons(numberOfPerson);
        fakeData.createFriends();
        fakeData.createRelationship();
        addFriendsToDB(CreateFakePersons.personsList);
        createFriendsRelationShip(CreateFakePersons.friendsMap);

        CreateFakeGotCharacters.createGOTCharacters(gOTCharactersCount);
        addGOTCharactersToDB(CreateFakeGotCharacters.gOTCharacterSet);
        createLinkageBetweenPersonAndGOTCharacters();
        tx.success();


    }

    private void createLinkageBetweenPersonAndGOTCharacters() {
        System.out.println("Add Linkages between PERSONS and GOT Characters: Started");
        List<Person> personsList = CreateFakePersons.personsList;

        int tenPercentSize = CreateFakePersons.personsList.size() / 10;
        for (int i = 0; i < personsList.size(); i++) {
            if (i % tenPercentSize == 0) {
                System.out.println("Adding Linkages between PERSONS and GOT Characters in NEO4J. Linkages for [" + i + "] of [" + personsList.size() + "] persons completed");
            }
            String username = personsList.get(i).getUsername();
            for (int j = 0; j < Constants.GOT_CHARACTERS_PER_PERSON; j++) {
                String gotCharacter = getRandomGOTCharacter();
                likeGotCharacter(username, gotCharacter, getRandomLikeScore());
            }
        }
        System.out.println("Adding Linkages between PERSONS and GOT Characters: Done");
    }

    private int getRandomLikeScore() {
        Random rand = new Random();
        int n = rand.nextInt(Constants.MAX_STARS_PER_GOT_CHARACTER) + 1;
        return n;
    }

    private String getRandomGOTCharacter() {
        String lastCharacter = null;
        int size = CreateFakeGotCharacters.gOTCharacterSet.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for (String obj : CreateFakeGotCharacters.gOTCharacterSet) {
            lastCharacter = obj;
            if (i == item)
                return obj;
            i++;
        }
        return lastCharacter;
    }

    private Relationship likeGotCharacter(String username, String gOTCharacterName, int stars) {
        Node user = findAPerson(username);
        Node gOTCharacter = findAGotCharacter(gOTCharacterName);
        Relationship relationship = user.createRelationshipTo(gOTCharacter, RelationshipTypes.LIKES_CHARACTER);
        relationship.setProperty("stars", stars);
        return relationship;
    }

    private Node findAGotCharacter(String gOTCharacterName) {
        Node myNode = graphDB.findNode(Labels.GOT_CHARACTERS, "name", gOTCharacterName);
        return myNode;
    }


    private Node findAPerson(String username) {
        Node myNode = graphDB.findNode(Labels.USER, "username", username);
        return myNode;
    }

    private void addFriendsToDB(List<Person> friendsList) {
        for (Person p : friendsList) {
            Node myNode = graphDB.createNode();
            myNode.addLabel(Labels.USER);
            myNode.setProperty("username", p.getUsername());
            myNode.setProperty("firstName", p.getFirstName());
            myNode.setProperty("lastName", p.getLastName());
            myNode.setProperty("title", p.getTitle());
            myNode.setProperty("designation", p.getDesignation());
            myNode.setProperty("city", p.getCity());
            myNode.setProperty("companyName", p.getCompanyName());
            myNode.setProperty("ssn", p.getSsn());
            myNode.addLabel(Labels.USER);
        }
        System.out.println("Adding friends to NEO4J: Done");
    }

    private void createRelationshipInTwoUsers(String username1, String username2) {
        Node user1 = findAPerson(username1);
        Node user2 = findAPerson(username2);
        user1.createRelationshipTo(user2, RelationshipTypes.IS_FRIEND);
    }

    private void createFriendsRelationShip(Map<String, Set<String>> friendsMap) {
        System.out.println("Add Relationships to NEO4J: Started");
        Iterator it = friendsMap.entrySet().iterator();
        int i = 0;
        int tenPercentSize = friendsMap.size() / 10;
        while (it.hasNext()) {
            if (i % tenPercentSize == 0) {
                System.out.println("Adding Relationships to NEO4J: [" + i + "] of [" + friendsMap.size() + "] relationships completed");
            }
            Map.Entry<String, Set<Person>> pair = (Map.Entry) it.next();
            String candidate = pair.getKey();
            Set<String> friendsSet = friendsMap.get(candidate);
            for (String s : friendsSet) {
                createRelationshipInTwoUsers(candidate, s);
            }
            i++;
        }
        System.out.println("Add Relationships to NEO4J: Done");
    }


    private void addGOTCharactersToDB(HashSet<String> characterSet) {
        for (String s : characterSet) {
            Node myNode = graphDB.createNode();
            myNode.addLabel(Labels.GOT_CHARACTERS);
            myNode.setProperty("name", s);
        }
        System.out.println("Adding GOT characters to NEO4J: Done");
    }

}
