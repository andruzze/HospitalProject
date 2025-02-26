package com.akulov.hospital.model.dto.entity;

import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;

@TableName(tableName = "stores")
public class StoreDTO extends DTOImpl {

    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "department_id")
    private final Integer departmentId;

    @TableField(collumnName = "administrator_id")
    private final Integer adminostratorId;

    @TableField(collumnName = "capacity")
    private final Integer capacity;

    @TableField(collumnName = "current_fill")
    private final Integer currentFill;

    @TableField(collumnName = "cabinet_number")
    private final int cabinetNumber;

    public StoreDTO(Integer id, Integer departmentId, Integer adminostratorId, Integer capacity, Integer currentFill, int cabinetNumber) {
        this.id = id;
        this.departmentId = departmentId;
        this.adminostratorId = adminostratorId;
        this.capacity = capacity;
        this.currentFill = currentFill;
        this.cabinetNumber = cabinetNumber;
    }

    public int getCabinetNumber() {return cabinetNumber;}

    public Integer getCurrentFill() {
        return currentFill;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getAdminostratorId() {
        return adminostratorId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public Integer getId() {
        return id;
    }
}
