package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.InventoryDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDAO extends DataAccessObjectImpl<InventoryDTO> {
    public InventoryDAO(DatabaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public String getTableName() {
        return parser.getTableName(InventoryDTO.class);
    }

    @Override
    public InventoryDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new InventoryDTO(
                rs.getInt("id"),
                rs.getInt("employee_id"),
                rs.getInt("store_id"),
                rs.getDate("inventory_date").toLocalDate(),
                rs.getString("sign")
        );
    }

    public void sign (int inventoryId, int userId){
        try {
            callProcedure("sign_inventory", inventoryId, userId);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
