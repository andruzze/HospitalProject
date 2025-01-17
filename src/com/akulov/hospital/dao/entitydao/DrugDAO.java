package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;

import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.DrugDTO;
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
    public DrugDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new DrugDTO(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("realise_form"),
                rs.getString("dose"),
                rs.getString("supplier"),
                rs.getInt("shelf_life"),
                rs.getString("description")
            );
    }
}
