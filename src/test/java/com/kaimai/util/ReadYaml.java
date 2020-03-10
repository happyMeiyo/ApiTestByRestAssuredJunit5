package com.kaimai.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ReadYaml {
    public static HashMap<String, Object> readDataFromYaml(String filePath) {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        TypeReference<HashMap<String, Object>> typeRef =
                new TypeReference<HashMap<String, Object>>() {
                };

        HashMap<String, Object> data = null;
        try {
            InputStream src = ReadYaml.class.getResourceAsStream(filePath);
            data = mapper.readValue(src,typeRef);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

    }
}
