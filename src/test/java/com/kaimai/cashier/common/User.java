package com.kaimai.cashier.common;

import java.util.HashMap;

import static com.kaimai.util.ReadYaml.readDataFromYaml;

public class User {
    private String merchantCode;
    private String userCode;
    private String password;

    private static User user;

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public User() {
        HashMap<String, Object> userInfo = readDataFromYaml("/user.yml");
        try {
            this.merchantCode = userInfo.get("merchantCode").toString();
            this.userCode = userInfo.get("userCode").toString();
            this.password = userInfo.get("password").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getPassword() {
        return password;
    }
}
