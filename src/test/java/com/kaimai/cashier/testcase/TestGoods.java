package com.kaimai.cashier.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaimai.cashier.api.Goods;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestGoods extends TestUser{
    @Test
    @DisplayName("获取商品分类")
    @Description("获取商品分类")
    void testCategorys() throws JsonProcessingException {
        Goods gds = new Goods();
        //Map<String, String> category = gds.getCategorys().then().extract().jsonPath().getJsonObject("data[0]");
        Map<String, String> category = gds.getCategorys().then().extract().jsonPath().getMap("data[0]");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(category);

        assertThat("类目返回数据格式正确", json, matchesJsonSchemaInClasspath("com/kaimai/cashier/categorySchema.json"));
        //gds.getCategorys().then().body(matchesJsonSchemaInClasspath("com/kaimai/cashier/categorySchemaNew.json"));
    }
}