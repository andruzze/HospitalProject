package com.akulov.hospital.utils;

import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import java.lang.reflect.Field;
import java.util.*;

public class TableElemAnnotationParser {

    public static Map<String, Object> ParseFieldsMapping (Object obj){
        Map<String, Object> resMapping = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();
        Field[] classFields = clazz.getDeclaredFields();
        for(Field field:classFields){
            if(field.isAnnotationPresent(TableField.class)){
                TableField annotation = field.getAnnotation(TableField.class);
                field.setAccessible(true);
                try{
                    resMapping.put(annotation.collumnName(), field.get(obj));
                }catch (IllegalArgumentException | IllegalAccessException e){
                    System.out.println("Ошибка маппинга класса " + clazz.getName() + ": " + e.getMessage());
                }

            }else{
                throw new IllegalArgumentException("Аннотация @TableFiled отсутствует в поле " + field.getName());
            }

        }
        return resMapping;
    }

    public static  Collection<String> ParseFields(Class<?> clazz){
        List<String> resList = new ArrayList<>();
        Field[] classFields = clazz.getDeclaredFields();
        for(Field field:classFields){
            if (field.isAnnotationPresent(TableField.class)){
                resList.add(field.getAnnotation(TableField.class).collumnName());
            }else{
                throw new IllegalArgumentException("Аннотация @TableFiled отсутствует в поле " + field.getName());
            }
        }
        return resList;
    }

    public static String getTableName(Class<?> clazz ){
        if(clazz.isAnnotationPresent(TableName.class)){
            TableName classAnnot = clazz.getAnnotation(TableName.class);
            return classAnnot.tableName();
        }else{
            throw new IllegalArgumentException("У клаас " + clazz.getName() + " аннотация @TableName отсутствует");
        }

    }
}
