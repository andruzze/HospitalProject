package com.akulov.hospital.dao;

import com.akulov.hospital.adapters.DatabaseAdapter;

import com.akulov.hospital.model.dto.DrugDTO;
import com.akulov.hospital.utils.TableElemAnnotationParser;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DrugDAO extends DataAccessObjectImpl<DrugDTO> {


    public DrugDAO(DatabaseAdapter adapter) {
        super(adapter);
    }

    @Override
    String getTableName(){
        return TableElemAnnotationParser.getTableName(DrugDTO.class);
    }

    @Override
    DrugDTO mapResultSetToEntity(ResultSet rs)throws SQLException {
        return new DrugDTO(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("realiseForm"),
                rs.getString("dose"),
                rs.getString("supplier"),
                rs.getInt("expirationDate")
            );
    }




//    public DrugDTO get(){
//
//    }



//    @Override
//    public Optional<DrugDTO> get(int id) {
//        final String query = "Select * from drugs where id = ?";
//        try{
//            final ResultSet rows = executeQuery(query, id);
//            if (rows.next()) {
//                final DrugDTO drug = mapResultSetToEntity(rows);
//            return Optional.of(drug);}
//        }catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//        return Optional.empty();
//
//    }
//    @Override
//    public Collection<DrugDTO> getAll() {
//        final String query = "Select * from drugs";
//        List<DrugDTO> tempList = new ArrayList<>();
//        try{
//            final ResultSet rows = executeQuery(query);
//            while(rows.next()){
//                final DrugDTO drug = mapResultSetToEntity(rows);
//                tempList.add(drug);
//            }
//        }catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//        return tempList;
//    }
//
//    @Override
//    public void insert(DrugDTO drugDTO) {
//        final String query = "Insert into drugs(name,realise_form,dose,supplier,expiration_date) values(?,?,?,?,?)";
//        try {
//            int count = executeUpdate(
//                    query,
//                    drugDTO.getName(),
//                    drugDTO.getRealiseForm(),
//                    drugDTO.getDose(),
//                    drugDTO.getSupplier(),
//                    drugDTO.getExpiration_date()
//            );
//        }catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//
//    }
//
//    @Override
//    public void update(DrugDTO drugDTO, String... args) {
//        final StringBuilder query = new StringBuilder().append("UPDATE drugs SET");
//
//    }
//
//    @Override
//    public void delete(DrugDTO drugDTO) {
//
//    }
}
