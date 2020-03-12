package com.kaimai.cashier.testcase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.kaimai.cashier.api.UserLogin;
import com.kaimai.cashier.api.VipApplication;
import com.kaimai.cashier.common.User;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;

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

    @ParameterizedTest
    @DisplayName("获取会员列表失败")
    @Description("测试获取会员列表异常情况")
    @NullAndEmptySource
    void testGetListOfVipExp(String vipCode) {
        vip.getListOfVip(vipCode).then().body("result.success", equalTo(false));
    }

    @ParameterizedTest
    @DisplayName("获取会员详情失败")
    @Description("测试获取会员详情失败情况")
    @ValueSource(strings = { "98765432101" })
    void testGetDetailOfVipFailure(String vipCardNo) {
        vip.getDetailOfVip(vipCardNo).then().body("result.success", equalTo(true)).
                                             body("data", equalTo(null));
    }

    @ParameterizedTest
    @DisplayName("获取会员列表成功")
    @Description("测试获取会员列表正常情况")
    @MethodSource("getVipCode")
    void testGetListOfVip(String vipCode) {
        vip.getListOfVip(vipCode).then().body("data[0].vipPhone", endsWith("****")).
                body("data[0].vipCardNo", equalTo(vip.getVipCardNo()));
    }

    static Stream<String> getVipCode() {
        return Stream.of(vip.getVipPhone(), vip.getVipCardNo());
    }

    @ParameterizedTest
    @DisplayName("获取会员详情失败")
    @Description("测试获取会员详情异常情况")
    @NullAndEmptySource
    void testGetDetailOfVipExp(String vipCardNo) {
        vip.getDetailOfVip(vipCardNo).then().body("result.success", equalTo(false));
    }

    static Stream<String> getVipCardNo() {
        return Stream.of(vip.getVipCardNo());
    }

    @ParameterizedTest
    @DisplayName("获取会员详情成功")
    @Description("测试获取详情列表正常情况")
    @MethodSource("getVipCardNo")
    void testGetDetailOfVip(String vipCardNo) {
        vip.getDetailOfVip(vipCardNo).then().body("data.vipPhone", endsWith("****")).
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
        vip.getListOfVip(newPhone).then().body("data[0].vipName", equalTo(vip.getVipName())).
                                          body("data[0].vipPhone", startsWith("1111111"));

        vip.updatePhoneOfVip(vip.getVipCardNo(), newPhone, vip.getVipPhone(), storeManagerUserId, oprPwd).
                then().body("result.success", equalTo(true));

        vip.getListOfVip(vip.getVipPhone()).then().body("data[0].vipName", equalTo(vip.getVipName())).
                body("data[0].vipPhone", startsWith(vip.getVipPhone().substring(0, 6)));

    }
}

