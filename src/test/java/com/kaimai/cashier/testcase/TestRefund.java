package com.kaimai.cashier.testcase;

import com.alibaba.fastjson.JSONObject;
import com.kaimai.cashier.api.OrderApplication;
import com.kaimai.cashier.api.PayApplication;
import com.kaimai.cashier.api.VipApplication;
import com.kaimai.cashier.common.User;
import io.qameta.allure.Description;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.kaimai.util.Util.template;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("测试退款相关业务")
public class TestRefund extends TestUser{
    static PayApplication pay = PayApplication.getInstance();
    static User cashier = User.getInstance();
    OrderApplication order = OrderApplication.getInstance();
    static VipApplication vip = VipApplication.getInstance();

    static Stream<Arguments> refundWithOprPwdExp() {
        return Stream.of(
                arguments("", "2650069415038", "操作密码必填"),
                arguments(null, "2650069415038", "操作密码错误"),
                arguments("99999", "2650069415038", "操作密码错误")
        );
    }

    @DisplayName("退款操作密码异常")
    @ParameterizedTest(name="退款异常：操作密码：{0}, 订单号{1}")
    @Description("退款异常场景测试")
    @MethodSource("refundWithOprPwdExp")
    void testRefundExp(String operatePwd, String orderNo, String errMsg){
        HashMap<String, Object> refundData = new HashMap<>();
        refundData.put("orderNo", orderNo);
        refundData.put("refundAmount", 1);
        refundData.put("operatePwd", operatePwd);

        pay.refund(refundData).then().
                body("result.success", equalTo(false)).
                body("result.errorMsg", equalTo(errMsg));

    }

    @DisplayName("获取应退款的支付列表异常")
    @ParameterizedTest(name="获取支付交易列表：订单号{0}")
    @Description("应退款的支付列表异常场景测试")
    @NullAndEmptySource
    void testGetPayListForRefundExp(String orderNo){
        HashMap<String, Object> refundData = new HashMap<>();
        refundData.put("orderNo", orderNo);
        refundData.put("refundAmount", 1);

        pay.getRefundListForPays(refundData).
                then().body("result.success", equalTo(false)).
                body("result.errorMsg", equalTo("订单状态异常，请查证"));

    }

    @Test
    @DisplayName("现金支付，全额退款，不退货")
    @Description("商品不享受优惠，现金支付，全额退款，不退货")
    void testFullRefundWithNonReturn() {
        HashMap<String, Object> payData = new HashMap<>();
        //支付方式是现金
        payData.put("paymentChannel", "CASH");
        payData.put("channelId", "4");
        String body=template("/com/kaimai/cashier/testcase/payTemplate.json", payData);
        JSONObject params = JSONObject.parseObject(body);
        Map<String, Object> orderInfo = pay.pay(params).then().
                body("result.success", equalTo(true)).
                body("data.orderStatus", equalTo("PAY_SUC")).
                body("data.tradeType", equalTo("PAYMENT")).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchema.json")).
                extract().jsonPath().getMap("data");


        HashMap<String, Object> refundData = new HashMap<>();
        refundData.put("orderNo", orderInfo.get("orderNo"));
        refundData.put("refundAmount", orderInfo.get("receiveAmount"));

        //获取支付交易的id和退款金额
        List<Map<String, Object>> refundInfoList = pay.getRefundListForPays(refundData).then().
                body("result.success",equalTo(true)).
                extract().jsonPath().getList("data");

        //获取退款订单的支付交易id和退款金额
        ArrayList<String> payDetail = new ArrayList<>();
        refundInfoList.forEach(item -> {
            Map<String, Object> refundInfo = new HashMap<>();
            refundInfo.put("id", item.get("id"));
            refundInfo.put("refundAmount", item.get("refundAmount"));
            payDetail.add(JSONObject.toJSONString(refundInfo));
        });
        refundData.put("paysDetail", payDetail);

        //只退款不退货，全额退款
        refundData.put("returnGoods", "false");
        refundData.put("operatePwd", cashier.getOprPassword());
        pay.refund(refundData).then().
                body("result.success",equalTo(true)).
                body("data.orderStatus",equalTo("ALL_REFUND")).
                body("data.refundAmount", equalTo(refundData.get("refundAmount"))).
                body("data.canReturnGoods", equalTo(true));
    }

