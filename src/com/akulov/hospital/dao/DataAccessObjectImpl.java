package com.akulov.hospital.dao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.model.dto.DTO;
import com.akulov.hospital.util.ParserDTO;
import java.sql.*;
import java.util.*;

public abstract class DataAccessObjectImpl<T extends DTO> implements DataAccessObject<T> {

    private final DatabaseAdapter adapter;
    protected final ParserDTO parser = new ParserDTO();

    protected DataAccessObjectImpl(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    abstract String getTableName();
    abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    @Override
    public Collection<T> get(String gettingField, Map<String, Object> fieldValueParams) {
        List<T> entityArr = new ArrayList<T>();
        StringBuilder queryBuilder = new StringBuilder()
                .append("SELECT ")
                .append(gettingField)
                .append(" FROM ")
                .append(getTableName())
                .append(" WHERE ");
        fieldValueParams.forEach((key, value) -> queryBuilder.append(key).append(" = ? AND "));
        String query = queryBuilder.substring(0, queryBuilder.length() - 5);
        try {
            ResultSet rows = adapter.executeQuery(query, fieldValueParams.values().toArray());
            while(rows.next()){
                entityArr.add(mapResultSetToEntity(rows));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return entityArr;
    }

    @Override
    public void insert(T dtoObj) {
        Map<String, Object> fieldMap = dtoObj.getFieldsValeus();
        Set<String> fields = fieldMap.keySet();
        Object[] vals = fieldMap.values().toArray();
        StringBuilder query = new StringBuilder()
                .append("INSERT INTO ")
                .append(getTableName())
                .append(" (");
        for(String field:fields){
            query.append(field).append(",");
        }
        query.setLength(query.length()-1);
        query.append(") VALUES(")
            .append("?,".repeat(vals.length));
        query.setLength(query.length()-1);
        query.append(")");
        try {
            adapter.executeUpdate(query.toString(), vals);
        }catch (SQLException e){
            System.out.println("Ошибка вставки строки в таблицу.\nQUERY - " + query.toString() + "\nVALUES - ");
            for(Object val:vals){
                System.out.print(val + ",");
            }
            System.out.println("\n" + e.getMessage());
        }
    }

    @Override
    public void update(T dtoObj, Map<String, Object> conditions) {
        Map<String, Object> fieldMap = dtoObj.getFieldsValeus();
        Collection<Object> vals = fieldMap.values();
        Set<String> fields = fieldMap.keySet();
        StringBuilder query = new StringBuilder()
                .append("UPDATE ")
                .append(getTableName())
                .append(" SET ");
        fieldMap.forEach((key, value) -> query.append(key).append("=").append(value).append(","));
        query.setLength(query.length()-1);
        query.append(" WHERE ");
        conditions.forEach((key, value) -> query.append(key).append("=").append(value).append(" AND "));
        query.setLength(query.length()-5);
        System.out.println(query.toString());
        try {
            adapter.executeUpdate(query.toString());
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }



    }

    @Override
    public void delete(String... args) {
        StringBuilder query = new StringBuilder().append("DELETE FROM ").append(getTableName()).append(" WHERE ").append("id = ").append("?");
        try{
            adapter.executeUpdate(query.toString(), args);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
