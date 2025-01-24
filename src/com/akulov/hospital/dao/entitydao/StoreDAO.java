package com.akulov.hospital.dao.entitydao;

import com.akulov.hospital.adapters.DatabaseAdapter;
import com.akulov.hospital.dao.DataAccessObjectImpl;
import com.akulov.hospital.model.dto.entity.StoreDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


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
                rs.getInt("current_fill"),
                rs.getInt("cabinet_number")
        );
    }

    public String[] getDetailsRaws(){
        ResultSet rs = callFunc("get_detail_stores_raws");
        Collection<String> res = new ArrayList<String>();
        try{
            while( rs.next()){
                res.add(rs.getInt("store_id") + " " + rs.getString("department_name") + " " + " Каб." + rs.getInt("cabinet_number") );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res.toArray(new String[0]);
    }
}
