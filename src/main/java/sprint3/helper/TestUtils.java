package sprint3.helper;

public class TestUtils {
    public static String USERNAME = "Ted Mosby Architect 43";
    public static final String PASSWORD = "199919";
    public static final String FIRST_NAME = "TED";

    public static String SCOOTER_URL = "http://qa-scooter.praktikum-services.ru";

    public static String prepareCreateCourierRequest(String username, String password, String firstName){
        return "{\"login\": \"" + username + "\", \"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\"}";
    }

    public static String prepareLoginPasswordRequest(String username, String password){
        return "{\"login\": \"" + username + "\", \"password\": \"" + password + "\"}";
    }

}


