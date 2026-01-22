package com.saucedemo.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config file");
        }
        return value;
    }

    public static String getAppUrl() {
        return getProperty("app.url");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait"));
    }

    public static String getValidUsername() {
        return getProperty("valid.username");
    }

    public static String getValidPassword() {
        return getProperty("valid.password");
    }

    public static String getLockedUsername() {
        return getProperty("locked.username");
    }

    public static String getLockedPassword() {
        return getProperty("locked.password");
    }

    public static String getInvalidUsername() {
        return getProperty("invalid.username");
    }

    public static String getInvalidPassword() {
        return getProperty("invalid.password");
    }

    public static String getTestFirstName() {
        return getProperty("test.firstname");
    }

    public static String getTestLastName() {
        return getProperty("test.lastname");
    }

    public static String getTestPostalCode() {
        return getProperty("test.postalcode");
    }

    public static String getScreenshotDir() {
        return getProperty("screenshot.dir");
    }

    public static boolean isScreenshotEnabled() {
        return Boolean.parseBoolean(getProperty("screenshot.enabled"));
    }
}
