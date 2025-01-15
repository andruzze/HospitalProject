package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.DepartmentDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentDAO extends DataAccessObjectImpl<DepartmentDTO> {

    public DepartmentDAO(DatabaseAdapter adapter){
        super(adapter);
    }

    @Override
    public String getTableName() {
        return parser.getTableName(DepartmentDTO.class);
    }

    @Override
    public DepartmentDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new DepartmentDTO(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("telephone"),
                rs.getInt("employees_count")
        );
    }

}
