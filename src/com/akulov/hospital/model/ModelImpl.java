package com.akulov.hospital.model;

import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.DTO;
import com.akulov.hospital.model.dto.entity.*;
import com.akulov.hospital.util.DaoFactory;
import com.akulov.hospital.util.ParserDTO;
import org.postgresql.util.PSQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class ModelImpl implements Model {
    private final DaoFactory daoFactory;
    private Integer authUserId = null;

    public ModelImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public Collection<DispensingDTO> getAllDispensing() {
        return getAllRecords(daoFactory.getDispensingDAO());
    }

    @Override
    public void issueDrug(int dispensingId, int adminId) {
        daoFactory.getDispensingDAO().issueDrug(dispensingId, adminId);
    }

    @Override
    public void signInventory(int inventoryId, int authUserId) {
        daoFactory.getInventoryDAO().sign(inventoryId, authUserId);
    }

    @Override
    public Collection<DispensingDTO> getDispensing(Map<String, Object> conditions) {
        return getRecord(daoFactory.getDispensingDAO(), conditions);
    }

    @Override
    public Collection<PatientDTO> getPatients(Map<String, Object> conditions) {
        return getRecord(daoFactory.getPatientDAO(), conditions);
    }

    @Override
    public Collection<StoreDrugsDTO> getDrugsStore(Map<String, Object> conditions) {
        return getRecord(daoFactory.getStoreDrugDAO(), conditions);
    }

    @Override
    public Collection<InventoryDTO> getInventories(Map<String, Object> conditions) {
        return getRecord(daoFactory.getInventoryDAO(), conditions);
    }

    @Override
    public void deleteInventory(Integer id) {
        daoFactory.getInventoryDAO().delete(id);
    }

    @Override
    public void deleteTransaction(Integer transactionId) {
        daoFactory.getTransactionDAO().delete(transactionId);
    }

    @Override
    public void deleteInventoryDetail(Object id) {
        daoFactory.getInventoryDetailsDAO().delete((Integer)id);
    }

    @Override
    public void addInventoryDetails(Integer inventoryId, Integer storeId, Integer drugId, Integer drugsCount) throws PSQLException {
        daoFactory.getInventoryDetailsDAO().addDetails(inventoryId, storeId, drugId, drugsCount);
    }

    @Override
    public String[] get_detail_stores_raws() {
        return daoFactory.getStoreDAO().getDetailsRaws();
    }

    @Override
    public Collection<InventoryDTO> getAllInventories() {
        return getAllRecords(daoFactory.getInventoryDAO());
    }

    @Override
    public Collection<InventoryDetailsDTO> getInventoryDetails(Map<String, Object> conditions) {
        return getRecord(daoFactory.getInventoryDetailsDAO(), conditions);
    }

    @Override
    public void addInventory(InventoryDTO inventoryDTO) {
        addRecord(daoFactory.getInventoryDAO(), inventoryDTO);
    }

    @Override
    public Collection<StoreDTO> getStores(Map<String, Object> conditions) {
        return getRecord(daoFactory.getStoreDAO(), conditions);
    }

    @Override
    public Collection<DepartmentDTO> getDepartments(Map<String, Object> conditions) {
        return getRecord(daoFactory.getDepartmentDAO(), conditions);
    }

    @Override
    public Collection<EmployeeDTO> getEmployees(Map<String, Object> conditions) {
        return getRecord(daoFactory.getEmployeeDAO(), conditions);
    }

    @Override
    public Collection<TransactionDTO> getTransactions(Map<String, Object> conditions) {
        return getRecord(daoFactory.getTransactionDAO(), conditions);
    }

    @Override
    public Integer getPatientId(Map<String, Object> conditions) {
       PatientDTO patientDTO = getRecord(daoFactory.getPatientDAO(), conditions).stream().findFirst().orElse(null);
       return patientDTO.getId();
    }

    @Override
    public Collection<TransactionDTO> getAllTransactions() {
        return getAllRecords(daoFactory.getTransactionDAO());
    }

    @Override
    public void addTransaction(TransactionDTO transaction, Integer patientId) throws PSQLException{
        daoFactory.getTransactionDAO().addTransactionFunc(
                transaction.getTransactionType(),
                transaction.getDrugId(),
                transaction.getDrugsCount(),
                transaction.getEmployeeId(),
                transaction.getOperationDate(),
                transaction.getStoreId(),
                patientId
        );
    }

    @Override
    public void addTransaction(TransactionDTO transaction) throws PSQLException {
        daoFactory.getTransactionDAO().addTransactionFunc(
                transaction.getTransactionType(),
                transaction.getDrugId(),
                transaction.getDrugsCount(),
                transaction.getEmployeeId(),
                transaction.getOperationDate(),
                transaction.getStoreId()
        );
    }

    @Override
    public Integer getEmployeeId(Map<String, Object> conditions) {
        Integer resId = -1;
        EmployeeDTO record = getRecord(daoFactory.getEmployeeDAO(), conditions).stream().findFirst().orElse(null);
        if(record != null){
            resId = record.getId();
        }
        return resId;
    }

    public Integer getAuthUserId(){
        return authUserId;
    }



    @Override
    public boolean authEmployee(String login, String pass) {
        boolean res = false;
        Integer employeeId = getEmployeeId(Map.of("telephone", login));
        if(employeeId != -1) {
            res = true;
            this.authUserId = employeeId;
        }
        return res;
    }

    public Collection<Object> getDrugStoregeById(int id){
        return daoFactory.getDrugDAO().getStoregeDrugsById(id);
    }

    @Override
    public Collection<DrugDTO> getDrugsRecords(Map<String, Object> conditions){
        return getRecord(daoFactory.getDrugDAO(), conditions);
    }


    @Override
    public Collection<DrugDTO> getAllDrugRecords(){
        return getAllRecords(daoFactory.getDrugDAO());
    }

    @Override
    public Collection<StoreDTO> getAllStores() {
        return getAllRecords(daoFactory.getStoreDAO());
    }

    @Override
    public void addDrug(DrugDTO drug){
        addRecord(daoFactory.getDrugDAO(), drug);
    }

    @Override
    public void addStore(StoreDTO storeDTO) {
        addRecord(daoFactory.getStoreDAO(), storeDTO);
    }

    @Override
    public void updateDrug(DrugDTO drugDTO, Map<String, Object> conditions){
        updateRecord(daoFactory.getDrugDAO(), drugDTO, conditions);
    }

    @Override
    public void updateStore(StoreDTO updatedStore, Map<String, Object> conditions) {
        updateRecord(daoFactory.getStoreDAO(), updatedStore, conditions);
    }

    @Override
    public void deleteDrug(Integer id){
        deleteRecord(daoFactory.getDrugDAO(), id);
    }

    @Override
    public void deleteStore(Integer id) {
        deleteRecord(daoFactory.getStoreDAO(), id);
    }

    private <T extends DTO> Collection<T> getAllRecords(DataAccessObjectImpl<T> dao) {
        return dao.get("*", Map.of());
    }

    private<T extends DTO> Collection<T> getRecord(DataAccessObjectImpl<T> dao, Map<String, Object> conditions){
        return dao.get("*", conditions);
    }

    private <T extends DTO> void addRecord(DataAccessObjectImpl<T> dao, T dto) {
        dao.insert(dto);
    }

    private <T extends DTO> void updateRecord(DataAccessObjectImpl<T> dao, T dto, Map<String, Object> conditions) {
        dao.update(dto, conditions);
    }

    private <T extends DTO> void deleteRecord(DataAccessObjectImpl<T> dao, Integer id) {
        dao.delete(id);
    }

    private <T extends DTO> Object[][] recordsToArray(Collection<T> records) {
        ParserDTO parserDTO = new ParserDTO();
        Object record = records.toArray()[0];
        int fieldCOunt = parserDTO.parseFields(record.getClass()).size();
        return new Object[records.size()][fieldCOunt];
    }
}
