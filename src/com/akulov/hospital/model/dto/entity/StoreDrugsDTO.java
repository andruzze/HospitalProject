package com.akulov.hospital.model.dto.entity;

import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;

import java.time.LocalDate;

@TableName(tableName = "store_drugs")
public class StoreDrugsDTO extends DTOImpl {
    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "drug_id")
    private final Integer drugId;

    @TableField(collumnName = "drugs_count")
    private final Integer drugsCount;

    @TableField(collumnName = "store_id")
    private final Integer storeId;

    @TableField(collumnName = "last_refill_date")
    private final LocalDate lastRefillDate;

    @TableField(collumnName = "last_write_off")
    private final LocalDate lastWriteOff;

    public StoreDrugsDTO(Integer id, Integer drugId, Integer drugsCount, Integer storeId, LocalDate lastRefillDate, LocalDate lastWriteOff) {
        this.id = id;
        this.drugId = drugId;
        this.drugsCount = drugsCount;
        this.storeId = storeId;
        this.lastRefillDate = lastRefillDate;
        this.lastWriteOff = lastWriteOff;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDrugId() {
        return drugId;
    }

    public Integer getDrugsCount() {
        return drugsCount;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public LocalDate getLastRefillDate() {
        return lastRefillDate;
    }

    public LocalDate getLastWriteOff() {
        return lastWriteOff;
    }
}
