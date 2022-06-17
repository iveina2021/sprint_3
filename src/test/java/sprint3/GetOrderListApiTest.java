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
import static sprint3.helper.TestConstants.SCOOTER_URL;
import static sprint3.helper.TestConstants.USERNAME;

public class GetOrderListApiTest {

    private static final String CREATE_COURIER_REQUEST = "{\"login\": \"" + USERNAME + "\", \"password\": \"199919\", \"firstName\": \"charlie\"}";

    private static final String LOGIN_COURIER = "{\"login\": \"" + USERNAME + "\", \"password\": \"199919\"}";

    private CourierIdWrapper idWrapper;

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    @Test
    public void getOrderListReturnSuccess() {
        createCourier()
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        CourierRequestTestHelper.loginCourierRequestHelper(LOGIN_COURIER)
                .then()
                .statusCode(200)
                .body("id", notNullValue());

        idWrapper = CourierRequestTestHelper.loginCourierRequestHelper(LOGIN_COURIER).as(CourierIdWrapper.class);

        OrderRequest orderToCreate = prepareOrder();
        CourierRequestTestHelper.createNewOrderRequestHelper(orderToCreate)
                .then()
                .statusCode(201)
                .body("track", notNullValue());

        CourierRequestTestHelper.getOrderList()
                .then()
                .statusCode(200);

        OrdersResponse ordersResponse = CourierRequestTestHelper.getOrderList().as(OrdersResponse.class);
        List<Order> orders = ordersResponse.getOrders();
        assertThat(orders).isNotEmpty();
    }

    private Response createCourier() {
        return CourierRequestTestHelper.createCourierRequestHelper(CREATE_COURIER_REQUEST);
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
        CourierRequestTestHelper.deleteCourierRequestHelper(idWrapper)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}




