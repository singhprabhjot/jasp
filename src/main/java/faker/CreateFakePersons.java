package faker;

import com.github.javafaker.Faker;

import java.util.*;

/**
 * Created by jaspreet.kaur on 4/26/18.
 * This class is used to create fake person data.
 */

public class CreateFakePersons {
    Faker faker = new Faker();

    // Number of People you would like to create in the database
    int maxNumberOfPersons;
    int strengthOfRelationships = 3;

    public CreateFakePersons(int maxNumberOfPersons) {
        this.maxNumberOfPersons = maxNumberOfPersons;
    }

    public static Map<String, Person> persons = new HashMap<String, Person>();
    public static List<Person> personsList = new ArrayList<Person>();
    Random rand = new Random();
    public static Map<String, Set<String>> friendsMap = new HashMap<String, Set<String>>();

    /**
     * Function to generate fake data and populate in Person object.
     * @return
     */
    public Person createPerson() {
        Person person = new Person();
        person.setFirstName(faker.name().firstName());
        person.setLastName(faker.name().lastName());
        person.setCity(faker.address().fullAddress());
        person.setCompanyName("Golden Sparrow");
        person.setTitle(faker.job().title());
        person.setDesignation(faker.job().position());
        person.setSsn(faker.idNumber().ssnValid());
        String empUserName = faker.name().username();
        empUserName = empUserName.replace(".", "");
        person.setUsername(empUserName);
        return person;
    }

    /**
     * This function runs a loop to generate persons and put them in MAP and List
     */
    public void createFriends() {
        System.out.println("Creating Friends");
        int n = this.maxNumberOfPersons;
        for (int i = 0; i < n; i++) {
            Person person = createPerson();
            if (!persons.containsKey(person.getUsername())) {
                persons.put(person.getUsername(), person);
                personsList.add(person);
            }
        }
    }

    /**
     *  This function loops through fake data generated to
     *  generate relationships.
     */
    public void createRelationship() {
        System.out.println("Creating relationships");
        //Traversing the loop  to create more relationships
        for (int j = 0; j < strengthOfRelationships; j++) {
            for (int i = 0; i < personsList.size(); i++) {
                int friend1 = getRandomFriend();
                int friend2 = getRandomFriend();
                while (friend1 == friend2) {
                    friend2 = getRandomFriend();
                }

                // Skiping few usernames so as to have mixed type of graph.
                try {
                    if ((personsList.get(friend1).getUsername().startsWith("a")) || personsList.get(friend2).getUsername().startsWith("a")) {
                        continue;
                    } else {
                        addToFriendList(friend1, friend2);
                        addToFriendList(friend2, friend1);
                    }
                } catch (Exception e) {
                }

            }
        }
    }

    /**
     * Function to create relationship between two friends
     * @param friend2
     * @param friend1
     */
    private void addToFriendList(int friend2, int friend1) {
        String friend1Username = personsList.get(friend1).getUsername();
        if (friendsMap.containsKey(friend1Username)) {
            Set<String> friendsSet = friendsMap.get(friend1Username);
            friendsSet.add(personsList.get(friend2).getUsername());
        } else {
            Set<String> friendsSet = new HashSet<String>();
            friendsSet.add(personsList.get(friend2).getUsername());
            friendsMap.put(friend1Username, friendsSet);
        }
    }

    /**
     * Function to get a random friends index
     * @return
     */
    private int getRandomFriend() {
        return rand.nextInt(maxNumberOfPersons - 1) + 1;
    }

    /**
     * Ignore this method. Used to manual testing of the class only.
     * @param args
     */
    public static void main(String[] args) {
        CreateFakePersons fakeData = new CreateFakePersons(100);
        fakeData.createFriends();
        fakeData.createRelationship();
        System.out.println();
    }


}
