package com.akulov.hospital.model.dto;

public class DrugDTO {
    private final Integer id;
    private final String name;
    private final String realiseForm;
    private final String dose;
    private final String supplier;
    private final int expiration_date;

    public DrugDTO(Integer id, String name, String realiseForm, String dose, String supplier, int expiration_date){
        this.id = id;
        this.name = name;
        this.realiseForm = realiseForm;
        this.dose = dose;
        this.supplier = supplier;
        this.expiration_date = expiration_date;
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

    public int getExpiration_date() {
        return expiration_date;
    }
}
