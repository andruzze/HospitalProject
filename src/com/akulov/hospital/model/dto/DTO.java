package com.akulov.hospital.model.dto;

import com.akulov.hospital.utils.TableElemAnnotationParser;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public abstract class DTO implements MapperDTO{

    public static  Collection<String> getCollumns(Class<?> cls){
        return TableElemAnnotationParser.ParseFields(cls);
    }

    @Override
    public Map<String, Object> getFieldsValeus(){
        return TableElemAnnotationParser.ParseFieldsMapping(this);
    }

    public static String getTableName(Class<?> cls){
        return TableElemAnnotationParser.getTableName(cls);
    }

}
