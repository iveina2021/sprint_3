package sprint3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sprint3.helper.CourierRequestTestHelper;
import sprint3.serialization.CourierIdWrapper;

import static org.hamcrest.Matchers.equalTo;
import static sprint3.helper.TestConstants.SCOOTER_URL;
import static sprint3.helper.TestConstants.USERNAME;

public class CreateNewCourierApiTest {

    private static final String CREATE_COURIER_REQUEST = "{\"login\": \"" + USERNAME + "\", \"password\": \"199919\", \"firstName\": \"charlie\"}";

    private static final String CREATE_COURIER_WITHOUT_LOGIN_REQUEST = "{\"login\": \"\", \"password\": \"199919\", \"firstName\": \"charlie\"}";

    private static final String CREATE_COURIER_WITHOUT_PASSWORD_REQUEST = "{\"login\": \"" + USERNAME + "\", \"password\": \"\", \"firstName\": \"charlie\"}";

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    @Test
    public void createNewCourierReturnSuccess() {
        CourierRequestTestHelper.createCourierRequestHelper(CREATE_COURIER_REQUEST)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    public void createDoubleCourierReturnError() {
        CourierRequestTestHelper.createCourierRequestHelper(CREATE_COURIER_REQUEST)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        CourierRequestTestHelper.createCourierRequestHelper(CREATE_COURIER_REQUEST)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void createCourierWithoutLoginReturnError() {
        CourierRequestTestHelper.createCourierRequestHelper(CREATE_COURIER_WITHOUT_LOGIN_REQUEST)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createCourierWithoutPasswordReturnError() {
        CourierRequestTestHelper.createCourierRequestHelper(CREATE_COURIER_WITHOUT_PASSWORD_REQUEST)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
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