package com.akulov.hospital.model.dto.types;

public class Passport {
    private final Integer serial;
    private final Integer number;

    public Passport(Integer serial, Integer number) {
        this.serial = serial;
        this.number = number;
    }

    public Integer getSerial() {
        return serial;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Passport{" +
                serial +
                "," +
                number +
                "}";
    }
}
