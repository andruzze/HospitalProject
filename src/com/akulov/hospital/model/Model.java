package com.akulov.hospital.model;

import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.DTO;

import java.util.Collection;
import java.util.Map;

public interface Model {
    <T extends DTO> Collection<T> getAllRecords(DataAccessObjectImpl<T> dao);
    <T extends DTO> Collection<T> getRecord(DataAccessObjectImpl<T> dao, Map<String, Object> conditions);
    <T extends DTO> void addRecord(DataAccessObjectImpl<T> dao, T dto);
    <T extends DTO> void updateRecord(DataAccessObjectImpl<T> dao, T dto, Map<String, Object> conditions);
    <T extends DTO> void deleteRecord(DataAccessObjectImpl<T> dao, Integer id);
    <T extends DTO> Object[][] recordsToArray(Collection<T> records);

}
