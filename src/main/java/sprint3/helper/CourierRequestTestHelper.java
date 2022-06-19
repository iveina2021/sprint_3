package sprint3.helper;

import io.restassured.response.Response;
import sprint3.serialization.CourierIdWrapper;
import sprint3.serialization.OrderRequest;

import static io.restassured.RestAssured.given;
import static sprint3.helper.TestUtils.prepareLoginPasswordRequest;

public class CourierRequestTestHelper {

    public static Response createCourierRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    public static Response loginCourierRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }

    public static Response deleteCourierRequest(CourierIdWrapper id) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(id)
                .when()
                .delete("/api/v1/courier/" + id.getId());
    }

    public static Response createNewOrderRequest(OrderRequest order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    public static Response getOrderListRequest() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/v1/orders");
    }

    public static void deleteCourierIfExistsWithRequest(String username, String password){
        Response response = CourierRequestTestHelper.loginCourierRequest(prepareLoginPasswordRequest(username, password));
        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            CourierIdWrapper idWrapper = response.as(CourierIdWrapper.class);

            CourierRequestTestHelper.deleteCourierRequest(idWrapper)
                    .then()
                    .statusCode(200);
        }
    }
}
