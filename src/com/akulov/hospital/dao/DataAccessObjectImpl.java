package com.akulov.hospital.dao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.model.dto.DTO;
import com.akulov.hospital.model.dto.types.FullName;
import com.akulov.hospital.model.dto.types.Passport;
import com.akulov.hospital.util.ParserDTO;
import java.sql.*;
import java.util.*;

public abstract class DataAccessObjectImpl<T extends DTO> implements DataAccessObject<T> {

    private final DatabaseAdapter adapter;
    protected final ParserDTO parser = new ParserDTO();

    protected DataAccessObjectImpl(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    public abstract String getTableName();

    public abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    @Override
    public Collection<T> get(String gettingField, Map<String, Object> fieldValueParams) {
        List<T> entityArr = new ArrayList<T>();
        StringBuilder queryBuilder = new StringBuilder()
                .append("SELECT ")
                .append(gettingField)
                .append(" FROM ")
                .append(getTableName());
        String query = queryBuilder.toString();
        if(!fieldValueParams.isEmpty()) {
            queryBuilder.append(" WHERE ");
            fieldValueParams.forEach((key, value) -> queryBuilder.append(key).append(" = ? AND "));
            query = queryBuilder.substring(0, queryBuilder.length() - 5);
        }
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
            if(!Objects.equals(field, "id")){
                query.append(field).append(",");
            }
        }
        query.setLength(query.length()-1);
        query.append(") VALUES(");
        for(int i = 1; i < vals.length; i++){
            if (vals[i].getClass().equals(FullName.class)){
                query.append("(?::full_name),");
                vals[i] = vals[i].toString();
            } else if (vals[i].getClass().equals(Passport.class)) {
                query.append("(?::passport),");
                vals[i] = vals[i].toString();
            } else{
                query.append("?,");
            };
        }
        query.setLength(query.length()-1);
        query.append(")");
        try {
            adapter.executeUpdate(query.toString(), Arrays.copyOfRange(vals, 1, vals.length));
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
        Collection<Object> args = new ArrayList<>();

        StringBuilder query = new StringBuilder()
                .append("UPDATE ")
                .append(getTableName())
                .append(" SET ");
        for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            query.append(key).append("=");
            if(value.getClass().equals(FullName.class)){
                query.append("(?::full_name)").append(",");
                args.add(value.toString());
            } else if (value.getClass().equals(Passport.class)) {
                query.append("(?::passport)").append(",");
                args.add(value.toString());
            }else{
                query.append("?").append(",");
                args.add(value);
            }
        }
        args.addAll(conditions.values());
        query.setLength(query.length()-1);
        query.append(" WHERE ");
        conditions.forEach((key, value) -> query.append(key).append("=").append("?").append(" AND "));
        query.setLength(query.length()-5);
        try {
            adapter.executeUpdate(query.toString(), args.toArray());
        }catch (SQLException e){
            System.out.println(query);
            args.forEach(val -> System.out.println(val));
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer... args) {
        StringBuilder query = new StringBuilder().append("DELETE FROM ").append(getTableName());
        if(args.length > 0){
            query.append(" WHERE ").append("id=").append("?");
        }
        try{
            adapter.executeUpdate(query.toString(), args);
        }catch (SQLException e){
            System.out.println(query);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
