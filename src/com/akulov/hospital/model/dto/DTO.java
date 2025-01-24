package com.akulov.hospital.model.dto;

import java.util.Collection;
import java.util.Map;

public interface DTO {
    Map<String, Object> getFieldsValeus();
    Collection<Object> getValues();
}
