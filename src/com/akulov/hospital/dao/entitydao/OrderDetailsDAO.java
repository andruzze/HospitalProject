package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.OrderDetailsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailsDAO extends DataAccessObjectImpl<OrderDetailsDTO> {
    public OrderDetailsDAO(DatabaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public String getTableName() {
        return parser.getTableName(OrderDetailsDTO.class);
    }

    @Override
    public OrderDetailsDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new OrderDetailsDTO(
                rs.getInt("id"),
                rs.getInt("order_id"),
                rs.getInt("drugs_count"),
                rs.getDate("batch_expiration_date").toLocalDate()
        );
    }
}
