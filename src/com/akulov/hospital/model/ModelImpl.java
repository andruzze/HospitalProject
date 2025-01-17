package com.akulov.hospital.model;

import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.DTO;
import com.akulov.hospital.util.ParserDTO;

import java.util.Collection;
import java.util.Map;

public class ModelImpl implements Model {

    @Override
    public <T extends DTO> Collection<T> getAllRecords(DataAccessObjectImpl<T> dao) {
        return dao.get("*", Map.of());
    }
    @Override
    public  <T extends DTO> Collection<T> getRecord(DataAccessObjectImpl<T> dao, Map<String, Object> conditions){
        return dao.get("*", conditions);
    }

    @Override
    public <T extends DTO> void addRecord(DataAccessObjectImpl<T> dao, T dto) {
        dao.insert(dto);
    }

    @Override
    public <T extends DTO> void updateRecord(DataAccessObjectImpl<T> dao, T dto, Map<String, Object> conditions) {
        dao.update(dto, conditions);
    }

    @Override
    public <T extends DTO> void deleteRecord(DataAccessObjectImpl<T> dao, Integer id) {
        dao.delete(id);
    }

    @Override
    public <T extends DTO> Object[][] recordsToArray(Collection<T> records) {
        ParserDTO parserDTO = new ParserDTO();
        Object record = records.toArray()[0];
        int fieldCOunt = parserDTO.parseFields(record.getClass()).size();
        return new Object[records.size()][fieldCOunt];
    }
}
