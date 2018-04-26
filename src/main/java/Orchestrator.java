import faker.CreateFakeData;

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
        dbHelper.addFriendsToDB(CreateFakeData.personsList);
        dbHelper.createIndexOnField("username");
        dbHelper.createFriendsRelationShip(CreateFakeData.friendsMap);
    }



    private void createFakeData() {
        CreateFakeData fakeData = new CreateFakeData(50);
        fakeData.createFriends();
        fakeData.createRelationship();
    }
}
