package com.kaimai.cashier.api;


import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class VipAppication {
    String vipPhone;
    String vipCardNo;

    public void setVipPhone(String vipPhone) {
        this.vipPhone = vipPhone;
    }

    public void setVipCardNo(String vipCardNo) {
        this.vipCardNo = vipCardNo;
    }

    public String getVipPhone() {
        return vipPhone;
    }

    public String getVipCardNo() {
        return vipCardNo;
    }

    public Response getListOfVip(String vipCode){
        return given().log().all().
                formParam("vipCode", vipCode).
               post("/v1/vip/simple/detail/list").
                then().log().all().
                extract().response();
    }

    public Response getDetailOfVip(String vipCardNo){
        return given().log().all().
                formParam("vipCardNo", vipCardNo).
               post("/v1/vip/detail/query").
                then().log().all().
                extract().response();
    }

}
