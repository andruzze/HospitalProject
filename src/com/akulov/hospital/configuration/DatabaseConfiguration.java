package com.akulov.hospital.configuration;

import com.akulov.hospital.properties.DatabaseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration implements AutoCloseable {
    private final Connection connection;

    public DatabaseConfiguration(DatabaseProperties dbProperties) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    dbProperties.getDatabaseUrl(),
                    dbProperties.getUsername(),
                    dbProperties.getPassword());
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к базе данных " + e.getMessage());
            // TODO Add exception handle
        }
        this.connection = conn;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
