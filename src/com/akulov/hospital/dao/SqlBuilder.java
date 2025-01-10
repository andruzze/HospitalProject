package com.akulov.hospital.dao;

import java.util.*;
import java.util.function.Predicate;

public class SqlBuilder<T>{
    private final String tableName;
    private List<String> selectedColumns;
    private List<String> filters;
    private List<Object> orders;
    private Map<Object, String> updateValues;
    private Map<Object, String> insertValues;
    private OperationType operation;

    public enum OperationType{
        SELECT,
        UPDATE,
        INSERT,
        DELETE
    }

    public SqlBuilder(String tableName){
        this.tableName = tableName;
        this.selectedColumns = new ArrayList<>();
        this.filters = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.updateValues = new HashMap<>();
        this.insertValues = new HashMap<>();
        this.operation = OperationType.SELECT;

    }

    public SqlBuilder insert(Map<Object, String> insertVals){
        this.operation = OperationType.INSERT;
        insertValues = insertVals;
        return this;
    }

    public SqlBuilder select(String... columns){
        operation = OperationType.SELECT;
        selectedColumns.addAll(Arrays.asList(columns));
        return this;
    }

    public SqlBuilder filter(String... conditions){
        filters.addAll(Arrays.asList(conditions));
        return this;
    }

    public SqlBuilder order(String... conditions){
        orders.addAll(Arrays.asList(conditions));
        return this;
    }

    public SqlBuilder update(Map<Object, String> updateVals){
        this.operation = OperationType.UPDATE;
        updateValues = updateVals;
        return this;
    }

    public SqlBuilder delete(){
        this.operation = OperationType.DELETE;
        return this;
    }

    public String build() {

        StringBuilder query = new StringBuilder();

        switch (operation) {
            case SELECT:
                buildSelect(query);
                break;
            case INSERT:
                buildInsert(query);
                break;
            case UPDATE:
                buildUpdate(query);
                break;
            case DELETE:
                buildDelete(query);
                break;
        }

        return query.toString();
    }
    private void addFilter(StringBuilder query){
        if(!filters.isEmpty()){
            query.append(" WHERE ");
            query.append(String.join(" AND ", filters));
        }
    }


    private void buildSelect(StringBuilder query){
        query.append("SElECT ");
        if(selectedColumns.isEmpty()){
            query.append("*");
        }else{
            query.append(String.join(", ", selectedColumns));
        }
        query.append(" FROM ").append(tableName);
        addFilter(query);
    }

    private void buildInsert(StringBuilder query){
    }

    private void buildUpdate(StringBuilder query){
    }

    private  void buildDelete(StringBuilder query){

    }
}
