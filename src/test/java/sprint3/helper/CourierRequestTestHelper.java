package sprint3.helper;

import io.restassured.response.Response;
import sprint3.serialization.CourierIdWrapper;
import sprint3.serialization.OrderRequest;

import static io.restassured.RestAssured.given;

public class CourierRequestTestHelper {

    public static Response createCourierRequestHelper(String json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    public static Response loginCourierRequestHelper(String json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }

    public static Response deleteCourierRequestHelper(CourierIdWrapper id) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(id)
                .when()
                .delete("/api/v1/courier/" + id.getId());
    }

    public static Response createNewOrderRequestHelper(OrderRequest order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    public static Response getOrderList() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/v1/orders");
    }
}
