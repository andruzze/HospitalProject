package com.akulov.hospital.model;

import com.akulov.hospital.model.dto.entity.*;
import org.postgresql.util.PSQLException;

import java.util.Collection;
import java.util.Map;

public interface Model {
    Collection<Object> getDrugStoregeById(int id);
    Collection<DrugDTO> getDrugsRecords(Map<String, Object> conditions);
    Collection<DrugDTO> getAllDrugRecords();
    Collection<StoreDTO> getAllStores();
    Integer getAuthUserId();
    Integer getEmployeeId(Map<String, Object> conditions);
    Integer getPatientId(Map<String, Object> conditions);
    boolean authEmployee(String login, String pass);
    void addDrug(DrugDTO drug);
    void addStore(StoreDTO storeDTO);
    void updateDrug(DrugDTO drugDTO, Map<String, Object> conditions);
    void updateStore(StoreDTO updatedStore, Map<String, Object> conditions);
    void deleteDrug(Integer id);
    void deleteStore(Integer id);
    void addTransaction(TransactionDTO transaction) throws PSQLException;
    void addTransaction(TransactionDTO transaction, Integer patientId) throws PSQLException;
    Collection<TransactionDTO> getAllTransactions();
    Collection<TransactionDTO> getTransactions(Map<String,Object> conditions);
    Collection<EmployeeDTO> getEmployees(Map<String, Object> conditions);
    Collection<DepartmentDTO> getDepartments(Map<String, Object> conditions);
    Collection<StoreDTO> getStores(Map<String, Object> conditions);
    void addInventory(InventoryDTO inventoryDTO);
    Collection<InventoryDetailsDTO> getInventoryDetails(Map<String, Object> conditions);
    Collection<InventoryDTO> getAllInventories();
    String[] get_detail_stores_raws();
    void addInventoryDetails(Integer inventoryId, Integer store_id, Integer drugId, Integer drugsCount) throws PSQLException;

    void deleteInventoryDetail(Object id);

    void deleteTransaction(Integer transactionId);

    void deleteInventory(Integer id);

    Collection<InventoryDTO> getInventories(Map<String, Object> conditions);

    Collection<StoreDrugsDTO> getDrugsStore(Map<String, Object> conditions);

    Collection<DispensingDTO> getDispensing(Map<String, Object> conditions);

    Collection<PatientDTO> getPatients(Map<String, Object> conditions);

    void signInventory(int inventoryId, int authUserId);

    Collection<DispensingDTO> getAllDispensing();

    void issueDrug(int dispensingId, int adminId);
}
