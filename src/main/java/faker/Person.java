package faker;

/**
 * Created by jaspreet.kaur on 4/26/18.
 */
public class Person {
    String firstName;
    String lastName;
    String username;
    String title;
    String designation;
    String city;
    String companyName;
    String ssn;

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", designation='" + designation + '\'' +
                ", city='" + city + '\'' +
                ", companyName='" + companyName + '\'' +
                ", ssn='" + ssn + '\'' +
                '}';
    }

    public String toJSON() {
        return " { " +
                "firstName:'" + escapeMetaCharacters(firstName) + '\'' +
                ", lastName:'" + escapeMetaCharacters(lastName) + '\'' +
                ", username:'" + escapeMetaCharacters(username) + '\'' +
                ", title:'" + escapeMetaCharacters(title) + '\'' +
                ", designation:'" + escapeMetaCharacters(designation) + '\'' +
                ", city:'" + escapeMetaCharacters(city) + '\'' +
                ", companyName:'" + escapeMetaCharacters(companyName) + '\'' +
                ", ssn:'" + ssn + '\'' +
                '}';
    }

    public static String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","'","(",")",".","*","+","?","|","<",">","-","&"};
        String outputString=inputString;
        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                outputString = inputString.replace(metaCharacters[i],"");
                inputString = outputString;
            }
        }
        return outputString;
    }
}
