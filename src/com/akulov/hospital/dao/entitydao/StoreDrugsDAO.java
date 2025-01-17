package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.StoreDrugsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreDrugsDAO extends DataAccessObjectImpl<StoreDrugsDTO> {
    public StoreDrugsDAO(DatabaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public String getTableName() {
        return parser.getTableName(StoreDrugsDTO.class);
    }

    @Override
    public StoreDrugsDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new StoreDrugsDTO(
                rs.getInt("id"),
                rs.getInt("drug_id"),
                rs.getInt("drugs_count"),
                rs.getInt("store_id"),
                rs.getDate("last_refill_date").toLocalDate(),
                rs.getDate("last_write_off").toLocalDate()
        );
    }
}
