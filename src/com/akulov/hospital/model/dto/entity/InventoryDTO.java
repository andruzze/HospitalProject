package com.akulov.hospital.model.dto.entity;

import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;

import java.time.LocalDate;

@TableName(tableName = "inventories")
public class InventoryDTO extends DTOImpl {
    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "employee_id")
    private final Integer employeeId;

    @TableField(collumnName = "store_id")
    private final Integer storeId;

    @TableField(collumnName = "inventory_date")
    private final LocalDate inventoryDate;

    @TableField(collumnName = "sign")
    private final String sign;

    public InventoryDTO(Integer id, Integer employeeId, Integer storeId, LocalDate inventoryDate, String sign) {
        this.id = id;
        this.employeeId = employeeId;
        this.storeId = storeId;
        this.inventoryDate = inventoryDate;
        this.sign = sign;

    }

    public Integer getId() {
        return id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public LocalDate getInventoryDate() {
        return inventoryDate;
    }

    public Object getSign() {return this.sign;}
}
