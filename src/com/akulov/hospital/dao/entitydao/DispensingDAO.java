package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.DispensingDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DispensingDAO extends DataAccessObjectImpl<DispensingDTO> {
    public DispensingDAO(DatabaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public String getTableName() {
        return parser.getTableName(DispensingDTO.class);
    }

    @Override
    public DispensingDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new DispensingDTO(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getInt("transaction_id")
        );
    }
}
