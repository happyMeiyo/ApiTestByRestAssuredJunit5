package com.kaimai.cashier.api;

import com.kaimai.cashier.common.CashierConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Order extends CashierConfig {

    @Step("获取订单详情")
    public Response getDetailOfOrder(String orderNo){
        return given().
                    formParam("orderNo", orderNo).
               when().
                    post("/v1/order/manager/detail").
               then().
                    extract().response();
    }
}
