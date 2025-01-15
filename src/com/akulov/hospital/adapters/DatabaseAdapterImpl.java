package com.akulov.hospital.adapters;
import com.akulov.hospital.model.dto.types.FullName;
import com.akulov.hospital.model.dto.types.Passport;

import java.sql.*;

public class DatabaseAdapterImpl implements DatabaseAdapter {

    private final Connection conn;
    public  DatabaseAdapterImpl(Connection conn){
        this.conn = conn;
    }

    @Override
    public ResultSet executeQuery(String query, Object... args) throws SQLException {
        ResultSet result = null;
        result = prepare(query, args).executeQuery();
        return result;
    }

    @Override
    public Integer executeUpdate(String query, Object... args) throws  SQLException{
        Integer result = null;
        result = prepare(query, args).executeUpdate();
        return result;
    }

    private PreparedStatement prepare(String query, Object... args) throws SQLException {
        PreparedStatement preparedStatement = null;
        preparedStatement = conn.prepareStatement(query);
        setParameters(preparedStatement, args);
        return preparedStatement;
    }

    private static void setParameters(PreparedStatement statement, Object... args) throws SQLException{
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i + 1, args[i]);
        }
    }
}
