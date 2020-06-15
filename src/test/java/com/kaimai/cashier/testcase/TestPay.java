package com.kaimai.cashier.testcase;

import com.alibaba.fastjson.JSONObject;
import com.kaimai.cashier.api.OrderApplication;
import com.kaimai.cashier.api.PayApplication;
import com.kaimai.cashier.api.VipApplication;
import io.qameta.allure.Description;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static com.kaimai.cashier.common.Util.template;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("测试商品收款相关业务")
public class TestPay extends TestUser{
    static ResponseSpecification responseSpec;
    static VipApplication vip = VipApplication.getInstance();
    PayApplication pay = PayApplication.getInstance();
    OrderApplication order = OrderApplication.getInstance();


    @BeforeAll
    @Description("支付成功测试用例的通用断言")
    static void beforeTestPay(){
        ResponseSpecBuilder respBuilder = new ResponseSpecBuilder();
        respBuilder.expectBody("result.success", equalTo(true));
        respBuilder.expectBody("data.orderStatus", equalTo("PAY_SUC"));
        respBuilder.expectBody("data.tradeType", equalTo("PAYMENT"));
        responseSpec = respBuilder.build();
    }

    static Stream<Arguments> payExp() {
//        String goodsDetail = "[{\"discountAmount\":0,\"discountPrice\":0,\"isDiscountPrice\":false,\"isTemporaryGoods\":false,\"isVipPrice\":false,\"productCategoryIdList0\":9993623,\"productCategoryIdList1\":9993624,\"productId\":999181571,\"productName\":\"果脯\",\"productSkuId\":999181613,\"saleCount\":\"1\",\"salePrice\":1300,\"saleUnit\":\"份\",\"skuId\":999305760,\"skuTitle\":\"果脯\",\"skuVersion\":1}]";
        return Stream.of(
                arguments(null, 4, 1300, 1300, "支付条码或支付渠道必选！"),
                arguments("Future",4, 1300, 1300, "请选择支付渠道"),
                arguments("ACCOUNTING",99999, 1300, 1300, "不支持的记账渠道"),
                arguments("CASH", 4, 1301, 1300, "实际支付金额大于订单总金额，请查证后再试！"),
                arguments("CASH", 4, 1300, 1400, "订单总金额与商品列表不一致，请查证"),
                arguments("CASH", 4, 1300, 1311, "订单总金额与商品列表不一致，请查证"),
                arguments("CASH", 4, 1300, 1401, "订单金额异常,不能完成结算，您可以清除购物车商品后，重新加购，若此问题重复出现，请联系客服小二处理")
        );
    }

    @DisplayName("支付异常测试")
    @ParameterizedTest(name="支付参数：渠道{0},渠道id{1}，支付金额{2},订单金额{3}")
    @Description("支付异常场景测试")
    @MethodSource("payExp")
    void testPayExp(String PaymentChannel,Integer channelId,
                    Integer payAmount, Integer totalAmount, String errMsg){
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是现金

//        data.put("goodsDetail",goodsDetail);
        data.put("paymentChannel", PaymentChannel);
        data.put("channelId", channelId);
        data.put("payAmount", payAmount);
        data.put("totalAmount", totalAmount);

        String body=template("/com/kaimai/cashier/testcase/payTemplateForExp.Json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.pay(params).
                then().
                    body("result.success", equalTo(false)).
                    body("result.errorMsg", equalTo(errMsg));

    }

    @Test
    @DisplayName("现金支付，无优惠")
    @Description("商品不享受优惠，现金支付")
    void testPayByCommonCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是现金
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        String body=template("/com/kaimai/cashier/testcase/payTemplate.json", data);
        JSONObject params = JSONObject.parseObject(body);
        pay.pay(params).then().
                spec(responseSpec).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchema.json"));
    }

