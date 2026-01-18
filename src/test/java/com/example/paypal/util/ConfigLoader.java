package com.example.paypal.util;


import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final Properties properties = new Properties();
    private static boolean loaded = false;

    private ConfigLoader() {}

    public static void load() {
        if (loaded) return;

        try {
            loadFile("config/env.properties");
            loadFile("config/config.properties");

            // Set all properties to System properties
            properties.forEach((key, value) -> {
            	if(System.getProperty((String) key)==null)
                System.setProperty(key.toString(), value.toString());
            });

            loaded = true;

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to load configuration files", e);
        }
    }

    private static void loadFile(String filePath) throws Exception {
        try (InputStream is = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream(filePath)) {

            if (is == null) {
                throw new RuntimeException("File not found: " + filePath);
            }
            properties.load(is);
        }
    }
}
