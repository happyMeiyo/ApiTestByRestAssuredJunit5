package com.kaimai.cashier.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaimai.cashier.api.OrderApplication;
import com.kaimai.cashier.api.PayApplication;
import com.kaimai.cashier.api.VipApplication;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("测试会员充值相关业务")
public class TestCharge extends TestUser{
    static VipApplication vip = VipApplication.getInstance();
    OrderApplication order = OrderApplication.getInstance();
    PayApplication pay = new PayApplication();

    static Stream<Arguments> chargeForVipExp() {
        String vipCardNo = vip.getVipCardNo();
        return Stream.of(
                arguments("VIPCARD", vipCardNo, 100, 100, "不支持的交易渠道"),
                arguments("ALIPAY", null,100, 100, "请选择要充值的会员！"),
                arguments(null, vipCardNo,100, 100, "支付条码或支付渠道必选！"),
                arguments("WECHAT", vipCardNo,99, 100, "充值不支持部分付款"),
                arguments("CASH", vipCardNo,100, 99, "实际支付金额大于订单总金额，请查证后再试！"),
                arguments("CASH", vipCardNo,0, 0, "订单金额应该大于0")
        );
    }


    @Order(1)
    @DisplayName("会员充值异常测试")
    @ParameterizedTest(name="会员充值：渠道{0}，会员卡{1},支付金额{2}，充值金额{3}")
    @Description("会员充值异常场景测试")
    @MethodSource("chargeForVipExp")
    void testChargeByCashForVipExp(String channel, String vipCardNo, Integer payAmount, Integer totalAmount, String errMsg){
        pay.chargeForVip(vipCardNo, channel, payAmount, totalAmount).
                then().body("result.success", equalTo(false)).
                       body("result.errorMsg", equalTo(errMsg));

    }

    @Order(50)
    @DisplayName("会员现金充值成功")
    @ParameterizedTest(name="会员{0}充值：{1}")
    @Description("会员现金充值成功")
    @CsvSource({"CASH,50000"})
    void testChargeByCashForVip(String channel, Integer amount){
        Map<String, Object> chargeInfo = pay.chargeForVip(vip.getVipCardNo(), channel, amount, amount).
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
        Map<String, Object> orderInfo = order.getDetailOfOrder(order.getOrderNo()).
                then().body("result.success", equalTo(true)).
                       body("data.orderInfo.orderNo", equalTo(order.getOrderNo())).
                extract().jsonPath().getMap("data");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(orderInfo);

        assertThat("数据格式正确", json, matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/orderSchema.json"));
    }

}
