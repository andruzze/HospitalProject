package com.akulov.hospital.adapters;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseAdapter  {
    ResultSet executeQuery(String query, Object... args) throws SQLException;
    Integer executeUpdate(String query, Object... args) throws  SQLException;
}
