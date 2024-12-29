package com.akulov.hospital.model.dto;
import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;

import java.util.HashMap;
import java.util.Map;

@TableName(tableName = "drugs")
public class DrugDTO extends DTO{

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

    @TableField(collumnName = "expiration_date")
    private final int expirationDate;

    public DrugDTO(Integer id, String name, String realiseForm, String dose, String supplier, int expiration_date){
        this.id = id;
        this.name = name;
        this.realiseForm = realiseForm;
        this.dose = dose;
        this.supplier = supplier;
        this.expirationDate = expiration_date;
    }


    public int getId() {
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

    public int getExpirationDate() {
        return expirationDate;
    }


}
