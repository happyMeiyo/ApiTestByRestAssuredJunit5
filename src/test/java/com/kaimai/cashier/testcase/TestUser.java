package com.kaimai.cashier.testcase;

import com.kaimai.cashier.api.UserLogin;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


import static org.hamcrest.Matchers.equalTo;

public class TestUser {
    static UserLogin ul = new UserLogin();

    @BeforeAll
    static void userLogin() {
        Response userLoginRsp = ul.userLoginSuccess();

        if (userLoginRsp.getStatusCode() == 200) {
            boolean success = userLoginRsp.getBody().jsonPath().get("result.success");
            if (success) {
                String token = userLoginRsp.then().
                        extract().path("data.accessToken");
                //使用Filter方法，在请求中添加cookie
//                RestAssured.filters((req, res, ctx) -> {
//                    //请求头中添加Cookie
//                    req.header("Cookie", String.format("hsAccessToken=%s", token));
//                    //发送请求，返回响应
//                    return ctx.next(req, res);
//                });

                //添加请求中的cookie
                RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
                reqBuilder.addHeader("Cookie", String.format("hsAccessToken=%s", token));
                RestAssured.requestSpecification = reqBuilder.build();
            }
            //响应消息的基础判断
            ResponseSpecBuilder respBuilder = new ResponseSpecBuilder();
            respBuilder.expectStatusCode(200);
            respBuilder.expectBody("result.success", equalTo(true));
            RestAssured.responseSpecification = respBuilder.build();
        }
    }

    @AfterAll
    static void userLogout() {
        ul.userLogout().then().body("result.success", equalTo(true));
    }
}
