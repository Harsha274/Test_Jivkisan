package com.jivkisan.utils;
 
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
public class ConfigReader {
    private static final Properties properties = new Properties();
 
    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties file not found in src/main/resources");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties", e);
        }
    }
 
    private ConfigReader() {}
 
    public static String get(String key) {
        return properties.getProperty(key);
    }
 
    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
 
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
}
 