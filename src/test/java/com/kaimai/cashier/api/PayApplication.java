package com.kaimai.cashier.api;

import com.kaimai.cashier.common.CashierConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PayApplication extends CashierConfig {
    @Step("会员充值")
    public Response chargeForVip(String vipCardNo, String channel, Integer payAmount, Integer totalAmount) {
        return given().
                    formParam("vipCardNo", vipCardNo).
                    formParam("payAmount", payAmount).
                    formParam("paymentChannel", channel).
                    formParam("totalAmount", totalAmount).
                when().log().all().
                    post("/v1/order/charge").
                then().log().all().
                    extract().response();
    }

    @Step("支付")
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
                when().log().all().
                    post("/v2/pay/nowaitpay").
                then().log().all().
                    extract().response();
    }
}
