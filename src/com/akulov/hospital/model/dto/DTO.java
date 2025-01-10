package com.akulov.hospital.model.dto;

import com.akulov.hospital.util.ParserDTO;

import java.util.Map;


public class DTO implements com.akulov.hospital.model.dto.MapperDTO {

    private final ParserDTO parser = new ParserDTO();

    @Override
    public Map<String, Object> getFieldsValeus(){
        return parser.parseFieldsMapping(this.getClass());
    }


}
