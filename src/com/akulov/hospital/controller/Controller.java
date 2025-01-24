package com.akulov.hospital.controller;

import com.akulov.hospital.model.dto.entity.*;
import org.postgresql.util.PSQLException;

import java.util.Collection;
import java.util.Map;

public interface Controller {

    Collection<DrugDTO> getAllDrugs();
    void addDrug(DrugDTO drug);
    void updateDrug(DrugDTO drug, Map<String, Object> condition);
    void deleteDrug(Integer id);
    void updateStore(StoreDTO updatedStore, Map<String, Object> conditions);
    void deleteStore(Integer id);
    void addStore(StoreDTO storeDTO);
    Collection<StoreDTO> getAllStores();
    Collection<DrugDTO> getDrugs(Map<String, Object> conditions);
    Collection<Object> getStoregeDrugsById(int id);
    Integer getUserId(Map<String, Object> conditions);
    EmployeeDTO getUser(Integer id);
    Collection<EmployeeDTO> getUsers(Map<String, Object> conditions);
    boolean authEmployee(String login, String pass);
    Integer getAuthUser();
    void addTransaction(TransactionDTO transaction) throws PSQLException;
    void addTransaction(TransactionDTO transactionDTO, Integer patientId) throws PSQLException;
    Integer getPatientId(Map<String, Object> conditions);
    Collection<TransactionDTO> getAllTransactions();
    void deleteTransaction(Integer transactionId);
    TransactionDTO getTransactionById(Integer transactionId);
    Collection<TransactionDTO> searchTransactions(String column, String searchText);
    DrugDTO getDrugById(Object id);
    EmployeeDTO getEmployeeById(Object id);

    DepartmentDTO getDepartmentById(Integer departmentId);

    StoreDTO getStoresById(Object id);

    Collection<InventoryDTO> getAllInventories();

    Collection<InventoryDTO> searchInventories(String column, String searchText);

    Collection<InventoryDetailsDTO> getInventoryDetails(Integer inventoryId);

    void addInventory(InventoryDTO inventoryDTO);

    String[] getStoresNames();

    void addInventoryDetails(Integer inventoryId,Integer storeID, Integer drugId, Integer drugsCount) throws PSQLException;

    void deleteInventoryDetail(Object id);

    void deleteInventory(Integer valueAt);

    StoreDTO getStoreById(Object storeId);

    Collection<StoreDrugsDTO> getDrugsByStoreId(Integer storeId);

    Collection<DispensingDTO> getDispensing(Map<String, Object> conditions);

    Collection<PatientDTO> getParients(Map<String,Object> conditions);

    Collection<DrugDTO> searchDrugs(String column, String arg);

    Collection<StoreDTO> searchStores(String column, String arg);

    void signInventory(int inventoryId,int authUserId);

    Collection<DispensingDTO> getAllDispensing();

    PatientDTO getPatientById(Integer patientId);

    PatientDTO getPatientByPolicy(String policyNumber);

    Collection<DispensingDTO> getDispensingByPatientId(Integer id);

    void issueDrug(int dispensingId, int adminId);
}
