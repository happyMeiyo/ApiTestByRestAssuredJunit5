package com.kaimai.cashier.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.kaimai.cashier.api.OrderApplication;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("测试订单相关业务")
public class TestOrder extends TestUser{
    OrderApplication order = OrderApplication.getInstance();

    @Test
    @DisplayName("查询订单汇总")
    @Description("查询订单汇总数据")
    void testDetailOfSummaryForOrders(){
        String str = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(str);
        String dateFormat = format.format(new Date());
        String startTime = dateFormat + " 00:00:00";
        String endTime = dateFormat + " 23:59:59";

        order.getDetailOfSummaryForOrders(startTime, endTime).
                then().body("result.success", equalTo(true)).
                       body(matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/summaryOfOrdersSchema.json"));
    }

    @ParameterizedTest(name="查询订单详情异常，订单号为空")
    @DisplayName("获取订单详情异常")
    @Description("测试获取订单详情异常情况")
    @NullAndEmptySource
    void testDetailOfOrder(String ordrNO) {
       order.getDetailOfOrder(ordrNO).
                then().body("result.success", equalTo(false));
    }


}