package com.akulov.hospital.model.dto.entity;


import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;

import java.time.LocalDate;

@TableName(tableName = "order_details")
public class OrderDetailsDTO extends DTOImpl {
    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "order_id")
    private final Integer orederId;

    @TableField(collumnName = "drugs_count")
    private final Integer drugsCount;

    @TableField(collumnName = "batch_expiration_date")
    private final LocalDate batchExpirationDate;

    public OrderDetailsDTO(Integer id, Integer orederId, Integer drugsCount, LocalDate batchExpirationDate) {
        this.id = id;
        this.orederId = orederId;
        this.drugsCount = drugsCount;
        this.batchExpirationDate = batchExpirationDate;
    }

    public Integer getId() {
        return id;
    }

    public Integer getOrederId() {
        return orederId;
    }

    public Integer getDrugsCount() {
        return drugsCount;
    }

    public LocalDate getBatchExpirationDate() {
        return batchExpirationDate;
    }
}
