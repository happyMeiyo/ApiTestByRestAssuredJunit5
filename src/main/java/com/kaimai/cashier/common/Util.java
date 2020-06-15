package com.kaimai.cashier.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

public class Util {
    public static HashMap<String, Object> readDataFromYaml(String filePath) {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        TypeReference<HashMap<String, Object>> typeRef =
                new TypeReference<HashMap<String, Object>>() {
                };

        HashMap<String, Object> data = null;
        try {
            InputStream src = Util.class.getResourceAsStream(filePath);
            data = mapper.readValue(src,typeRef);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

    }

    public static String template(String templatePath, HashMap<String, Object> data){
        Writer writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(Util.class.getResource(templatePath).getPath());
        mustache.execute(writer, data);
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        return writer;
        return writer.toString();
    }
}


