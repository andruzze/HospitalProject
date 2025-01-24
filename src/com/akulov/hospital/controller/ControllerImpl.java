package com.akulov.hospital.controller;

import com.akulov.hospital.dao.entitydao.DrugDAO;
import com.akulov.hospital.model.Model;
import com.akulov.hospital.model.dto.entity.*;
import org.postgresql.util.PSQLException;
import java.time.LocalDate;
import java.util.*;


public class ControllerImpl implements Controller {
    private final Model model;
    private final DrugDAO drugDAO;

    public ControllerImpl(Model model, DrugDAO drugDAO) {
        this.model = model;
        this.drugDAO = drugDAO;
    }
    public Collection<DrugDTO> getAllDrugs() {
        return model.getAllDrugRecords();
    }

    public void addDrug(DrugDTO drug) {
        model.addDrug(drug);
    }

    public void updateDrug(DrugDTO drug, Map<String, Object> condition) {
        model.updateDrug(drug, condition);
    }

    public void deleteDrug(Integer id) {
        model.deleteDrug(id);
    }

    public void updateStore(StoreDTO updatedStore, Map<String, Object> conditions) {
        model.updateStore(updatedStore, conditions);
    }

    public void deleteStore(Integer id) {
        model.deleteStore(id);
    }

    public void addStore(StoreDTO storeDTO) {
        model.addStore(storeDTO);
    }

    public Collection<StoreDTO> getAllStores() {
        return model.getAllStores();
    }

    public Collection<DrugDTO> getDrugs(Map<String, Object> conditions) {
        return model.getDrugsRecords(conditions);
    }

    public Collection<Object> getStoregeDrugsById(int id){
        return model.getDrugStoregeById(id);
    }

    public Integer getUserId(Map<String, Object> conditions) {
        return model.getEmployeeId(conditions);
    }

    public EmployeeDTO getUser(Integer id){
        return null;
    }

    public Collection<EmployeeDTO> getUsers(Map<String, Object> conditions){
        return null;
    }

    public boolean authEmployee(String login, String pass) {
        return model.authEmployee(login, pass);
    }

    public Integer getAuthUser(){
        return model.getAuthUserId();
    }

    public void addTransaction(TransactionDTO transaction) throws PSQLException {
        model.addTransaction(transaction);

    }
    public void addTransaction(TransactionDTO transactionDTO, Integer patientId) throws PSQLException {
        model.addTransaction(transactionDTO, patientId);
    }

    public Integer getPatientId(Map<String, Object> conditions) {
        return model.getPatientId(conditions);
    }

    @Override
    public Collection<TransactionDTO> getAllTransactions() {
        return  model.getAllTransactions();
    }

    @Override
    public void deleteTransaction(Integer transactionId) {
        model.deleteTransaction(transactionId);
    }

    @Override
    public TransactionDTO getTransactionById(Integer transactionId) {
        return null;
    }

    @Override
    public Collection<TransactionDTO> searchTransactions(String column, String searchText) {
        String tableCol = null;
        Object valCol = null;
        switch (column){
            case "ID":
                tableCol = "id";
                valCol = Integer.parseInt(searchText);
                break;
            case "Тип":
                tableCol = "transaction_type";
                valCol = searchText;
                break;
            case "ID Препарата":
                tableCol = "drug_id";
                valCol = Integer.parseInt(searchText);
                break;
            case "Кол-во препарата":
                tableCol = "drugs_count";
                valCol = Integer.parseInt(searchText);
                break;
            case "ID Сотрудника":
                tableCol = "employee_id";
                valCol = Integer.parseInt(searchText);
                break;
            case "Дата операции":
                tableCol = "operation_date";
                valCol = LocalDate.parse(searchText);
                break;
            case "ID Склада" :
                tableCol = "store_id";
                valCol = Integer.parseInt(searchText);
                break;
        }

        return model.getTransactions(Map.of(tableCol, valCol));
    }

    @Override
    public DrugDTO getDrugById(Object id) {
        return model.getDrugsRecords(Map.of("id", id)).stream().findFirst().orElse(null);
    }

    @Override
    public EmployeeDTO getEmployeeById(Object id) {
        return model.getEmployees(Map.of("id", id)).stream().findFirst().orElse(null);
    }

    @Override
    public DepartmentDTO getDepartmentById(Integer departmentId) {
        return model.getDepartments(Map.of("id", departmentId)).stream().findFirst().orElse(null);
    }

    @Override
    public StoreDTO getStoresById(Object id) {
        return model.getStores(Map.of("id", id)).stream().findFirst().orElse(null);
    }

    @Override
    public void addInventory(InventoryDTO inventoryDTO) {
        model.addInventory(inventoryDTO);
    }

    @Override
    public Collection<InventoryDetailsDTO> getInventoryDetails(Integer inventoryId) {
        return model.getInventoryDetails(Map.of("inventory_id", inventoryId));
    }

