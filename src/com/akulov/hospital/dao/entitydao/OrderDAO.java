package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.OrderDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO extends DataAccessObjectImpl<OrderDTO> {
    public OrderDAO(DatabaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public String getTableName() {
        return parser.getTableName(OrderDTO.class);
    }

    @Override
    public OrderDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new OrderDTO(
                rs.getInt("id"),
                rs.getString("vendor"),
                rs.getDate("order_date").toLocalDate(),
                rs.getDate("delivery_date").toLocalDate(),
                rs.getString("status")
        );
    }
}
