package com.kaimai.cashier.testcase;

import com.kaimai.cashier.api.Goods;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestGoods extends TestUser{
    @Test
    @DisplayName("获取商品分类")
    @Description("获取商品分类")
    void testCategorys(){
        Goods gds = new Goods();
        gds.getCategorys().then();
    }
}
