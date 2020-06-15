package com.kaimai.cashier.testcase;

import com.kaimai.cashier.api.UserLogin;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.Matchers.equalTo;

@DisplayName("测试用户登录相关业务")
public class TestUserLogin {
    UserLogin ul = UserLogin.getInstance();

    @Test
    @Description("测试用户登录成功")
    @DisplayName("用户登录成功")
    void testUserLoginSuccess() {
        ul.userLoginSuccess().then().body("result.success", equalTo(true));
        ul.userLogout().then().body("result.success", equalTo(true));
    }

    @ParameterizedTest(name="用户登录失败")
    @Description("测试用户登录失败")
    @DisplayName("用户登录失败")
    @CsvSource({
            "111111, qing, a123456, USER_MERCHANT_USER_CODE_ERROR",
            "100457, q001, a123456, USER_MERCHANT_USER_CODE_ERROR",
            "100457, qing, 123456, USER_LOGIN_PWD_ERROR"
    })
    void testUserLoginFailure(String merchantCode, String username, String password, String errorCode) {
        ul.userLoginFailure(merchantCode, username, password).then().
                body("result.errorCode", equalTo(errorCode)).
                body("result.confirm", equalTo(false));
    }
}
