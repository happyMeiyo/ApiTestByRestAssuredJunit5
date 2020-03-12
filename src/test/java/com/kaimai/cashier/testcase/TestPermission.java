package com.kaimai.cashier.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaimai.cashier.api.Permission;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("测试权限相关业务")
public class TestPermission extends TestUser{

    @Test
    @DisplayName("测试获取权限")
    @Description("测试获取权限")
    void testPermission() throws JsonProcessingException {
        Permission pm = new Permission();

        Map<String, String> permission = pm.getPermission().then().extract().jsonPath().getMap("data");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(permission);

        assertThat("权限数据格式正确", json, matchesJsonSchemaInClasspath("com/kaimai/cashier/testcase/permissionSchema.json"));

    }
}
