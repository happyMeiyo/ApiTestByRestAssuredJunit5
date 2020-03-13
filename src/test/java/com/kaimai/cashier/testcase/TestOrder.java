package com.kaimai.cashier.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaimai.cashier.api.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("测试订单相关业务")
public class TestOrder extends TestUser{
    Order order = new Order();
    static String orderNo;

    @BeforeAll
    static void getOrderNo(){
        orderNo = "2640141543135";
    }

    @Test
    void testDetailOfOrder() throws JsonProcessingException { ;
        Map<String, Object> orderInfo = order.getDetailOfOrder(orderNo).
                then().body("result.success", equalTo(true)).
                       body("data.orderInfo.orderNo", equalTo(orderNo)).
                extract().jsonPath().getMap("data");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(orderInfo);

        assertThat("数据格式正确", json, matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/orderSchema.json"));
    }
}