    @Test
    @DisplayName("现金支付，部分退款，不退货")
    @Description("商品不享受优惠，现金支付，部分退款，不退货")
    void testPartRefundWithNonReturn() {
        HashMap<String, Object> payData = new HashMap<>();
        //支付方式是现金
        payData.put("paymentChannel", "CASH");
        payData.put("channelId", "4");
        String body=template("/com/kaimai/cashier/testcase/payTemplate.json", payData);
        JSONObject params = JSONObject.parseObject(body);
        Map<String, Object> orderInfo = pay.pay(params).then().
                body("result.success", equalTo(true)).
                body("data.orderStatus", equalTo("PAY_SUC")).
                body("data.tradeType", equalTo("PAYMENT")).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchema.json")).
                extract().jsonPath().getMap("data");


        HashMap<String, Object> refundData = new HashMap<>();
        refundData.put("orderNo", orderInfo.get("orderNo"));
        refundData.put("refundAmount", Integer.parseInt(orderInfo.get("receiveAmount").toString()) >> 1);

        //获取支付交易的id和退款金额
        List<Map<String, Object>> refundInfoList = pay.getRefundListForPays(refundData).then().
                body("result.success",equalTo(true)).
                extract().jsonPath().getList("data");

        //获取退款订单的支付交易id和退款金额
        ArrayList<String> payDetail = new ArrayList<>();
        refundInfoList.forEach(item -> {
            Map<String, Object> refundInfo = new HashMap<>();
            refundInfo.put("id", item.get("id"));
            refundInfo.put("refundAmount", item.get("refundAmount"));
            payDetail.add(JSONObject.toJSONString(refundInfo));
        });
        refundData.put("paysDetail", payDetail);

        //只退款不退货，全额退款
        refundData.put("returnGoods", "false");
        refundData.put("operatePwd", cashier.getOprPassword());
        pay.refund(refundData).then().
                body("result.success",equalTo(true)).
                body("data.orderStatus",equalTo("PART_REFUND")).
                body("data.refundAmount", equalTo(refundData.get("refundAmount"))).
                body("data.canReturnGoods", equalTo(false));
    }

