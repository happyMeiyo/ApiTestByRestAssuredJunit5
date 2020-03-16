package com.kaimai.cashier.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.kaimai.cashier.api.OrderApplication;
import com.kaimai.cashier.api.PayApplication;
import com.kaimai.cashier.api.VipApplication;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("支付相关业务测试")
public class TestPay extends TestUser{

    static VipApplication vip = VipApplication.getInstance();
    OrderApplication order = OrderApplication.getInstance();
    PayApplication pay = new PayApplication();

    @Order(1)
    @DisplayName("会员现金充值成功")
    @ParameterizedTest(name="会员{0}充值：{1}")
    @Description("会员现金充值成功")
    @CsvSource({"CASH,100"})
    void testChargeByCashForVip(String channel, Integer amount) throws IOException {
        Map<String, Object> chargeInfo = pay.chargeForVip(vip.getVipCardNo(), channel, amount).
                then().body("result.success", equalTo(true)).
                extract().jsonPath().getMap("data");

        assertAll("chargeInfo",
                () -> assertEquals("PAY_SUC", chargeInfo.get("orderStatus")),
                () -> assertEquals("CHARGE", chargeInfo.get("tradeType")),
                () -> assertEquals(vip.getVipCardNo(), chargeInfo.get("vipCardNo")),
                () -> assertEquals(Collections.singletonList(channel), chargeInfo.get("paymentChannels")),
                () -> assertEquals(amount, chargeInfo.get("receiveAmount")),
                () -> {
                    ArrayList<Map<String, Object>> payList = (ArrayList<Map<String, Object>>) chargeInfo.get("payList");
                    assertAll("payList",
                            () -> assertEquals("PAYMENT", payList.get(0).get("tradeType")),
                            () -> assertEquals(amount, payList.get(0).get("receiveAmount")),
                            () -> assertEquals(channel, payList.get(0).get("paymentChannel")),
                            () -> assertEquals("PAY_SUC", payList.get(0).get("orderStatus"))
                    );

                }
        );

        // orderNo写入yaml文件
//        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//        Map<String, Object> orderNo= new HashMap<>();
//        orderNo.put("orderNo", chargeInfo.get("orderNo").toString());
//        String filePath = System.getProperty("user.dir") +
//                "\\src\\test\\resources\\com\\kaimai\\cashier\\testcase\\";
//        System.out.println(filePath);
//        mapper.writeValue(new File(filePath + "order.yml"), orderNo);
        order.setOrderNo(chargeInfo.get("orderNo").toString());
    }

    @Order(100)
    @Test
    @DisplayName("获取充值订单的详情")
    @Description("获取充值订单的详情成功")
    void testDetailOfOrder() throws JsonProcessingException {
        String orderNO = order.getOrderNo();
        Map<String, Object> orderInfo = order.getDetailOfOrder(order.getOrderNo()).
                then().body("result.success", equalTo(true)).
                body("data.orderInfo.orderNo", equalTo(order.getOrderNo())).
                extract().jsonPath().getMap("data");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(orderInfo);

        assertThat("数据格式正确", json, matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/orderSchema.json"));
    }


}
