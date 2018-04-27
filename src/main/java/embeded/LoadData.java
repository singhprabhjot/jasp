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


    public void loadPersonData(int numberOfPerson) {
        Person p = new Person();

        Transaction tx = null;
        tx = graphDB.beginTx();

        CreateFakePersons fakeData = new CreateFakePersons(numberOfPerson);
        fakeData.createFriends();
        fakeData.createRelationship();
        addFriendsToDB(CreateFakePersons.personsList);
        createFriendsRelationShip(CreateFakePersons.friendsMap);
        CreateFakeGotCharacters.createGOTCharacters();
        addGOTCharactersToDB(CreateFakeGotCharacters.gOTCharacterSet);

//        Node steve = graphDB.createNode(Labels.USER);
//        steve.setProperty("name", "Steve");
//        Node linda = graphDB.createNode(Labels.USER);
//        linda.setProperty("name", "Linda");
//        Node michael = graphDB.createNode(Labels.USER);
//        michael.setProperty("name", "Michael");
//        Node rebecca = graphDB.createNode(Labels.USER);
//        rebecca.setProperty("name", "Rebecca");
//
//
//        steve.createRelationshipTo(michael, RelationshipTypes.IS_FRIEND);
//        steve.createRelationshipTo(rebecca, RelationshipTypes.IS_FRIEND);
//        steve.createRelationshipTo(linda, RelationshipTypes.IS_FRIEND);
//

//        Node divergent = graphDB.createNode(Labels.GOT_CHARACTERS);
//        divergent.setProperty("name", "Divergent");
//        Node hero = graphDB.createNode(Labels.GOT_CHARACTERS);
//        hero.setProperty("name", "Big Hero 6");
//        Node cinderella = graphDB.createNode(Labels.GOT_CHARACTERS);
//        cinderella.setProperty("name", "Cinderella");
//        Node interview = graphDB.createNode(Labels.GOT_CHARACTERS);
//        interview.setProperty("name", "The Interview");


//        likeGotCharacter(steve, divergent, 5);
//        likeGotCharacter(steve, hero, 5);
//        likeGotCharacter(steve, cinderella, 4);
//        likeGotCharacter(rebecca, hero, 5);
//        likeGotCharacter(rebecca, cinderella, 5);
//        likeGotCharacter(michael, hero, 5);
//        likeGotCharacter(michael, cinderella, 3);
//        likeGotCharacter(linda, divergent, 4);
//        likeGotCharacter(linda, hero, 5);
//        likeGotCharacter(linda, cinderella, 5);
        createLinkageBetweenPersonAndGOTCharacters();
        tx.success();



    }

    private void createLinkageBetweenPersonAndGOTCharacters() {
        List<Person> personsList = CreateFakePersons.personsList;
        for (int i = 0; i < personsList.size(); i++) {
            String username = personsList.get(i).getUsername();
            for (int j = 0; j < Constants.MOVIES_PER_PERSON; j++) {
                String gotCharacter = getRandomGOTCharacter();
                likeGotCharacter(username, gotCharacter, getRandomLikeScore());
            }
        }
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
        System.out.println("Creating Relationships: Started");
        Iterator it = friendsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Set<Person>> pair = (Map.Entry) it.next();
            String candidate = pair.getKey();
            Set<String> friendsSet = friendsMap.get(candidate);
            for (String s : friendsSet) {
                createRelationshipInTwoUsers(candidate, s);
            }
        }
        System.out.println("Create Relationships: Done");
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
