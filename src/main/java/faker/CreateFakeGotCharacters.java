package faker;

import com.github.javafaker.Faker;

import java.util.HashSet;

/**
 * Created by jaspreet.Kaur on 4/26/18.
 */
public class CreateFakeGotCharacters {

    public static HashSet<String> gOTCharacterSet = new HashSet<>();

    /**
     * Function to create Game of Thrones characters
     * by taking input of how many characters are required.
     * @param gOTCharactersCount
     */
    public static void createGOTCharacters(int gOTCharactersCount) {
        Faker faker = new Faker();
        for (int i = 0; i < gOTCharactersCount; i++) {
            gOTCharacterSet.add(faker.gameOfThrones().character());
        }
    }

}
