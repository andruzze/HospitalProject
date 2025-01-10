package com.akulov.hospital.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseProperties {

    private final Properties properties;

    public DatabaseProperties(String filePath) {
        this.properties = new Properties();
        try{
            FileInputStream file = new FileInputStream(filePath);
            properties.load(file);
        }catch (IOException e){
            System.out.println("Не удалось открыть файл конфигураций " + e.getMessage());

        }

    }
    public String getDatabaseUrl() {
        return properties.getProperty("jdbc.url");
    }

    public String getPassword() {
        return properties.getProperty("jdbc.password");
    }

    public String getUsername() {
        return properties.getProperty("jdbc.username");
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty("jdbc.port"));
    }
}
