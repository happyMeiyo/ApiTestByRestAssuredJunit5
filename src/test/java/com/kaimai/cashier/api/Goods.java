package com.kaimai.cashier.api;

import com.kaimai.cashier.common.CashierConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Goods extends CashierConfig {
    public Response getCategorys(){
        return given().
                post("/v1/product/shop/category/query").
                then().
                extract().response();
    }

}