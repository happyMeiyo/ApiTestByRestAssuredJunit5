package com.kaimai.cashier.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.kaimai.cashier.common.CashierConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.io.InputStream;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class UserLogin extends CashierConfig{
    private String merchantCode;
    private String userCode;
    private String password;
    private String oprPassword;
    private Integer userId;

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getPassword() {
        return password;
    }

    public String getOprPassword() {
        return oprPassword;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserLogin() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeReference<HashMap<String, Object>> typeRef =
                new TypeReference<HashMap<String, Object>>() {
                };

        try {
            InputStream src = UserLogin.class.getResourceAsStream("user.yml");
            HashMap<String, Object> userInfo = mapper.readValue(src,typeRef);

            this.merchantCode = userInfo.get("merchantCode").toString();
            this.userCode = userInfo.get("userCode").toString();
            this.password = userInfo.get("password").toString();
            this.oprPassword = userInfo.get("oprPassword").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                then().
                    extract().response();
    }


    @Step("用户登录成功")
    public Response userLoginSuccess(){
        return userLogin(getMerchantCode(), getUserCode(), getPassword());
    }

    @Step("用户登陆失败")
    public Response userLoginFailure(String merchantCode, String username, String password) {
        return userLogin(merchantCode,username,password);
    }

    @Step("用户退出登录")
    public Response userLogout(){
        return given().
                when().
                    post("/v1/passport/logout").
                then().
                    extract().response();
    }



}
