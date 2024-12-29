package com.akulov.hospital;

import com.akulov.hospital.adapters.DatabaseAdapterImpl;
import com.akulov.hospital.configuration.DatabaseConfiguration;
import com.akulov.hospital.dao.DrugDAO;
import com.akulov.hospital.model.dto.DrugDTO;
import com.akulov.hospital.properties.DatabaseProperties;
import com.akulov.hospital.utils.TableElemAnnotationParser;

import java.sql.SQLException;
import java.util.*;

public class Main {
    private final static String DBPATH = "src/com/akulov/hospital/application.properties";

    public static void main(String[] args) {

        DatabaseProperties dbProps = new DatabaseProperties(DBPATH);

        DatabaseConfiguration dbConf = new DatabaseConfiguration(dbProps);
        DatabaseAdapterImpl adapter = new DatabaseAdapterImpl(dbConf.getConnection());
        DrugDAO drugs = new DrugDAO(adapter);
        DrugDTO dragDTO = new DrugDTO(1, "Paracetomol", "tablets", "40mg", "Clinic", 10);

//        Collection<String> res = TableElemAnnotationParser.ParseFields(DrugDTO.class);
        Map<String, Object> params = new HashMap<>();
        params.put("id", dragDTO.getId());
        drugs.update(dragDTO,params);

//
//        try{
//            Collection<DrugDTO> res = drugs.get("*", params);
//            for(DrugDTO el : res){
//                System.out.println(el.getName());
//            }
//        }catch (SQLException e){
//            System.out.println(e.getMessage());
//        }









    }
}