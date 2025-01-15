package com.akulov.hospital.model.dto.entity;

import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;

@TableName(tableName = "dispensing")
public class DispensingDTO extends DTOImpl {
    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "patient_id")
    private final Integer patientId;

    @TableField(collumnName = "transaction_id")
    private final Integer transactionId;

    public DispensingDTO(Integer id, Integer patientId, Integer transactionId) {
        this.id = id;
        this.patientId = patientId;
        this.transactionId = transactionId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }
}
