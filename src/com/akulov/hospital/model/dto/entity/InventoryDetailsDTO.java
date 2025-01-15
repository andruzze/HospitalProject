package com.akulov.hospital.model.dto.entity;

import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;

@TableName(tableName = "inventory_details")
public class InventoryDetailsDTO extends DTOImpl {
    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "inventory_id")
    private final Integer inventoryId;

    @TableField(collumnName = "drug_id")
    private final Integer drugId;

    @TableField(collumnName = "drugs_count")
    private final Integer drugsCount;

    @TableField(collumnName = "drugs_count_before")
    private final Integer drugsCountBefore;

    public InventoryDetailsDTO(Integer id, Integer inventoryId, Integer drugId, Integer drugsCount, Integer drugsCountBefore) {
        this.id = id;
        this.inventoryId = inventoryId;
        this.drugId = drugId;
        this.drugsCount = drugsCount;
        this.drugsCountBefore = drugsCountBefore;
    }

    public Integer getId() {
        return id;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public Integer getDrugId() {
        return drugId;
    }

    public Integer getDrugsCount() {
        return drugsCount;
    }

    public Integer getDrugsCountBefore() {
        return drugsCountBefore;
    }
}
