package com.akulov.hospital.model.dto;

import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.types.FullName;
import com.akulov.hospital.model.dto.types.Passport;

@TableName(tableName = "employees")
public class EmployeeDTO extends DTOImpl {
    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "fio")
    private final FullName fullName;

    @TableField(collumnName = "age")
    private final Integer age;

    @TableField(collumnName = "passport")
    private final Passport passport;

    @TableField(collumnName = "telephone")
    private final String telephone;

    @TableField(collumnName =  "speciality")
    private final String speciality;

    @TableField(collumnName = "department_id")
    private final Integer departmentId;

    public EmployeeDTO(Integer id, FullName fullName, Integer age, Passport passport, String telephone, String speciality, Integer departmentId) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.passport = passport;
        this.telephone = telephone;
        this.speciality = speciality;
        this.departmentId = departmentId;
    }

    public Integer getId() {
        return id;
    }

    public FullName getFullName() {
        return fullName;
    }

    public Integer getAge() {
        return age;
    }

    public Passport getPassport() {
        return passport;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }
}
