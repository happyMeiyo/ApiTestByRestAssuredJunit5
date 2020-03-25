package com.kaimai.cashier.api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Condition;

import static io.restassured.RestAssured.given;

public class PayApplication {
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

    @Step("支付")
    public Response pay(String body) {
        return given().
                    body(body).
                when().log().all().
                    post("/v2/pay/nowaitpay").
                then().log().all().
                    extract().response();
    }
}
