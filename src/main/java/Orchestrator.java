import faker.CreateFakePersons;

/**
 * Created by jaspreet.kaur on 4/26/18.
 */
public class Orchestrator {
    DBHelper dbHelper;

    public Orchestrator(){
        dbHelper=new DBHelper();
    }
    public static void main(String[] args) {
        Orchestrator orchestrator = new Orchestrator();
        orchestrator.runMe();
    }

    private void runMe() {
        createFakeData();
        dbHelper.deleteLabelIfExists();
        dbHelper.addFriendsToDB(CreateFakePersons.personsList);
        dbHelper.createIndexOnField("username");
        dbHelper.createFriendsRelationShip(CreateFakePersons.friendsMap);
    }



    private void createFakeData() {
        CreateFakePersons fakeData = new CreateFakePersons(50);
        fakeData.createFriends();
        fakeData.createRelationship();
    }
}
