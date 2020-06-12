package com.kaimai.cashier.api;

import com.kaimai.cashier.common.CashierConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PayApplication extends CashierConfig {
    private static PayApplication pay;

    public static PayApplication getInstance() {
        if (pay == null) {
            pay = new PayApplication();
        }
        return pay;
    }

    @Step("会员充值")
    public Response chargeForVip(String vipCardNo, String channel, Integer payAmount, Integer totalAmount) {
        return given().
                    formParam("vipCardNo", vipCardNo).
                    formParam("payAmount", payAmount).
                    formParam("paymentChannel", channel).
                    formParam("totalAmount", totalAmount).
                when().
                    post("/v1/order/charge").
                then().
                    extract().response();
    }

    @Step("支付，无优惠")
    public Response pay(Map<String, Object> params) {
        return given().
                    formParam("goodsDetail", String.valueOf(params.get("goodsDetail"))).
                    formParam("tempDiscountAmount", String.valueOf(params.get("tempDiscountAmount"))).
                    formParam("channelId", String.valueOf(params.get("channelId"))).
                    formParam("point", String.valueOf(params.get("point"))).
                    formParam("payAmount", String.valueOf(params.get("payAmount"))).
                    formParam("unDiscountAmount", String.valueOf(params.get("unDiscountAmount"))).
                    formParam("eliminateAmount", String.valueOf(params.get("eliminateAmount"))).
                    formParam("deviceNo", String.valueOf(params.get("deviceNo"))).
                    formParam("paymentChannel", String.valueOf(params.get("paymentChannel"))).
                    formParam("cashBackAmount", String.valueOf(params.get("cashBackAmount"))).
                    formParam("smilePay", String.valueOf(params.get("smilePay"))).
                    formParam("totalDiscountAmount", String.valueOf(params.get("totalDiscountAmount"))).
                    formParam("clientOrderNo", String.valueOf(params.get("clientOrderNo"))).
                    formParam("totalAmount", String.valueOf(params.get("totalAmount"))).
                when().
                    post("/v2/pay/nowaitpay").
                then().
                    extract().response();
    }

    @Step("支付，有全场活动")
    public Response payWithDiscountForOrder(Map<String, Object> params) {
        return given().
                    formParam("activityId", String.valueOf(params.get("activityId"))).
                    formParam("goodsDetail", String.valueOf(params.get("goodsDetail"))).
                    formParam("tempDiscountAmount", String.valueOf(params.get("tempDiscountAmount"))).
                    formParam("channelId", String.valueOf(params.get("channelId"))).
                    formParam("point", String.valueOf(params.get("point"))).
                    formParam("payAmount", String.valueOf(params.get("payAmount"))).
                    formParam("unDiscountAmount", String.valueOf(params.get("unDiscountAmount"))).
                    formParam("eliminateAmount", String.valueOf(params.get("eliminateAmount"))).
                    formParam("deviceNo", String.valueOf(params.get("deviceNo"))).
                    formParam("paymentChannel", String.valueOf(params.get("paymentChannel"))).
                    formParam("cashBackAmount", String.valueOf(params.get("cashBackAmount"))).
                    formParam("smilePay", String.valueOf(params.get("smilePay"))).
                    formParam("totalDiscountAmount", String.valueOf(params.get("totalDiscountAmount"))).
                    formParam("clientOrderNo", String.valueOf(params.get("clientOrderNo"))).
                    formParam("totalAmount", String.valueOf(params.get("totalAmount"))).
                when().
                    post("/v2/pay/nowaitpay").
                then().
                    extract().response();
    }

    @Step("会员支付，无全场活动")
    public Response payForVip(Map<String, Object> params) {
        return given().
                    formParam("vipCardNo", String.valueOf(params.get("vipCardNo"))).
                    formParam("vipPayToken", String.valueOf(params.get("vipPayToken"))).
                    formParam("goodsDetail", String.valueOf(params.get("goodsDetail"))).
                    formParam("tempDiscountAmount", String.valueOf(params.get("tempDiscountAmount"))).
                    formParam("channelId", String.valueOf(params.get("channelId"))).
                    formParam("point", String.valueOf(params.get("point"))).
                    formParam("payAmount", String.valueOf(params.get("payAmount"))).
                    formParam("unDiscountAmount", String.valueOf(params.get("unDiscountAmount"))).
                    formParam("eliminateAmount", String.valueOf(params.get("eliminateAmount"))).
                    formParam("deviceNo", String.valueOf(params.get("deviceNo"))).
                    formParam("paymentChannel", String.valueOf(params.get("paymentChannel"))).
                    formParam("cashBackAmount", String.valueOf(params.get("cashBackAmount"))).
                    formParam("smilePay", String.valueOf(params.get("smilePay"))).
                    formParam("totalDiscountAmount", String.valueOf(params.get("totalDiscountAmount"))).
                    formParam("clientOrderNo", String.valueOf(params.get("clientOrderNo"))).
                    formParam("totalAmount", String.valueOf(params.get("totalAmount"))).
                when().
                    post("/v2/pay/nowaitpay").
                then().
                    extract().response();
    }


    @Step("获取应退款的支付列表")
    public Response getRefundListForPays(Map<String, Object> params) {
        return given().
                    formParam("orderNo", String.valueOf(params.get("orderNo"))).
                    formParam("refundAmount", String.valueOf(params.get("refundAmount"))).
                when().
                    post("/v1/order/refund/pays").
                then().
                    extract().response();
    }

    @Step("退款")
    public Response refund(HashMap<String, Object> params) {
        return given().
                    formParam("orderNo", String.valueOf(params.get("orderNo"))).
                    formParam("refundAmount", String.valueOf(params.getOrDefault("refundAmount", 0))).
                    formParam("returnGoods", String.valueOf(params.getOrDefault("returnGoods", false))).
                    formParam("operatePwd", String.valueOf(params.getOrDefault("operatePwd", "123456"))).
                    formParam("paysDetail", String.valueOf(params.getOrDefault("paysDetail", ""))).
                    formParam("goodsDetail", String.valueOf(params.getOrDefault("goodsDetail", ""))).
                    formParam("storeManagerUserId", String.valueOf(params.getOrDefault("storeManagerUserId", ""))).
                    formParam("refundDiscountAmount", String.valueOf(params.getOrDefault("refundDiscountAmount", 0))).
                    formParam("refundTotalAmount", String.valueOf(params.getOrDefault("refundTotalAmount", 0))).
                when().
                    post("/v1/order/refund").
                then().
                    extract().response();
    }
}
