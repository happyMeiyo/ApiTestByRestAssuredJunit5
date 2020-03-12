package com.kaimai.cashier.testcase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.kaimai.cashier.api.VipAppication;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("测试会员相关业务")
public class TestVipApp extends TestUser{
    static VipAppication vip = new VipAppication();

    @BeforeAll
    static void getVipInfo(){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        TypeReference<HashMap<String, Object>> typeRef =
                new TypeReference<HashMap<String, Object>>() {
                };
        InputStream src = TestVipApp.class.getResourceAsStream("vip.yml");

        try {
            HashMap<String, Object> VipInfo = mapper.readValue(src, typeRef);
            vip.setVipPhone(VipInfo.get("vipPhone").toString());
            vip.setVipCardNo(VipInfo.get("vipCardNo").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @DisplayName("获取会员列表失败")
    @Description("测试获取会员列表异常情况")
    @NullAndEmptySource
    void testGetListOfVipFailure(String vipCode){
        vip.getListOfVip(vipCode).then().body("result.success", equalTo(false));
    }


    @ParameterizedTest
    @DisplayName("获取会员列表成功")
    @Description("测试获取会员列表正常情况")
    @MethodSource("getVipCode")
    void testGetListOfVip(String vipCode){
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
    void testGetDetailOfVipFailure(String vipCardNo){
        vip.getDetailOfVip(vipCardNo).then().body("result.success", equalTo(false));
    }


    static Stream<String> getVipCardNo() {
        return Stream.of(vip.getVipCardNo());
    }
    @ParameterizedTest
    @DisplayName("获取会员列表成功")
    @Description("测试获取会员列表正常情况")
    @MethodSource("getVipCardNo")
    void testGetDetailOfVip(String vipCardNo){
        vip.getDetailOfVip(vipCardNo).then().body("data.vipPhone", endsWith("****"));
    }
}

