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
        return given().
                    formParam("vipCode", vipCode).
                when().
                    post("/v1/vip/simple/detail/list").
                then().
                    extract().response();
    }

    @Step("获取会员详情")
    public Response getDetailOfVip(String vipCardNo){
        return given().
                    formParam("vipCardNo", vipCardNo).
                when().
                    post("/v1/vip/detail/query").
                then().
                    extract().response();
    }

    @Step("修改会员手机号")
    public Response updatePhoneOfVip(String vipCardNo, String oldPhone, String newPhone, String storeManagerUserId, String oprPwd){
        return given().
                    formParam("cardNo", vipCardNo).
                    formParam("oldPhone", oldPhone).
                    formParam("phone", newPhone).
                    formParam("storeManagerUserId", storeManagerUserId).
                    formParam("operatePwd", oprPwd).
                when().
                    post("/v1/vip/info/update/phone").
                then().
                    extract().response();
    }

    @Step("获取券列表")
    public Response getListOfCoupon(String vipCardNo) {
        return given().
                    formParam("vipCardNo", vipCardNo).
                when().
                    post("/v1/vip/coupon/list").
                then().
                    extract().response();
    }

    @Step("获取积分列表")
    public Response getListOfPoint(String vipCardNo, Integer pageNumber, Integer pageSize) {
        return given().
                    formParam("vipCardNo", vipCardNo).
                    formParam("pageNumber", pageNumber).
                    formParam("pageSize", pageSize).
                when().
                    post("/v1/vip/point/record/list").
                then().
                    extract().response();
    }

    @Step("解绑实体卡")
    public Response unbindCardForVip(String vipCardNo) {
        return given().
                    formParam("vipCardNo", vipCardNo).
                when().
                    post("/v1/vip/physicalcard/unbind").
                then().
                    extract().response();
    }

    @Step("绑定实体卡")
    public Response bindCardForVip(String vipCardNo, String cardNo) {
        return given().
                    formParam("vipCardNo", vipCardNo).
                    formParam("physicalCardNo", cardNo).
                when().
                    post("/v1/vip/physicalcard/bind").
                then().
                    extract().response();
    }

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
