package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.PatientDTO;
import com.akulov.hospital.model.dto.types.FullName;
import com.akulov.hospital.model.dto.types.Passport;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientDAO extends DataAccessObjectImpl<PatientDTO> {
    public PatientDAO(DatabaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public String getTableName() {
        return parser.getTableName(PatientDTO.class);
    }

    @Override
    public PatientDTO mapResultSetToEntity(ResultSet rs) throws SQLException {
        String fio = rs.getString("fio");
        String[] Name = fio.substring(1, fio.length()-1).split(",");
        String passport = rs.getString("passport");
        String[] passportArr = passport.substring(1, passport.length()-1).split(",");
        return new PatientDTO(
                rs.getInt("id"),
                new FullName(Name[0], Name[1], Name[2]),
                rs.getInt("age"),
                new Passport(Integer.parseInt(passportArr[0]), Integer.parseInt(passportArr[1])),
                rs.getString("telephone"),
                rs.getString("policy")
        );
    }
}
