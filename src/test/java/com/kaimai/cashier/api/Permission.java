package com.kaimai.cashier.api;

import com.kaimai.cashier.common.CashierConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Permission extends CashierConfig {
    @Step("获取权限列表")
    public Response getPermission(){
        return given().
                post("/v1/permission/permissions").
               then().
                extract().response();
    }
}
