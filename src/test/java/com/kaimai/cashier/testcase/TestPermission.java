package com.kaimai.cashier.testcase;

import com.kaimai.cashier.api.Permission;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestPermission extends TestUser{

    @Test
    @DisplayName("测试获取权限")
    @Description("测试获取权限")
    void testPermission(){
        Permission pm = new Permission();
        pm.getPermission().then();
    }
}
