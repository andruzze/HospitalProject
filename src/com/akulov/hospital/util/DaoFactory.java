package com.akulov.hospital.util;

import com.akulov.hospital.adapters.DatabaseAdapterImpl;
import com.akulov.hospital.dao.entitydao.*;

public class DaoFactory {
    private final DatabaseAdapterImpl adapter;
    public DaoFactory(DatabaseAdapterImpl adapter){
        this.adapter = adapter;
    }

    public DrugDAO getDrugDAO(){
        return new DrugDAO(adapter);
    }

    public DepartmentDAO getDepartmentDAO(){
        return new DepartmentDAO(adapter);
    }

    public DispensingDAO getDispensingDAO(){
        return new DispensingDAO(adapter);
    }

    public EmployeeDAO getEmployeeDAO(){
        return new EmployeeDAO(adapter);
    }

    public InventoryDAO getInventoryDAO(){
        return new InventoryDAO(adapter);
    }

    public InventoryDetailsDAO getInventoryDetailsDAO(){
        return new InventoryDetailsDAO(adapter);
    }

    public OrderDAO getOrderDAO(){
        return new OrderDAO(adapter);
    }

    public OrderDetailsDAO getOrderDetailsDAO(){
        return new OrderDetailsDAO(adapter);
    }

    public PatientDAO getPatientDAO(){
        return new PatientDAO(adapter);
    }

    public StoreDAO getStoreDAO() {
        return new StoreDAO(adapter);
    }

    public StoreDrugsDAO getStoreDrugDAO(){
        return new StoreDrugsDAO(adapter);
    }

    public TransactionDAO getTransactionDAO(){
        return new TransactionDAO(adapter);
    }
}
