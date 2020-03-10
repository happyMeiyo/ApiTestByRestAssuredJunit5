package com.kaimai.cashier.api;

import com.kaimai.cashier.common.CashierConfig;
import com.kaimai.cashier.common.User;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserLogin extends CashierConfig{

    public Response userLogin(String merchantCode, String username, String password){
        return given().
                formParam("merchantCode", merchantCode).
                formParam("username", username).
                formParam("password", password).
               when().log().all().
                post("/v1/passport/login").
               then().log().all().extract().response();
    }

    public Response userLoginSuccess(){
        User userInfo = User.getInstance();
        return userLogin(userInfo.getMerchantCode(), userInfo.getUserCode(), userInfo.getPassword());
    }

    public Response userLoginFailure(String merchantCode, String username, String password) {
        return userLogin(merchantCode,username,password);
    }

    public Response userLogout(){
        return given().when().post("/v1/passport/logout").
               then().extract().response();
    }


}
