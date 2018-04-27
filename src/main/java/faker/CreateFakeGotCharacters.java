package faker;

import com.github.javafaker.Faker;
import utilities.Constants;

import java.util.HashSet;

/**
 * Created by jaspreet.Kaur on 4/26/18.
 */
public class CreateFakeGotCharacters {

    public static HashSet<String> gOTCharacterSet = new HashSet<>();

    public static void createGOTCharacters() {
        Faker faker = new Faker();
        for (int i = 0; i < Constants.MAX_NUMBER_OF_GOT_CHARACTERS; i++) {
            gOTCharacterSet.add(faker.gameOfThrones().character());
        }
    }

}
