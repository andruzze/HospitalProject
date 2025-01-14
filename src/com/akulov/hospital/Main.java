package com.akulov.hospital;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.adapters.DatabaseAdapterImpl;
import com.akulov.hospital.configuration.DatabaseConfiguration;
import com.akulov.hospital.dao.EmployeeDAO;
import com.akulov.hospital.model.dto.DepartmentDTO;
import com.akulov.hospital.model.dto.DrugDTO;
import com.akulov.hospital.model.dto.EmployeeDTO;
import com.akulov.hospital.properties.DatabaseProperties;
import com.akulov.hospital.util.ParserDTO;

import java.util.*;

public class Main {
    private final static String DBPATH = "src/com/akulov/hospital/application.properties";

    public static void main(String[] args) {

        DatabaseProperties dbProps = new DatabaseProperties(DBPATH);
        DatabaseConfiguration dbConf = new DatabaseConfiguration(dbProps);
        DatabaseAdapterImpl adapter = new DatabaseAdapterImpl(dbConf.getConnection());
        testQuery(adapter);
//        testParser();

    }
    private static void testParser(){
        ParserDTO parserDTO = new ParserDTO();
        DepartmentDTO departmentDTO = new DepartmentDTO(1, "Cardiologi", "89527086706", 0);
        DrugDTO drugDTO = new DrugDTO(1, "somename", "somerealise", "50mg", "supp", 20);
        System.out.println(departmentDTO.getFieldsValeus());
    }

    private static void testQuery(DatabaseAdapter adapter){


        EmployeeDAO employeeDAO = new EmployeeDAO(adapter);
        Collection<EmployeeDTO> res =  employeeDAO.get("*", new HashMap<>());
        res.forEach(System.out::println);
//        try{
//
//
//        }catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
    }
}