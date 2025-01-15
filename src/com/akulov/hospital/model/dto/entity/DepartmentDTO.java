package com.akulov.hospital.model.dto.entity;

import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;

@TableName(tableName = "departments")
public class DepartmentDTO extends DTOImpl {

    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "name")
    private final String name;

    @TableField(collumnName = "telephone")
    private final String telephone;

    @TableField(collumnName = "employees_count")
    private final Integer employeesCount;

    public DepartmentDTO(Integer id, String name, String telephone, Integer employeesCount) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.employeesCount = employeesCount;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTelephone() {
        return telephone;
    }

    public Integer getEmployeesCount() {
        return employeesCount;
    }
}
