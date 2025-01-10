package com.akulov.hospital.dao;

import com.akulov.hospital.adapters.DatabaseAdapter;

import com.akulov.hospital.model.dto.DrugDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DrugDAO extends DataAccessObjectImpl<DrugDTO> {

    public DrugDAO(DatabaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public String getTableName(){
        return parser.getTableName(DrugDTO.class);
    }

    @Override
    DrugDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new DrugDTO(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("realise_form"),
                rs.getString("dose"),
                rs.getString("supplier"),
                rs.getInt("expiration_date")

            );
    }
}