    @Test
    @DisplayName("会员现金支付，全额退款，并退货")
    @Description("商品不享受优惠，会员现金支付，全额退款，并退货；且已退款的订单不能再退款")
    void testFullRefundWithReturn() throws InterruptedException {
        HashMap<String, Object> refundData = new HashMap<>();
        HashMap<String, Object> payData = new HashMap<>();
        String vipPayToken = "";

        //获取会员支付的vipPayToken
        String vipCardNo = vip.getVipCardNo();
        try {
            vipPayToken = vip.getDetailOfVip(vipCardNo).then().extract().path("data.vipPayToken");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //会员现金支付
        payData.put("paymentChannel", "CASH");
        payData.put("channelId", "4");
        payData.put("vipCardNo", vipCardNo);
        payData.put("vipPayToken", vipPayToken);

        String body = template("/com/kaimai/cashier/testcase/payTemplateWithDistForEvyGoodAndPointForVip.json", payData);
        JSONObject params = JSONObject.parseObject(body);

        //支付成功，获取订单号
        Map<String, Object> orderInfo = pay.payForVip(params).then().
                body("result.success", equalTo(true)).
                body("data.orderStatus", equalTo("PAY_SUC")).
                body("data.tradeType", equalTo("PAYMENT")).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchemaForVip.json")).
                extract().jsonPath().getMap("data");

        String orderNo = orderInfo.get("orderNo").toString();

        // 获取退款订单号、退款金额
        refundData.put("orderNo", orderNo);
        refundData.put("refundAmount", orderInfo.get("receiveAmount"));
        refundData.put("refundTotalAmount", orderInfo.get("totalAmount"));
        refundData.put("refundDiscountAmount", orderInfo.get("merchantDiscountTotalAmount"));

        //根据订单号，获取商品id
        List<Map<String, Object>> goodLists = order.getDetailOfOrder(orderNo).
                then().body("result.success", equalTo(true)).
                body("data.orderInfo.orderNo", equalTo(orderNo)).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/orderForPaySchema.json")).
                extract().jsonPath().getList("data.goodsList");


        //获取退款的商品id，并标识退货
        ArrayList<String> goodsDetail = new ArrayList<>();
        goodLists.forEach(item -> {
            Map<String, Object> goods = new HashMap<>();
            goods.put("id", item.get("id"));
            goods.put("returnGoods", true);
            goodsDetail.add(JSONObject.toJSONString(goods));
        });
        refundData.put("goodsDetail", goodsDetail);

        //获取支付交易的id和退款金额
        List<Map<String, Object>> refundInfoList = pay.getRefundListForPays(refundData).then().
                body("result.success",equalTo(true)).
                extract().jsonPath().getList("data");


        //获取退款订单的支付交易id和退款金额
        ArrayList<String> payDetail = new ArrayList<>();
        refundInfoList.forEach(item -> {
            Map<String, Object> refundInfo = new HashMap<>();
            refundInfo.put("id", item.get("id"));
            refundInfo.put("refundAmount", item.get("refundAmount"));
            payDetail.add(JSONObject.toJSONString(refundInfo));
        });
        refundData.put("paysDetail", payDetail);


        //退款并退货，全额退款
        refundData.put("returnGoods", "true");
        refundData.put("operatePwd", cashier.getOprPassword());

        pay.refund(refundData).then().
                body("result.success",equalTo(true)).
                body("data.orderStatus",equalTo("ALL_REFUND")).
                body("data.refundAmount", equalTo(refundData.get("refundAmount"))).
                body("data.canReturnGoods", equalTo(true));

        //两次退款时间太短，请稍后再试
        pay.refund(refundData).then().
                body("result.success",equalTo(false)).
                body("result.errorCode",equalTo("REFUND_TOO_SOON"));

        //等待10s,订单已经全额退款，不能继续退款
        Thread.sleep(10000);
        pay.refund(refundData).then().
                body("result.success",equalTo(false)).
                body("result.errorCode",equalTo("ORDER_STATUS_ERROR"));
    }



    @Test
    @DisplayName("退款并退货异常，金额不一致")
    @Description("会员现金支付，全额退款，并退货，退款总金额与商品总金额不一致")
    void testFullRefundWithReturnExp(){
        HashMap<String, Object> refundData = new HashMap<>();
        HashMap<String, Object> payData = new HashMap<>();
        String vipPayToken = "";

        //获取会员支付的vipPayToken
        String vipCardNo = vip.getVipCardNo();
        try {
            vipPayToken = vip.getDetailOfVip(vipCardNo).then().extract().path("data.vipPayToken");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //会员现金支付
        payData.put("paymentChannel", "CASH");
        payData.put("channelId", "4");
        payData.put("vipCardNo", vipCardNo);
        payData.put("vipPayToken", vipPayToken);

        String body = template("/com/kaimai/cashier/testcase/payTemplateWithDistForEvyGoodAndPointForVip.json", payData);
        JSONObject params = JSONObject.parseObject(body);

        //支付成功，获取订单号
        Map<String, Object> orderInfo = pay.payForVip(params).then().
                body("result.success", equalTo(true)).
                body("data.orderStatus", equalTo("PAY_SUC")).
                body("data.tradeType", equalTo("PAYMENT")).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/paySuccessSchemaForVip.json")).
                extract().jsonPath().getMap("data");

        String orderNo = orderInfo.get("orderNo").toString();

        // 获取退款订单号、退款金额
        refundData.put("orderNo", orderNo);
        refundData.put("refundAmount", orderInfo.get("receiveAmount"));
        refundData.put("refundTotalAmount", Integer.parseInt(orderInfo.get("totalAmount").toString()) + 100);
        refundData.put("refundDiscountAmount", orderInfo.get("merchantDiscountTotalAmount"));

        //根据订单号，获取商品id
        List<Map<String, Object>> goodLists = order.getDetailOfOrder(orderNo).
                then().body("result.success", equalTo(true)).
                body("data.orderInfo.orderNo", equalTo(orderNo)).
                body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/orderForPaySchema.json")).
                extract().jsonPath().getList("data.goodsList");


        //获取退款的商品id，并标识退货
        ArrayList<String> goodsDetail = new ArrayList<>();
        goodLists.forEach(item -> {
            Map<String, Object> goods = new HashMap<>();
            goods.put("id", item.get("id"));
            goods.put("returnGoods", true);
            goodsDetail.add(JSONObject.toJSONString(goods));
        });
        refundData.put("goodsDetail", goodsDetail);

        //获取支付交易的id和退款金额
        List<Map<String, Object>> refundInfoList = pay.getRefundListForPays(refundData).then().
                body("result.success",equalTo(true)).
                extract().jsonPath().getList("data");


        //获取退款订单的支付交易id和退款金额
        ArrayList<String> payDetail = new ArrayList<>();
        refundInfoList.forEach(item -> {
            Map<String, Object> refundInfo = new HashMap<>();
            refundInfo.put("id", item.get("id"));
            refundInfo.put("refundAmount", item.get("refundAmount"));
            payDetail.add(JSONObject.toJSONString(refundInfo));
        });
        refundData.put("paysDetail", payDetail);


        //退款并退货，全额退款
        refundData.put("returnGoods", "true");
        refundData.put("operatePwd", cashier.getOprPassword());

        pay.refund(refundData).then().
                body("result.success",equalTo(false)).
                body("result.errorMsg",equalTo("退款总金额与商品总金额不一致，请查证"));

    }
}
