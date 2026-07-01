package com.saucedemo.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Centralised access to test configuration. Values are loaded from
 * {@code config.properties} on the classpath and may be overridden at runtime
 * via {@code -D} system properties (e.g. {@code -Dheadless=false}).
 */
public final class TestConfig {

    private static final Properties PROPS = load();

    private TestConfig() {
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            props.load(in);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
        return props;
    }

    private static String get(String key) {
        // System property wins so CI can override without touching the file.
        return System.getProperty(key, PROPS.getProperty(key));
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String password() {
        return get("password");
    }

    public static String standardUser() {
        return get("user.standard");
    }

    public static String lockedUser() {
        return get("user.locked");
    }

    public static String problemUser() {
        return get("user.problem");
    }

    public static String glitchUser() {
        return get("user.glitch");
    }

    public static String browser() {
        return get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static double timeout() {
        return Double.parseDouble(get("timeout"));
    }
}
