package com.akulov.hospital.model.dto.entity;

import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;

import java.time.LocalDate;

@TableName(tableName = "transactions")
public class TransactionDTO extends DTOImpl {
    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "transaction_type")
    private final String transactionType;

    @TableField(collumnName = "drug_id")
    private final Integer drugId;

    @TableField(collumnName = "drugs_count")
    private final Integer drugsCount;

    @TableField(collumnName = "employee_id")
    private final Integer employeeId;

    @TableField(collumnName = "operation_date")
    private final LocalDate operationDate;

    @TableField(collumnName = "store_id")
    private final Integer storeId;

    public TransactionDTO(Integer id, String transactionType, Integer drugId, Integer drugsCount, Integer employeeId, LocalDate operationDate, Integer storeId) {
        this.id = id;
        this.transactionType = transactionType;
        this.drugId = drugId;
        this.drugsCount = drugsCount;
        this.employeeId = employeeId;
        this.operationDate = operationDate;
        this.storeId = storeId;
    }

    public Integer getId() {
        return id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Integer getDrugId() {
        return drugId;
    }

    public Integer getDrugsCount() {
        return drugsCount;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public LocalDate getOperationDate() {
        return operationDate;
    }

    public Integer getStoreId() {
        return storeId;
    }
}
