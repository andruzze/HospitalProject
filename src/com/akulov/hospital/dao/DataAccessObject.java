package com.akulov.hospital.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;


public interface DataAccessObject<T> {

    Collection<T> get(String gettingField, Map<String, Object> fieldValueParams) throws SQLException;

    void insert(T t) ;

    void update(T t, Map<String, Object> conditions);

    void delete(Integer... args);

}
