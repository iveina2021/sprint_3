package sprint3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sprint3.helper.CourierRequestTestHelper;
import sprint3.serialization.CourierIdWrapper;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static sprint3.helper.TestConstants.SCOOTER_URL;
import static sprint3.helper.TestConstants.USERNAME;

public class LoginCourierApiTest {

    public static final String LOGIN_COURIER = "{\"login\": \"" + USERNAME + "\", \"password\": \"199919\"}";

    public static final String WITHOUT_LOGIN_COURIER_LOGIN = "{\"login\": \"\", \"password\": \"199919\"}";

    public static final String WITHOUT_PASSWORD_COURIER_LOGIN = "{\"login\": \"Ted Mosby\", \"password\": \"\"}";

    public static final String LOGIN_COURIER_WITH_INCORRECT_LOGIN = "{\"login\": \"Tom Mosby\", \"password\": \"199919\"}";

    public static final String LOGIN_COURIER_WITH_INCORRECT_PASSWORD = "{\"login\": \"Ted Mosby\", \"password\": \"19991900\"}";

    private static final String CREATE_COURIER_REQUEST = "{\"login\": \"" + USERNAME + "\", \"password\": \"199919\"}";

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    @Test
    public void loginCourierReturnSuccess() {

        CourierRequestTestHelper.createCourierRequestHelper(CREATE_COURIER_REQUEST)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        CourierRequestTestHelper.loginCourierRequestHelper(LOGIN_COURIER)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    public void loginCourierWithoutLoginReturnError() {
        CourierRequestTestHelper.loginCourierRequestHelper(WITHOUT_LOGIN_COURIER_LOGIN)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginCourierOnlyWithoutPasswordReturnError() {
        CourierRequestTestHelper.loginCourierRequestHelper(WITHOUT_PASSWORD_COURIER_LOGIN)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginCourierWithIncorrectLoginReturnError() {
        CourierRequestTestHelper.loginCourierRequestHelper(LOGIN_COURIER_WITH_INCORRECT_LOGIN)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginCourierWithIncorrectPasswordReturnError() {
        CourierRequestTestHelper.loginCourierRequestHelper(LOGIN_COURIER_WITH_INCORRECT_PASSWORD)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void cleanUp() {
        Response response = CourierRequestTestHelper.loginCourierRequestHelper(CREATE_COURIER_REQUEST);
        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            CourierIdWrapper idWrapper = response.as(CourierIdWrapper.class);

            CourierRequestTestHelper.deleteCourierRequestHelper(idWrapper)
                    .then()
                    .statusCode(200);
        }
    }
}