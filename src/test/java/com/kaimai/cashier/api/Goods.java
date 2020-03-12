package com.kaimai.cashier.api;

import com.kaimai.cashier.common.CashierConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Goods extends CashierConfig {
    @Step("获取商品分类")
    public Response getCategorys(){
        return given().
                post("/v1/product/shop/category/query").
                then().
                extract().response();
    }

}