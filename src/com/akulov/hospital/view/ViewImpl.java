package com.akulov.hospital.view;

import com.akulov.hospital.controller.ControllerImpl;
import com.akulov.hospital.model.dto.entity.DrugDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class ViewImpl {
    private final ControllerImpl controller;
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewImpl(ControllerImpl controller) {
        this.controller = controller;
        initializeUI();
    }

    /**
     * Инициализация UI.
     */
    private void initializeUI() {
        frame = new JFrame("Список препаратов");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Создаем таблицу
        String[] columnNames = {"ID", "Name", "Realise Form", "Dose", "Supplier", "Shelf Life", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Загружаем данные в таблицу
        loadTableData();

        // Панель с кнопками
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Добавить препарат");
        JButton updateButton = new JButton("Обновить препарат");
        JButton deleteButton = new JButton("Удалить препарат");

        // Добавление слушателей
        addButton.addActionListener(e -> showAddDrugDialog());
        updateButton.addActionListener(e -> showUpdateDrugDialog());
        deleteButton.addActionListener(e -> deleteSelectedDrug());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Добавляем компоненты в окно
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * Загружает данные в таблицу.
     */
    private void loadTableData() {
        Collection<DrugDTO> drugs = controller.getAllDrugs();
        tableModel.setRowCount(0); // Очистка таблицы
        for (DrugDTO drug : drugs) {
            tableModel.addRow(new Object[]{
                    drug.getId(),
                    drug.getName(),
                    drug.getRealiseForm(),
                    drug.getDose(),
                    drug.getSupplier(),
                    drug.getShelfLife(),
                    drug.getDescription()
            });
        }
    }

    /**
     * Показывает диалоговое окно для добавления нового препарата.
     */
    private void showAddDrugDialog() {
        JTextField nameField = new JTextField();
        JTextField realiseFormField = new JTextField();
        JTextField doseField = new JTextField();
        JTextField supplierField = new JTextField();
        JTextField shelfLifeField = new JTextField();
        JTextField descriptionField = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Realise Form:", realiseFormField,
                "Dose:", doseField,
                "Supplier:", supplierField,
                "Shelf Life:", shelfLifeField,
                "Description:", descriptionField
        };

        int result = JOptionPane.showConfirmDialog(frame, fields, "Добавить препарат", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            DrugDTO newDrug = new DrugDTO(
                    null,
                    nameField.getText(),
                    realiseFormField.getText(),
                    doseField.getText(),
                    supplierField.getText(),
                    Integer.parseInt(shelfLifeField.getText()),
                    descriptionField.getText()
            );
            controller.addDrug(newDrug);
            loadTableData();
        }
    }

    /**
     * Показывает диалоговое окно для обновления препарата.
     */
    private void showUpdateDrugDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Выберите препарат для обновления.");
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        JTextField nameField = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
        JTextField realiseFormField = new JTextField((String) tableModel.getValueAt(selectedRow, 2));
        JTextField doseField = new JTextField((String) tableModel.getValueAt(selectedRow, 3));
        JTextField supplierField = new JTextField((String) tableModel.getValueAt(selectedRow, 4));
        JTextField shelfLifeField = new JTextField(tableModel.getValueAt(selectedRow, 5).toString());
        JTextField descriptionField = new JTextField((String) tableModel.getValueAt(selectedRow, 6));

        Object[] fields = {
                "Name:", nameField,
                "Realise Form:", realiseFormField,
                "Dose:", doseField,
                "Supplier:", supplierField,
                "Shelf Life:", shelfLifeField,
                "Description:", descriptionField
        };

        int result = JOptionPane.showConfirmDialog(frame, fields, "Обновить препарат", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            DrugDTO updatedDrug = new DrugDTO(
                    id,
                    nameField.getText(),
                    realiseFormField.getText(),
                    doseField.getText(),
                    supplierField.getText(),
                    Integer.parseInt(shelfLifeField.getText()),
                    descriptionField.getText()
            );
            controller.updateDrug(updatedDrug, id);
            loadTableData();
        }
    }

    /**
     * Удаляет выбранный препарат.
     */
    private void deleteSelectedDrug() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Выберите препарат для удаления.");
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        controller.deleteDrug(id);
        loadTableData();
    }
}
