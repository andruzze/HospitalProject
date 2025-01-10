package com.akulov.hospital;

import com.akulov.hospital.adapters.DatabaseAdapterImpl;
import com.akulov.hospital.configuration.DatabaseConfiguration;
import com.akulov.hospital.dao.DrugDAO;
import com.akulov.hospital.dao.SqlBuilder;
import com.akulov.hospital.model.dto.DTO;
import com.akulov.hospital.model.dto.DrugDTO;
import com.akulov.hospital.properties.DatabaseProperties;

import java.sql.SQLException;
import java.util.*;

public class Main {
    private final static String DBPATH = "src/com/akulov/hospital/application.properties";

    public static void main(String[] args) {

        DatabaseProperties dbProps = new DatabaseProperties(DBPATH);
        DatabaseConfiguration dbConf = new DatabaseConfiguration(dbProps);
        DatabaseAdapterImpl adapter = new DatabaseAdapterImpl(dbConf.getConnection());
        DrugDTO dragDTO = new DrugDTO(1, "Paracetomol", "tablets", "40mg", "Clinic", 10);

        DrugDAO drugDAO = new DrugDAO(adapter);
        Map<String, Object> values = new HashMap<>();
        values.put("id", 2);

        try{
            Collection<DrugDTO> res = drugDAO.get("*",values);

            if(res.isEmpty()){
                System.out.println(1);
            }
            for(DrugDTO r:res){

                System.out.println(r.getName());
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }











    }
}