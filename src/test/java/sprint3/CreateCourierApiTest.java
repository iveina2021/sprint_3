package sprint3;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sprint3.helper.CourierRequestTestHelper;

import static org.hamcrest.Matchers.equalTo;
import static sprint3.helper.TestUtils.*;

public class CreateCourierApiTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    @Test
    public void createNewCourierReturnSuccess() {
        CourierRequestTestHelper.createCourierRequest(prepareCreateCourierRequest(USERNAME, PASSWORD, FIRST_NAME))
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    /**
     * Также должен заметить, что ты случайно пропустила реализовать тест на это требование:
     * "если создать пользователя с логином, который уже есть, возвращается ошибка".
     */
    @Test
    public void createTwoIdenticalCouriersReturnError() {
        CourierRequestTestHelper.createCourierRequest(prepareCreateCourierRequest(USERNAME, PASSWORD, FIRST_NAME))
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        CourierRequestTestHelper.createCourierRequest(prepareCreateCourierRequest(USERNAME, PASSWORD, FIRST_NAME))
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void createCourierWithoutLoginReturnError() {
        CourierRequestTestHelper.createCourierRequest(prepareCreateCourierRequest("", PASSWORD, FIRST_NAME))
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createCourierWithoutPasswordReturnError() {
        CourierRequestTestHelper.createCourierRequest(prepareCreateCourierRequest(USERNAME, "", FIRST_NAME))
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void cleanUp() {
        CourierRequestTestHelper.deleteCourierIfExistsWithRequest(USERNAME, PASSWORD);
    }
}