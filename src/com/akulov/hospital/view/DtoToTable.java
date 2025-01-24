package com.akulov.hospital.view;

import com.akulov.hospital.model.dto.DTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Collection;

public class DtoToTable<T extends DTO> {
    private final Collection<T> dtoCollection;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public DtoToTable(Object[] columnName, Collection<T> dtoCollection) {
        this.dtoCollection = dtoCollection;
        this.tableModel = new DefaultTableModel(columnName, 0);
        this.table = new JTable(tableModel);
        loadData();
    }

    public void updateData(){
        loadData();
    }

    private void loadData(){
        tableModel.setRowCount(0); // Очистка таблицы
        for (T dto : dtoCollection) {
            tableModel.addRow(dto.getValues().toArray());
        }
    }

    public DefaultTableModel getTableModel(){
        return tableModel;
    }
    public JTable getTable (){
        return table;
    }
}
