package com.kaimai.cashier.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PayApplication {
    @Step("会员充值")
    public Response chargeForVip(String vipCardNo, String channel, Integer amount) {
        return given().
                formParam("vipCardNo", vipCardNo).
                formParam("payAmount", amount).
                formParam("paymentChannel", channel).
                formParam("totalAmount", amount).
                when().
                post("/v1/order/charge").
                then().
                extract().response();
    }
}
