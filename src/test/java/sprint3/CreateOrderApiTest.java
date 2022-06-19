package sprint3;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sprint3.helper.CourierRequestTestHelper;
import sprint3.serialization.OrderRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static sprint3.helper.TestUtils.SCOOTER_URL;

@RunWith(Parameterized.class)
public class CreateOrderApiTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Arrays.asList()}
        });
    }

    private final List<String> color;

    public CreateOrderApiTest(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    @Test
    public void createNewOrderReturnSuccess() {
        OrderRequest order = new OrderRequest();
        order.setFirstName("charlie");
        order.setLastName("mcdowell");
        order.setAddress("Mira, 142 apt.");
        order.setMetroStation(4);
        order.setPhone("+7 800 444 55 22");
        order.setRentTime(5);
        order.setDeliveryDate("2022-06-07");
        order.setComment("come back");
        order.setColor(color);

        CourierRequestTestHelper.createNewOrderRequest(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}

