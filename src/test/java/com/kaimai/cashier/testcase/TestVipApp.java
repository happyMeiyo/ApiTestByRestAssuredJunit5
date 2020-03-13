package com.kaimai.cashier.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.kaimai.cashier.api.Order;
import com.kaimai.cashier.api.UserLogin;
import com.kaimai.cashier.api.VipApplication;
import com.kaimai.cashier.common.User;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("测试会员相关业务")
public class TestVipApp extends TestUser {
    static VipApplication vip = new VipApplication();

    @BeforeAll
    @Step("获取会员信息")
    static void getVipInfo() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        TypeReference<HashMap<String, Object>> typeRef =
                new TypeReference<HashMap<String, Object>>() {
                };
        InputStream src = TestVipApp.class.getResourceAsStream("vip.yml");

        try {
            HashMap<String, Object> VipInfo = mapper.readValue(src, typeRef);
            vip.setVipPhone(VipInfo.get("vipPhone").toString());
            vip.setVipCardNo(VipInfo.get("vipCardNo").toString());
            vip.setVipName(VipInfo.get("vipName").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        String oprPwd = User.getInstance().getOprPassword();

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

    @DisplayName("会员现金充值成功")
    @ParameterizedTest(name="会员{0}充值：{1}")
    @Description("会员现金充值成功")
    @CsvSource({"CASH,100"})
    void testChargeByCashForVip(String channel, Integer amount) throws JsonProcessingException {
        Map<String, Object> chargeInfo = vip.chargeForVip(vip.getVipCardNo(), channel, amount).
                then().body("result.success", equalTo(true)).
                       extract().jsonPath().getMap("data");

        assertAll("chargeInfo",
                () -> assertEquals("PAY_SUC", chargeInfo.get("orderStatus")),
                () -> assertEquals("CHARGE", chargeInfo.get("tradeType")),
                () -> assertEquals(vip.getVipCardNo(), chargeInfo.get("vipCardNo")),
                () -> assertEquals(Collections.singletonList(channel), chargeInfo.get("paymentChannels")),
                () -> assertEquals(amount, chargeInfo.get("receiveAmount")),
                () -> {
                        ArrayList<Map<String, Object>> payList = (ArrayList<Map<String, Object>>) chargeInfo.get("payList");
                        assertAll("payList",
                                () -> assertEquals("PAYMENT", payList.get(0).get("tradeType")),
                                () -> assertEquals(amount, payList.get(0).get("receiveAmount")),
                                () -> assertEquals(channel, payList.get(0).get("paymentChannel")),
                                () -> assertEquals("PAY_SUC", payList.get(0).get("orderStatus"))
                        );

                }
        );

        // TODO: 2020/3/13 orderNo写入yaml文件
        //String orderNo = chargeInfo.get("orderNo").toString();
    }
}

