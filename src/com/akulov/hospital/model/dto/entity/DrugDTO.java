package com.akulov.hospital.model.dto.entity;
import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;

import java.time.LocalDate;

@TableName(tableName = "drugs")
public class DrugDTO extends DTOImpl {

    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "name")
    private final String name;

    @TableField(collumnName = "realise_form")
    private final String realiseForm;

    @TableField(collumnName = "dose")
    private final String dose;

    @TableField(collumnName = "supplier")
    private final String supplier;

    @TableField(collumnName = "shelf_life")
    private final Integer shelfLife;

    @TableField(collumnName = "description")
    private final String description;

    public DrugDTO(Integer id, String name, String realiseForm, String dose, String supplier, Integer shelfLife, String description){
        this.id = id;
        this.name = name;
        this.realiseForm = realiseForm;
        this.dose = dose;
        this.supplier = supplier;
        this.shelfLife = shelfLife;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRealiseForm() {
        return realiseForm;
    }

    public String getDose() {
        return dose;
    }

    public String getSupplier() {
        return supplier;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public String getDescription() {
        return description;
    }
}
