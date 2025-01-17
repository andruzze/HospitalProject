package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.StoreDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreDAO extends DataAccessObjectImpl<StoreDTO> {
    public StoreDAO(DatabaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public String getTableName() {
        return parser.getTableName(StoreDTO.class);
    }

    @Override
    public StoreDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new StoreDTO(
                rs.getInt("id"),
                rs.getInt("department_id"),
                rs.getInt("administrator_id"),
                rs.getInt("capacity"),
                rs.getInt("current_fill")
        );
    }
}
