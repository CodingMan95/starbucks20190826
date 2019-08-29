package com.eim.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/**
 * 在普通类里面拿配置文件里面得属性值
 */
public class PropertiesUtils {

    private static String PROPERTY_NAME = "application-dev.yml";

    public static Object getCommonYml(Object key){
        ClassPathResource resource = new ClassPathResource(PROPERTY_NAME);
        Properties properties = null;
        try {
            YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
            yamlFactory.setResources(resource);
            properties =  yamlFactory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return properties.get(key);
    }

    public static void main(String[] args) {
        System.out.println(getCommonYml("spring.redis.host"));
    }

}
