package com.kaimai.cashier.api;


import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Condition;

import static io.restassured.RestAssured.given;

public class VipApplication {
    String vipPhone;
    String vipCardNo;
    String vipName;

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

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    @Step("获取会员列表")
    public Response getListOfVip(String vipCode){
        return given().log().all().
                formParam("vipCode", vipCode).
               post("/v1/vip/simple/detail/list").
                then().log().all().
                extract().response();
    }

    @Step("获取会员详情")
    public Response getDetailOfVip(String vipCardNo){
        return given().log().all().
                formParam("vipCardNo", vipCardNo).
               post("/v1/vip/detail/query").
                then().log().all().
                extract().response();
    }

    @Step("修改会员手机号")
    public Response updatePhoneOfVip(String vipCardNo, String oldPhone, String newPhone, String storeManagerUserId, String oprPwd){
        return given().log().all().
                formParam("cardNo", vipCardNo).
                formParam("oldPhone", oldPhone).
                formParam("phone", newPhone).
                formParam("storeManagerUserId", storeManagerUserId).
                formParam("operatePwd", oprPwd).
               post("/v1/vip/info/update/phone").
                then().log().all().
                extract().response();
    }

    @Step("获取券列表")
    public Response getListOfCoupon(String vipCardNo) {
        return given().log().all().
                formParam("vipCardNo", vipCardNo).
               post("/v1/vip/coupon/list").
                then().log().all().
                extract().response();
    }

    @Step("获取积分列表")
    public Response getListOfPoint(String vipCardNo, Integer pageNumber, Integer pageSize) {
        return given().log().all().
                formParam("vipCardNo", vipCardNo).
                formParam("pageNumber", pageNumber).
                formParam("pageSize", pageSize).
               post("/v1/vip/point/record/list").
                then().log().all().
                extract().response();
    }

    @Step("解绑实体卡")
    public Response unbindCardForVip(String vipCardNo) {
        return given().log().all().
                formParam("vipCardNo", vipCardNo).
               post("/v1/vip/physicalcard/unbind").
                then().log().all().
                extract().response();
    }

    @Step("绑定实体卡")
    public Response bindCardForVip(String vipCardNo, String cardNo) {
        return given().log().all().
                formParam("vipCardNo", vipCardNo).
                formParam("physicalCardNo", cardNo).
               post("/v1/vip/physicalcard/bind").
                then().log().all().
                extract().response();
    }
}
