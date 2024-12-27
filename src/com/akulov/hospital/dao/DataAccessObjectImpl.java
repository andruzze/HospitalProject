package com.akulov.hospital.dao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import java.sql.*;
import java.util.*;

public abstract class DataAccessObjectImpl<T> implements DataAccessObject<T> {
    private final DatabaseAdapter adapter;

    protected DataAccessObjectImpl(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    abstract String getTableName();
    abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    @Override
    public Collection<T> get(String gettingField, Map<String, Object> fieldValueParams) throws SQLException {
        List<T> entityArr = new ArrayList<T>();
        StringBuilder queryBuilder = new StringBuilder()
                .append("SELECT ")
                .append(gettingField)
                .append(" FROM ")
                .append(getTableName())
                .append(" WHERE ");
        fieldValueParams.forEach((key, value) -> queryBuilder.append(key).append(" = ? AND "));
        String query = queryBuilder.substring(0, queryBuilder.length() - 5);
        ResultSet rows = adapter.executeQuery(query, fieldValueParams.values().toArray());
        System.out.println(query);
        while(rows.next()){
            entityArr.add(mapResultSetToEntity(rows));
        }
        return entityArr;
    }

    @Override
    public void insert(T t) {

    }

    @Override
    public void update(T t, String... args) {

    }

    @Override
    public void delete(T t) {

    }
}
