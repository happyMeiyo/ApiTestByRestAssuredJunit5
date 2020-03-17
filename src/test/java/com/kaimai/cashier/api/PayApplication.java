package com.kaimai.cashier.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

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
}
