package com.kaimai.cashier.api;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.kaimai.cashier.common.CashierConfig;
import com.kaimai.cashier.testcase.TestVipApplication;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.io.InputStream;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class VipApplication extends CashierConfig {
    private String vipPhone;
    private String vipCardNo;
    private String vipName;

    private static VipApplication vip;

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

    public VipApplication() {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        TypeReference<HashMap<String, Object>> typeRef =
                new TypeReference<HashMap<String, Object>>() {
                };
        InputStream src = TestVipApplication.class.getResourceAsStream("vip.yml");

        try {
            HashMap<String, Object> VipInfo = mapper.readValue(src, typeRef);
            setVipPhone(VipInfo.get("vipPhone").toString());
            setVipCardNo(VipInfo.get("vipCardNo").toString());
            setVipName(VipInfo.get("vipName").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static VipApplication getInstance() {
        if (vip == null) {
            vip = new VipApplication();
        }
        return vip;
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

}