    @Override
    public Collection<InventoryDTO> getAllInventories() {
        return model.getAllInventories();
    }

    @Override
    public Collection<InventoryDTO> searchInventories(String column, String searchText) {
        String tableCol = null;
        Object valCol = null;
        switch (column){
            case "ID":
                tableCol = "id";
                valCol = Integer.parseInt(searchText);
                break;
            case "ID Сотрудника":
                tableCol = "employee_id";
                valCol = searchText;
                break;
            case "Номер кабинета":
                tableCol = "store_id";
                StoreDTO storeDTO = model.getStores(Map.of("cabinet_number", Integer.parseInt(searchText))).stream().findFirst().orElse(null);
                if (storeDTO == null){
                    return new ArrayList<>();
                }
                valCol = storeDTO.getId();
                break;
            case "Id склада":
                tableCol = "store_id";
                valCol = Integer.parseInt(searchText);
                break;
            case "Дата инвентаризации":
                tableCol = "inventory_date";
                valCol = LocalDate.parse(searchText);
                break;
        }

        assert tableCol != null;
        return model.getInventories(Map.of(tableCol, valCol));
    }

    @Override
    public Collection<DrugDTO> searchDrugs(String column, String searchText) {
        String tableCol = null;
        Object valCol = null;
        switch (column){
            case "ID":
                tableCol = "id";
                valCol = Integer.parseInt(searchText);
                break;
            case "Название":
                tableCol = "name";
                valCol = searchText;
                break;
            case "Форма выпуска":
                tableCol = "realise_form";
                valCol = searchText;
                break;
            case "Дозировка":
                tableCol = "dose";
                valCol = searchText;
                break;
            case "Поставщик":
                tableCol = "supplier";
                valCol = searchText;
                break;
            case "Срок годности":
                tableCol = "shelf_life";
                valCol = Integer.parseInt(searchText);
                break;
            case "Описание":
                tableCol = "description";
                valCol = searchText;
                break;
        }
        assert tableCol != null;
        return model.getDrugsRecords(Map.of(tableCol, valCol));

    }

    @Override
    public Collection<StoreDTO> searchStores(String column, String searchText) {
        String tableCol = null;
        Object valCol = Integer.parseInt(searchText);
        switch (column){
            case "ID":
                tableCol = "id";
                valCol = Integer.parseInt(searchText);
                break;
            case "Номер кабинета":
                tableCol = "cabinet_number";
                break;
            case "Отделение":
                tableCol = "department_id";
                break;
            case "Администратор":
                tableCol = "administrator_id";
                break;
            case "Вместительность":
                tableCol = "capacity";
                break;
            case "Текущая заполненность":
                tableCol = "current_fill";
                break;
        }
        assert tableCol != null;
        return model.getStores(Map.of(tableCol, valCol));
    }

    @Override
    public String[] getStoresNames() {
        return model.get_detail_stores_raws();
    }

    @Override
    public void addInventoryDetails(Integer inventoryId, Integer storeID, Integer drugId, Integer drugsCount) throws PSQLException {
        model.addInventoryDetails(inventoryId,storeID, drugId, drugsCount);
    }

    @Override
    public void deleteInventoryDetail(Object id) {
        model.deleteInventoryDetail(id);
    }

    @Override
    public void deleteInventory(Integer id) {
        model.deleteInventory(id);
    }



    @Override
    public StoreDTO getStoreById(Object storeId) {
        return model.getStores(Map.of("id", storeId)).stream().findFirst().orElse(null);
    }

    @Override
    public Collection<StoreDrugsDTO> getDrugsByStoreId(Integer storeId) {
        return model.getDrugsStore(Map.of("store_id", storeId));
    }

    @Override
    public Collection<DispensingDTO> getDispensing(Map<String, Object> conditions) {
        return model.getDispensing(conditions);
    }

    @Override
    public Collection<PatientDTO> getParients(Map<String, Object> conditions) {
        return model.getPatients(conditions);
    }

    @Override
    public void signInventory(int inventoryId, int authUserId) {
        model.signInventory(inventoryId, authUserId);
    }

    @Override
    public Collection<DispensingDTO> getAllDispensing() {
        return model.getAllDispensing();
    }

    @Override
    public PatientDTO getPatientById(Integer patientId) {
        return model.getPatients(Map.of("id", patientId)).stream().findFirst().orElse(null);
    }

    @Override
    public PatientDTO getPatientByPolicy(String policyNumber) {
        return model.getPatients(Map.of("policy", policyNumber)).stream().findFirst().orElse(null);
    }

    @Override
    public Collection<DispensingDTO> getDispensingByPatientId(Integer id) {
        return model.getDispensing(Map.of("patient_id", id));
    }

    @Override
    public void issueDrug(int dispensingId, int adminId) {
        model.issueDrug(dispensingId, adminId);
    }
}
