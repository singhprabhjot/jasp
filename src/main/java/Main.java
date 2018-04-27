import embeded.LoadData;
import embeded.Queries;

/**
 * Created by jaspreet.kaur on 4/26/18.
 */
public class Main {
    public static void main(String[] args) {
        LoadData loadData=new LoadData();
        loadData.loadPersonData(10000,50);

        Queries queries=new Queries();
        queries.runQueryForGOTCharacters();
        queries.runQueryToFindUsers();
        queries.getAverageRating();
        queries.getCharactersAUserHasLiked();
        queries.gotCharactersThatMyFriendsHaveLiked();

    }

}
