package com.akulov.hospital.model.dto.entity;

public class Column {
    private final String name;
    private final Class<?> type;
    private final boolean isNull;
    private Object value;
    private final boolean privaryKey = false;
    private boolean foreignKey =false;
    private Column foreignColumn = null;

    public Column(String name, Class<?> type, boolean isNull){
        this.name = name;
        this.type = type;
        this.isNull = isNull;
    }

    public void setValue(Object val){
        value = val;
    }

    public String equally(Object obj){
        return concatenetion("=", obj);
    }

    public String gt(Object obj){
        return concatenetion(">", obj);
    }

    public String egt(Object obj){
        return concatenetion(">=", obj);
    }

    public String lt(Object obj){
        return concatenetion("<", obj);
    }

    public String elt(Object obj){
        return concatenetion("<=", obj);
    }

    public String notEqueally(Object obj){
        return concatenetion("<>", obj);
    }


    private String concatenetion(String operator, Object obj){
        return value.toString() + operator + obj;
    }
}
