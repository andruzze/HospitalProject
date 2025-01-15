package com.akulov.hospital.model.dto.types;

public class FullName {
    private final String firstName;
    private final String middleName;
    private final String lastName;

    public FullName(String lastName, String firstName, String middleName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return String.format("('%s','%s','%s')",
                lastName.replace("'", "''"),
                firstName.replace("'", "''"),
                middleName.replace("'", "''"));
    }
    public String toPostgresString() {
        return String.format("('%s','%s','%s')",
                lastName.replace("'", "''"),
                firstName.replace("'", "''"),
                middleName.replace("'", "''"));
    }


}
