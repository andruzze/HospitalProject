package com.akulov.hospital.view;

import com.akulov.hospital.controller.Controller;
import com.akulov.hospital.model.dto.entity.DispensingDTO;
import com.akulov.hospital.model.dto.entity.PatientDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class DispensingPage implements Page {
    private final Controller controller;
    private final JPanel panel;
    private final JTable dispensingTable;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;

    public DispensingPage(Controller controller) {
        this.controller = controller;
        this.panel = new JPanel(new BorderLayout());

        // Модель данных для таблицы
        String[] columnNames = {"ID", "ID Пациента", "Пациент", "Полис" ,"ID Транзакции", "Статус"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dispensingTable = new JTable(tableModel);

        // Настройка цветов для строки в зависимости от статуса
        dispensingTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String status = (String) table.getValueAt(row, 5); // Столбец "Статус"
                if ("Не выдан".equals(status)) {
                    c.setBackground(Color.RED);
                } else if ("Выдан".equals(status)) {
                    c.setBackground(Color.GREEN);
                } else {
                    c.setBackground(Color.WHITE);
                }

                if (isSelected) {
                    c.setBackground(c.getBackground().darker());
                }

                return c;
            }
        });

        // Поисковая панель
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Поиск");
        JButton updateBtn = new JButton("Обновить");

        searchPanel.add(new JLabel("Поиск по полису пациента:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(updateBtn);

        // Панель кнопок управления
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton issueButton = new JButton("Выдать");
        buttonPanel.add(issueButton);

        // Обработчики событий
        searchButton.addActionListener(e -> searchDispensing());
        issueButton.addActionListener(e -> issueSelected());
        updateBtn.addActionListener(e-> updateData());

        // Компоновка панели
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(dispensingTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Загрузка данных
        updateData();
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void updateData() {
        tableModel.setRowCount(0);
        Collection<DispensingDTO> dispensingList = controller.getAllDispensing();
        for (DispensingDTO dispensing : dispensingList) {
            PatientDTO patient = controller.getPatientById(dispensing.getPatientId());
            tableModel.addRow(new Object[]{
                    dispensing.getId(),
                    dispensing.getPatientId(),
                    patient.getFullName(),
                    patient.getPolicy(),
                    dispensing.getTransactionId(),
                    dispensing.getStatus()
            });
        }
    }

    private void searchDispensing() {
        String policyNumber = searchField.getText().trim();
        if (policyNumber.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Введите номер полиса для поиска.");
            return;
        }

        tableModel.setRowCount(0);
        PatientDTO patient = controller.getPatientByPolicy(policyNumber);
        if (patient == null) {
            JOptionPane.showMessageDialog(panel, "Пациент с таким номером полиса не найден.");
            return;
        }

        Collection<DispensingDTO> dispensingList = controller.getDispensingByPatientId(patient.getId());
        for (DispensingDTO dispensing : dispensingList) {
            tableModel.addRow(new Object[]{
                    dispensing.getId(),
                    dispensing.getPatientId(),
                    patient.getFullName(),
                    dispensing.getTransactionId(),
                    dispensing.getStatus()
            });
        }
    }

    private void issueSelected() {
        int selectedRow = dispensingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Выберите запись для выдачи.");
            return;
        }

        int dispensingId = (Integer) dispensingTable.getValueAt(selectedRow, 0);
        String status = (String) dispensingTable.getValueAt(selectedRow, 5);

        if ("Выдан".equals(status)) {
            JOptionPane.showMessageDialog(panel, "Этот препарат уже выдан.");
            return;
        }

        controller.issueDrug(dispensingId, controller.getAuthUser());
        updateData();
        JOptionPane.showMessageDialog(panel, "Статус выдачи изменен на 'Выдан'.");
    }
}
