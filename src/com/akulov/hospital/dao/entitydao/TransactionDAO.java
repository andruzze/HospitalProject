package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.TransactionDTO;
import org.postgresql.util.PSQLException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDAO extends DataAccessObjectImpl<TransactionDTO> {

    public TransactionDAO(DatabaseAdapter adapter){
        super(adapter);
    }

    @Override
    public String getTableName() {
        return parser.getTableName(TransactionDTO.class);
    }

    @Override
    public TransactionDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new TransactionDTO(
                rs.getInt("id"),
                rs.getString("transaction_type"),
                rs.getInt("drug_id"),
                rs.getInt("drugs_count"),
                rs.getInt("employee_id"),
                rs.getDate("operation_date").toLocalDate(),
                rs.getInt("store_id")
        );

    }

    public void addTransactionFunc(Object... args) throws PSQLException {
        callProcedure("create_transaction", args);
    }
}
