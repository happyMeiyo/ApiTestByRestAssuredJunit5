package com.kaimai.cashier.testcase;

import com.kaimai.cashier.api.OrderApplication;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

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

    @ParameterizedTest(name="查询订单列表异常，开始时间或结束时间异常")
    @DisplayName("获取订单列表异常")
    @Description("测试获取订单列表异常情况")
    @CsvSource({" ,2020-05-13 23:59:59",
                "2020-05-14 23:59:59, 2020-05-13 23:59:59"})
    void testListOfOrderTimeExp(String startTime, String endTime) {
        order.getListOfOrders(startTime, endTime).
                then().body("result.success", equalTo(false));
    }

    @Test
    @DisplayName("获取订单列表异常")
    @Description("查询订单列表异常，查询6个月之前订单")
    void testListOfOrderBefore6M() {
        String str = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.add(Calendar.MONTH,-6);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.add(Calendar.DAY_OF_MONTH,-2);
        String startTime =format.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_MONTH,3);
        String endTime = format.format(calendar.getTime());

        order.getListOfOrders(startTime, endTime).
                then().body("result.success", equalTo(false));
    }

    @Test
    @DisplayName("获取订单列表异常")
    @Description("查询订单列表异常，查询时间跨度超过31天")
    void testListOfOrderBefore31D() {
        String str = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String endTime = format.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.add(Calendar.DAY_OF_MONTH,-31);
        String startTime =format.format(calendar.getTime());

        order.getListOfOrders(startTime, endTime).
                then().body("result.success", equalTo(false));
    }

    @ParameterizedTest(name="查询订单详情异常，订单号为空")
    @DisplayName("获取订单详情异常")
    @Description("测试获取订单详情异常情况")
    @NullAndEmptySource
    void testDetailOfOrderExp(String orderNo) {
       order.getDetailOfOrder(orderNo).
                then().body("result.success", equalTo(false));
    }

    // TODO: 2020/5/14  1. 非本商户的订单、非本门店、同一门店收银员不可查看其他收银员的订单
    @ParameterizedTest(name="查询订单详情异常，无权限查看")
    @DisplayName("获取订单详情异常")
    @Description("测试获取订单详情异常情况")
    @ValueSource(strings = { "2649038716498"})
    void testDetailOfOrderNoPerms(String orderNo) {
        order.getDetailOfOrder(orderNo).
                then().body("result.success", equalTo(false)).
                        body("result.errorCode",equalTo("NO_PERMISSION"));
    }

}