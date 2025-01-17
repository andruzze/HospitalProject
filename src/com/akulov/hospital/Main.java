package com.akulov.hospital;

import com.akulov.hospital.adapters.DatabaseAdapterImpl;
import com.akulov.hospital.configuration.DatabaseConfiguration;
import com.akulov.hospital.controller.ControllerImpl;
import com.akulov.hospital.dao.entitydao.DrugDAO;
import com.akulov.hospital.model.Model;
import com.akulov.hospital.model.ModelImpl;
import com.akulov.hospital.properties.DatabaseProperties;
import com.akulov.hospital.view.ViewImpl;

public class Main {
    private final static String DBPATH = "src/com/akulov/hospital/application.properties";

    public static void main(String[] args) {

        DatabaseProperties dbProps = new DatabaseProperties(DBPATH);
        DatabaseConfiguration dbConf = new DatabaseConfiguration(dbProps);
        DatabaseAdapterImpl adapter = new DatabaseAdapterImpl(dbConf.getConnection());

        Model model = new ModelImpl();
        DrugDAO drugDAO = new DrugDAO(adapter);
        ControllerImpl controller = new ControllerImpl(model, drugDAO);
        new ViewImpl(controller);

    }
}