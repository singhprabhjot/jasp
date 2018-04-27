import embeded.LoadData;
import embeded.Queries;

/**
 * Created by jaspreet.kaur on 4/26/18.
 * Run this class to load fake data
 * and run the queries
 */
public class Main {
    public static void main(String[] args) {
        LoadData loadData = new LoadData();
        loadData.loadPersonData(10000, 50); //This will load fake data.

        // Following commands will run the queries.
        Queries queries = new Queries();
        queries.runQueryForGOTCharacters();
        queries.runQueryToFindUsers();
        queries.getAverageRating();
        queries.getCharactersAUserHasLiked();
        queries.gotCharactersThatMyFriendsHaveLiked();

    }

}
