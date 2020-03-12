package com.kaimai.cashier.api;

import com.kaimai.cashier.common.CashierConfig;
import com.kaimai.cashier.common.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserLogin extends CashierConfig{
    String token;
    Integer userId;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private static UserLogin userLogin;

    public static UserLogin getInstance() {
        if (userLogin == null) {
            userLogin = new UserLogin();
        }
        return userLogin;
    }

    @Step("用户登录 merchantCode {0}, username {1} and password {2}")
    public Response userLogin(String merchantCode, String username, String password){
        return given().
                formParam("merchantCode", merchantCode).
                formParam("username", username).
                formParam("password", password).
               when().
                post("/v1/passport/login").
               then().extract().response();
    }


    public Response userLoginSuccess(){
        User userInfo = User.getInstance();
        return userLogin(userInfo.getMerchantCode(), userInfo.getUserCode(), userInfo.getPassword());
    }

    public Response userLoginFailure(String merchantCode, String username, String password) {
        return userLogin(merchantCode,username,password);
    }

    @Step("用户退出登录")
    public Response userLogout(){
        return given().when().post("/v1/passport/logout").
               then().extract().response();
    }


}
