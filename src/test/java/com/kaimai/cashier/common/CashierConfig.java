package com.kaimai.cashier.common;

import com.kaimai.util.ReadYaml;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Cookie;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.util.HashMap;

public class CashierConfig {
    private static CashierConfig cashCfg;

    public static CashierConfig getInstance() {
        if (cashCfg == null) {
            cashCfg = new CashierConfig();
        }
        return cashCfg;
    }

    public CashierConfig(){
        try {
            HashMap<String, Object> envInfo = ReadYaml.readDataFromYaml("/enviroment.yml");
            RestAssured.baseURI = envInfo.getOrDefault("URI", "http://gw3.daily.heyean.com").toString();
            RestAssured.basePath = envInfo.getOrDefault("basePath", "/cashier").toString();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        RestAssured.requestSpecification = builder.build();

//        RestAssured.proxy("192.168.50.216", 8888);
    }
}
