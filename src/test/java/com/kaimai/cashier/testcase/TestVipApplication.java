package com.kaimai.cashier.testcase;

import com.kaimai.cashier.api.UserLogin;
import com.kaimai.cashier.api.VipApplication;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@DisplayName("测试会员相关业务")
public class TestVipApplication extends TestUser {
    static VipApplication vip = VipApplication.getInstance();

    @ParameterizedTest(name="查询会员异常，会员码为空")
    @DisplayName("获取会员列表异常")
    @Description("测试获取会员列表异常情况")
    @NullAndEmptySource
    void testGetListOfVipExp(String vipCode) {
        vip.getListOfVip(vipCode).
                then().body("result.success", equalTo(false));
    }

    @ParameterizedTest(name="查询会员:{0}失败")
    @DisplayName("获取会员列表失败")
    @Description("测试获取会员列表失败情况")
    @ValueSource(strings = { "98765432101" })
    void testGetListOfVipFailure(String vipCardNo) {
        vip.getListOfVip(vipCardNo).
                then().body("result.success", equalTo(true)).
                       body("data", hasSize(0));
    }

    @ParameterizedTest(name="查询会员:{0}成功")
    @DisplayName("获取会员列表成功")
    @Description("测试获取会员列表正常情况")
    @MethodSource("getVipCode")
    void testGetListOfVip(String vipCode) {
        vip.getListOfVip(vipCode).
                then().body("data[0].vipPhone", endsWith("****")).
                       body("data[0].vipCardNo", equalTo(vip.getVipCardNo()));
    }

    static Stream<String> getVipCode() {
        return Stream.of(vip.getVipPhone(), vip.getVipCardNo());
    }

    @ParameterizedTest(name="获取会员详情，会员卡号为空")
    @DisplayName("获取会员详情失败")
    @Description("测试获取会员详情异常情况")
    @NullAndEmptySource
    void testGetDetailOfVipExp(String vipCardNo) {
        vip.getDetailOfVip(vipCardNo).
                then().body("result.success", equalTo(false));
    }

    @ParameterizedTest(name="查询会员:{0}失败")
    @DisplayName("获取会员列表失败")
    @Description("测试获取会员列表失败情况")
    @ValueSource(strings = { "98765432101" })
    void testGetDetailOfVipFailure(String vipCardNo) {
        vip.getDetailOfVip(vipCardNo).
                then().body("result.success", equalTo(true)).
                       body("data", equalTo(null));
    }

    static Stream<String> getVipCardNo() {
        return Stream.of(vip.getVipCardNo());
    }

    @ParameterizedTest(name="获取会员{0}的详情")
    @DisplayName("获取会员详情成功")
    @Description("测试获取详情列表正常情况")
    @MethodSource("getVipCardNo")
    void testGetDetailOfVip(String vipCardNo) {
        vip.getDetailOfVip(vipCardNo).
                then().body("data.vipPhone", endsWith("****")).
                       body("data.vipName", equalTo(vip.getVipName()));
    }

    @Test
    @DisplayName("修改会员信息成功")
    @Description("测试修改会员信息")
    void testModifyPhoneOfVip(){
        String newPhone = "11111112561";
        String storeManagerUserId = UserLogin.getInstance().getUserId().toString();
        String oprPwd = UserLogin.getInstance().getOprPassword();

        vip.updatePhoneOfVip(vip.getVipCardNo(), vip.getVipPhone(), newPhone, storeManagerUserId, oprPwd).
                then().body("result.success", equalTo(true));
        vip.getListOfVip(newPhone).
                then().body("data[0].vipName", equalTo(vip.getVipName())).
                       body("data[0].vipPhone", startsWith("1111111"));

        vip.updatePhoneOfVip(vip.getVipCardNo(), newPhone, vip.getVipPhone(), storeManagerUserId, oprPwd).
                then().body("result.success", equalTo(true));

        vip.getListOfVip(vip.getVipPhone()).
                then().body("data[0].vipName", equalTo(vip.getVipName())).
                       body("data[0].vipPhone", startsWith(vip.getVipPhone().substring(0, 6)));
    }

    @ParameterizedTest(name="获取券，会员卡号为空")
    @DisplayName("获取会员券列表异常")
    @Description("测试获取会员券列表异常")
    @NullAndEmptySource
    void testListOfCouponExp(String vipCardNo){
        vip.getListOfCoupon(vipCardNo).
                then().body("result.success", equalTo(false));
    }

    @Test
    @DisplayName("获取会员券列表成功")
    @Description("测试获取会员券列表")
    void testListOfCoupon(){
        vip.getListOfCoupon(vip.getVipCardNo()).
                then().body("result.success", equalTo(true));
    }

    @ParameterizedTest(name="获取积分，会员卡号为空")
    @DisplayName("获取会员积分列表异常")
    @Description("测试获取会员积分列表异常")
    @NullAndEmptySource
    void testListOfPointExp(String vipCardNo){
        Integer pageNumber = 1;
        Integer pageSize = 30;
        vip.getListOfPoint(vipCardNo, pageNumber, pageSize).
                then().body("result.success", equalTo(false));
    }

    @Test
    @DisplayName("获取会员积分列表成功")
    @Description("测试获取会员积分列表")
    void testListOfPoint(){
        Integer pageNumber = 1;
        Integer pageSize = 30;
        vip.getListOfPoint(vip.getVipCardNo(), pageNumber, pageSize).
                then().body("result.success", equalTo(true)).
                       body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/pointOfVipSchema.json"));
    }

    @ParameterizedTest(name="绑定实体卡号为空")
    @DisplayName("绑定实体卡异常")
    @Description("测试绑定实体卡异常")
    @NullAndEmptySource
    void testBindCardForVipExp(String cardNo){
        vip.bindCardForVip(vip.getVipCardNo(), cardNo).
                then().body("result.success", equalTo(false));
    }

    @DisplayName("绑定解绑实体卡成功")
    @ParameterizedTest(name="绑定实体卡号：{0}")
    @Description("测试绑定解绑实体卡成功")
    @ValueSource(strings = { "98765432101" })
    void testBindCardForVip(String cardNo){
        vip.bindCardForVip(vip.getVipCardNo(), cardNo).
                then().body("result.success", equalTo(true));
        vip.getDetailOfVip(vip.getVipCardNo()).
                then().body("data.physicalCardNo", equalTo(cardNo));

        vip.unbindCardForVip(vip.getVipCardNo()).
                then().body("result.success", equalTo(true));
        vip.getDetailOfVip(vip.getVipCardNo()).
                then().body("data", not(hasKey("physicalCardNo")));
    }
}

