package com.kaimai.cashier.api;

import com.kaimai.cashier.common.CashierConfig;
import io.restassured.response.Response;
import sun.misc.Cache;

import static io.restassured.RestAssured.given;

public class Permission extends CashierConfig {
    public Response getPermission(){
        return given().
                post("/v1/permission/permissions").
               then().
                extract().response();
    }
}
