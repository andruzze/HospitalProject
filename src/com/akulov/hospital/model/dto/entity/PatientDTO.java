package com.akulov.hospital.model.dto.entity;

import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;
import com.akulov.hospital.model.dto.types.FullName;
import com.akulov.hospital.model.dto.types.Passport;

@TableName(tableName = "patients")
public class PatientDTO extends DTOImpl {
    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "full_name")
    private final FullName fullName;

    @TableField(collumnName = "age")
    private final Integer age;

    @TableField(collumnName = "passport")
    private final Passport passport;

    @TableField(collumnName = "telephone")
    private final String telephone;

    public PatientDTO(Integer id, FullName fullName, Integer age, Passport passport, String telephone) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.passport = passport;
        this.telephone = telephone;
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
}
