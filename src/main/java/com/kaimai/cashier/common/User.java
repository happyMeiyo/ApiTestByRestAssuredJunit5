package com.kaimai.cashier.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;
import java.util.HashMap;

public class User {
    private String merchantCode;
    private String userCode;
    private String password;
    private String oprPassword;

    private static User user;

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public User() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeReference<HashMap<String, Object>> typeRef =
                new TypeReference<HashMap<String, Object>>() {
                };

        try {
            InputStream src = User.class.getResourceAsStream("user.yml");
            HashMap<String, Object> userInfo = mapper.readValue(src,typeRef);

            this.merchantCode = userInfo.get("merchantCode").toString();
            this.userCode = userInfo.get("userCode").toString();
            this.password = userInfo.get("password").toString();
            this.oprPassword = userInfo.get("oprPassword").toString();
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

    public String getOprPassword() {
        return oprPassword;
    }
}
