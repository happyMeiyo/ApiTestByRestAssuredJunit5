package com.kaimai.cashier.testcase;

import com.kaimai.cashier.api.UserLogin;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


import static org.hamcrest.Matchers.equalTo;

public class TestUser {
    static UserLogin ul = new UserLogin();

    @BeforeAll
    static void userLogin() {
        Response userlogin = ul.userLoginSuccess();

        if(userlogin.getStatusCode() == 200) {
            boolean success = userlogin.getBody().jsonPath().get("result.success");
            if (success) {
                String token = userlogin.then().
                        extract().path("data.accessToken");
                //使用Filter方法，在请求中添加cookie
                RestAssured.filters((req, res, ctx) -> {
                    //请求头中添加Cookie
                    req.header("Cookie", "hsAccessToken=" + token);
                    //发送请求，返回响应
                    return ctx.next(req, res);
                });

                //添加请求中的cookie
//        RequestSpecBuilder builder = new RequestSpecBuilder();
//        builder.addHeader("Cookie", "hsAccessToken=" + token);
//        RestAssured.requestSpecification = builder.build();

                //响应消息的基础判断
                ResponseSpecBuilder build = new ResponseSpecBuilder();
                build.expectStatusCode(200);
                build.expectBody("result.success", equalTo(true));
                RestAssured.responseSpecification = build.build();
            }
//        responseSpecification=new ResponseSpecBuilder().build();
//        responseSpecification.statusCode(200);
//        responseSpecification.body("result.success", equalTo(true));
        }
    }

    @AfterAll
    static void userLogout() {
        ul.userLogout().then().body("result.success", equalTo(true));
    }
}
