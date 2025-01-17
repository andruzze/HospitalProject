package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.InventoryDetailsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDetailsDAO extends DataAccessObjectImpl<InventoryDetailsDTO> {
    public InventoryDetailsDAO(DatabaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public String getTableName() {
        return parser.getTableName(InventoryDetailsDTO.class);
    }

    @Override
    public InventoryDetailsDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new InventoryDetailsDTO(
                rs.getInt("id"),
                rs.getInt("inventory_id"),
                rs.getInt("drug_id"),
                rs.getInt("drugs_count"),
                rs.getInt("drugs_count_before")
        );
    }
}
