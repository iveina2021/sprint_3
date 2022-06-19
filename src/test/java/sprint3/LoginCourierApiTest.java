package sprint3;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static sprint3.helper.CourierRequestTestHelper.*;
import static sprint3.helper.TestUtils.*;

public class LoginCourierApiTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
        createCourierRequest(prepareCreateCourierRequest(USERNAME, PASSWORD, FIRST_NAME))
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    public void loginCourierReturnSuccess() {
        loginCourierRequest(prepareLoginPasswordRequest(USERNAME, PASSWORD))
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    public void loginCourierWithoutLoginReturnError() {
        loginCourierRequest(prepareLoginPasswordRequest("", PASSWORD))
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginCourierWithoutPasswordReturnError() {
        loginCourierRequest(prepareLoginPasswordRequest(USERNAME, ""))
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginCourierWithIncorrectLoginReturnError() {
        loginCourierRequest(prepareLoginPasswordRequest("Missing username", PASSWORD))
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginCourierWithIncorrectPasswordReturnError() {
        loginCourierRequest(prepareLoginPasswordRequest(USERNAME, "error_password"))
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void cleanUp() {
        deleteCourierIfExistsWithRequest(USERNAME, PASSWORD);
    }


}