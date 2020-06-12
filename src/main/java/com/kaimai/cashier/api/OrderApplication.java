package com.kaimai.cashier.api;

import com.kaimai.cashier.common.CashierConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderApplication extends CashierConfig {
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    private static OrderApplication order;

    public static OrderApplication getInstance() {
        if (order == null) {
            order = new OrderApplication();
        }
        return order;
    }

    @Step("获取订单详情")
    public Response getDetailOfOrder(String orderNo){
        return given().
                    formParam("orderNo", orderNo).
               when().
                    post("/v1/order/manager/detail").
               then().
                    extract().response();
    }

    @Step("获取订单汇总数据")
    public Response getDetailOfSummaryForOrders(String startTime, String endTime) {
        return given().
                    formParam("startTime", startTime).
                    formParam("endTime", endTime).
                when().
                    post("/v1/order/manager/summary/detail").
                then().
                    extract().response();
    }

    @Step("查询订单列表")
    public Response getListOfOrders(String startTime, String endTime) {
        return given().
                    formParam("startTime", startTime).
                    formParam("endTime", endTime).
                when().
                    post("/v1/order/manager/list").
                then().
                    extract().response();
    }
}
