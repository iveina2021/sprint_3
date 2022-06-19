package sprint3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sprint3.helper.CourierRequestTestHelper;
import sprint3.serialization.CourierIdWrapper;
import sprint3.serialization.Order;
import sprint3.serialization.OrderRequest;
import sprint3.serialization.OrdersResponse;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static sprint3.helper.TestUtils.*;

public class GetOrderListApiTest {

    private CourierIdWrapper idWrapper;

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    @Test
    public void getOrderListReturnSuccess() {
        CourierRequestTestHelper.createCourierRequest(prepareCreateCourierRequest(USERNAME, PASSWORD, FIRST_NAME))
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        Response loginCourierResponse = CourierRequestTestHelper.loginCourierRequest(prepareLoginPasswordRequest(USERNAME, PASSWORD));

        loginCourierResponse
                .then()
                .statusCode(200)
                .body("id", notNullValue());

        idWrapper = loginCourierResponse.as(CourierIdWrapper.class);

        OrderRequest orderToCreate = prepareOrder();
        CourierRequestTestHelper.createNewOrderRequest(orderToCreate)
                .then()
                .statusCode(201)
                .body("track", notNullValue());

        Response getOrderListResponse = CourierRequestTestHelper.getOrderListRequest();

        getOrderListResponse
                .then()
                .statusCode(200);

        List<Order> orders = getOrderListResponse.as(OrdersResponse.class).getOrders();
        assertThat(orders).isNotEmpty();
    }

    private OrderRequest prepareOrder() {
        OrderRequest order = new OrderRequest();
        order.setFirstName("charlie");
        order.setLastName("mcdowell");
        order.setAddress("Mira, 142 apt.");
        order.setMetroStation(4);
        order.setPhone("+7 800 444 55 22");
        order.setRentTime(5);
        order.setDeliveryDate("2022-06-07");
        order.setComment("come back");
        order.setColor(singletonList("BLACK"));
        return order;
    }

    @After
    public void cleanUp() {
        CourierRequestTestHelper.deleteCourierRequest(idWrapper)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}




