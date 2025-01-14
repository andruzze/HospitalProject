package com.akulov.hospital.model.dto;

import com.akulov.hospital.util.ParserDTO;

import java.util.Map;


public class DTOImpl implements DTO {

    private final ParserDTO parser = new ParserDTO();

    @Override
    public Map<String, Object> getFieldsValeus(){

        return parser.parseFieldsMapping(this);
    }

    @Override
    public String toString() {
        Map<String, Object> fieldVals = getFieldsValeus();
        StringBuilder sb = new StringBuilder().append("Object ").append(this.getClass().getSimpleName()).append("{");
        fieldVals.forEach((key, val) -> sb.append(key).append(":").append(val).append(","));
        sb.append("}");
        return sb.toString();
    }
}