    @Test
    @DisplayName("现金支付(找零)，享商品优惠")
    @Description("商品享受品类折扣，现金支付，找零")
    void testPayWithDiscountForCategoryByCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是现金
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDiscountForCategory.Json", data);
        JSONObject params = JSONObject.parseObject(body);
        pay.pay(params).then().
                spec(responseSpec).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchema.json"));
    }

    @Test
    @DisplayName("记账(学生卡)支付，享商品优惠和全场活动")
    @Description("商品享受品类折扣，订单享受全场活动(订单优惠包含享品类折扣商品)，现金支付")
    void testPayWithDiscountForCategoryAndOrderByCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是记账，校园卡
        data.put("paymentChannel", "ACCOUNTING");
        data.put("channelId", "47");
        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDiscountForCatgyAndOrder.json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.payWithDiscountForOrder(params).then().
                spec(responseSpec).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchema.json"));
    }

    @Test
    @DisplayName("现金支付(找零)，享商品优惠和订单优惠")
    @Description("商品享受单品折扣、单品优惠，订单享受全场活动、整单优惠(订单优惠包含享单品优惠商品)，现金支付")
    void testPayWithDiscountForEvyGoodAndOrderByCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是现金
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDiscountForEvyGoodAndOrder.json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.payWithDiscountForOrder(params).then().spec(responseSpec);
    }

    @Test
    @DisplayName("现金支付，享商品优惠与整单优惠")
    @Description("商品享受单品优惠，订单享受整单优惠，现金支付")
    void testPayWithDiscountForEveryGoodAndOrderByCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是记账，银行卡
        data.put("paymentChannel", "ACCOUNTING");
        data.put("channelId", "48");
        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDiscountForEveryGoodAndOrder.Json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.pay(params).then().
                spec(responseSpec).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchema.json"));
    }

    @Test
    @DisplayName("现金支付，享商品折扣与全场活动")
    @Description("商品价格改高、改低，订单享全场活动，现金支付")
    void testPayWithDiscountForGoodsAndOrderByCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是现金
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDiscountForGoodsAndOrder.Json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.payWithDiscountForOrder(params).then().
                spec(responseSpec).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchema.json"));
    }


    @Test
    @DisplayName("会员现金支付，有会员优惠")
    @Description("商品享受会员优惠(会员价)，会员现金支付")
    void testPayByCashForVip() {
        HashMap<String, Object> data = new HashMap<>();
        String vipPayToken = "";
        String vipCardNo = vip.getVipCardNo();
        try {
            vipPayToken = vip.getDetailOfVip(vipCardNo).then().extract().path("data.vipPayToken");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //会员现金支付
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        data.put("vipCardNo", vipCardNo);
        data.put("vipPayToken", vipPayToken);

        String body=template("/com/kaimai/cashier/testcase/payTemplateForVip.Json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.payForVip(params).then().
                spec(responseSpec).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchemaForVip.json"));

    }


    @Test
    @DisplayName("会员余额支付，有会员优惠和单品优惠")
    @Description("商品享受单品优惠和会员优惠(会员价)，会员余额支付")
    void testPayWithDiscountForEveryGoodAndOrderByCardForVip() {
        HashMap<String, Object> data = new HashMap<>();
        String vipPayToken = "";
        String vipCardNo = vip.getVipCardNo();
        try {
            vipPayToken = vip.getDetailOfVip(vipCardNo).then().extract().path("data.vipPayToken");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //会员卡余额支付
        data.put("paymentChannel", "VIPCARD");
        data.put("channelId", "0");
        data.put("vipCardNo", vipCardNo);
        data.put("vipPayToken", vipPayToken);

        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDistForEvyGoodAndOrderForVip.json", data);
        JSONObject params = JSONObject.parseObject(body);
        pay.payForVip(params).then().
                spec(responseSpec).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchemaForVip.json"));
    }

    @Test
    @DisplayName("会员现金支付，有会员优惠和单品优惠")
    @Description("商品享受单品优惠和会员优惠(会员价)，会员现金支付")
    void testPayWithDiscountForEveryGoodAndOrderByCashForVip() {
        HashMap<String, Object> data = new HashMap<>();
        String vipPayToken = "";
        String vipCardNo = vip.getVipCardNo();
        try {
            vipPayToken = vip.getDetailOfVip(vipCardNo).then().extract().path("data.vipPayToken");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //会员现金支付
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        data.put("vipCardNo", vipCardNo);
        data.put("vipPayToken", vipPayToken);

        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDistForEvyGoodAndOrderForVip.json", data);
        JSONObject params = JSONObject.parseObject(body);
        pay.payForVip(params).then().
                spec(responseSpec).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchemaForVip.json"));
    }

    @Test
    @DisplayName("会员现金支付，有商品优惠和会员优惠，查询订单详情")
    @Description("商品享受单品优惠和会员优惠(会员价、会员折和积分)，会员现金支付")
    void testPayWithDiscountForEveryGoodAndPointForVip(){
        HashMap<String, Object> data = new HashMap<>();
        String vipPayToken = "";
        String vipCardNo = vip.getVipCardNo();
        try {
            vipPayToken = vip.getDetailOfVip(vipCardNo).then().extract().path("data.vipPayToken");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //会员现金支付
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        data.put("vipCardNo", vipCardNo);
        data.put("vipPayToken", vipPayToken);

        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDistForEvyGoodAndPointForVip.json", data);
        JSONObject params = JSONObject.parseObject(body);

        String orderNo = pay.payForVip(params).then().
                spec(responseSpec).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchemaForVip.json")).
                extract().path("data.orderNo");

        //查询订单详情
        order.getDetailOfOrder(orderNo).
                then().body("result.success", equalTo(true)).
                body("data.orderInfo.orderNo", equalTo(orderNo)).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/orderForPaySchema.json"));
    }

    // TODO: 2020/5/19 接口v1/vip/promotion/check/v2 和 /v1/vip/promotion/point进行接口测试



}
