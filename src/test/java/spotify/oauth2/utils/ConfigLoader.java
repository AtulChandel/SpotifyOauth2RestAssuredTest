package spotify.oauth2.utils;

import java.util.Properties;

public class ConfigLoader {
    private final Properties properties;
    private static ConfigLoader configLoader;

    private ConfigLoader() {
        properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");
    }

    public static ConfigLoader getInstance() {
        if (configLoader == null) {
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

    public String getClientId(){
        String prop = properties.getProperty("client_id");
        if(prop != null){
            return prop;
        }
        else{
            throw new RuntimeException("property is not present in config file");
        }
    }

    public String getClientSecret(){
        String prop = properties.getProperty("client_secret");
        if(prop != null){
            return prop;
        }
        else{
            throw new RuntimeException("property is not present in config file");
        }
    }

    public String getRefreshToken(){
        String prop = properties.getProperty("refresh_token");
        if(prop != null){
            return prop;
        }
        else{
            throw new RuntimeException("property is not present in config file");
        }
    }

    public String getGrantType(){
        String prop = properties.getProperty("grant_type");
        if(prop != null){
            return prop;
        }
        else{
            throw new RuntimeException("property is not present in config file");
        }
    }

    public String getUser(){
        String prop = properties.getProperty("user");
        if(prop != null){
            return prop;
        }
        else{
            throw new RuntimeException("property is not present in config file");
        }
    }
}