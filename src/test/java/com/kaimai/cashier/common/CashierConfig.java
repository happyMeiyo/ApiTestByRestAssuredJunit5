package com.kaimai.cashier.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;

import java.io.InputStream;
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
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeReference<HashMap<String, Object>> typeRef =
                new TypeReference<HashMap<String, Object>>() {
                };

        try {
            InputStream src = CashierConfig.class.getResourceAsStream("enviroment.yml");
            HashMap<String, Object> envInfo = mapper.readValue(src,typeRef);

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
