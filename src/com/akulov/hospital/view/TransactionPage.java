package com.akulov.hospital.view;

import com.akulov.hospital.controller.Controller;
import com.akulov.hospital.model.dto.entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;


public class TransactionPage implements Page {
    private final Controller controller;
    private final JPanel panel;
    private final JTable transactionsTable;
    private final DefaultTableModel tableModel;
    private final JPanel detailsPanel;
    private final JLabel idLabel;
    private final JLabel typeLabel;
    private final JLabel drugInfoLabel;
    private final JLabel countLabel;
    private final JLabel employeeInfoLabel;
    private final JLabel dateLabel;
    private final JLabel storeInfoLabel;

    public TransactionPage(Controller controller) {
        this.controller = controller;
        panel = new JPanel(new BorderLayout());

        // Создаем таблицу для отображения транзакций
        String[] columnNames = {"ID", "Тип", "ID Препарата", "Кол-во препарата", "ID Сотрудника", "Дата операции", "ID Склада"};
        tableModel = new DefaultTableModel(columnNames, 0);
        transactionsTable = new JTable(tableModel);

        // Добавляем таблицу в панель с прокруткой
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Панель управления (содержит поиск, кнопки изменения и удаления)
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JComboBox<String> searchColumnComboBox = new JComboBox<>(columnNames);
        controlPanel.add(new JLabel("Поиск:"));
        controlPanel.add(searchColumnComboBox);

        JTextField searchField = new JTextField(15);
        controlPanel.add(searchField);

        JButton searchButton = new JButton("Найти");
        controlPanel.add(searchButton);


        JButton deleteButton = new JButton("Удалить");
        controlPanel.add(deleteButton);

        panel.add(controlPanel, BorderLayout.NORTH);

        JButton refreshButton = new JButton("Обновить");
        refreshButton.addActionListener(e -> updateData());
        panel.add(refreshButton, BorderLayout.SOUTH);

        // Создаем панель для отображения подробной информации
        detailsPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Подробности транзакции"));

        idLabel = createDetailRow(detailsPanel, "ID транзакции:");
        typeLabel = createDetailRow(detailsPanel, "Тип:");
        drugInfoLabel = createDetailRow(detailsPanel, "Информация о препарате:");
        countLabel = createDetailRow(detailsPanel, "Кол-во препарата:");
        employeeInfoLabel = createDetailRow(detailsPanel, "Сотрудник совершивший транзакциию:");
        dateLabel = createDetailRow(detailsPanel, "Дата операции:");
        storeInfoLabel = createDetailRow(detailsPanel, "Информация о складе:");

        panel.add(detailsPanel, BorderLayout.SOUTH);

        // Обработчик события выбора строки в таблице
        transactionsTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = transactionsTable.getSelectedRow();
            if (selectedRow != -1) {
                updateDetailsPanel(selectedRow);
            }
        });

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            String selectedColumn = (String) searchColumnComboBox.getSelectedItem();
            performSearch(selectedColumn, searchText);
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = transactionsTable.getSelectedRow();
            Object status = transactionsTable.getValueAt(selectedRow, 1);
            Integer transactionId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if(status.equals("Выдача")){
                DispensingDTO dispensingDTO = controller.getDispensing(Map.of("transaction_id", transactionId)).stream().findFirst().get();
                if (dispensingDTO.getStatus().equals( "Выдан")) {
                    JOptionPane.showMessageDialog(panel, "Нельзя удалить транзакцию выдачи препарата, который уже выдали", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (selectedRow != -1) {

                int confirmation = JOptionPane.showConfirmDialog(
                        panel,
                        "Вы уверены, что хотите удалить транзакцию с ID " + transactionId + "?",
                        "Подтверждение",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmation == JOptionPane.YES_OPTION) {
                    controller.deleteTransaction(transactionId);
                    updateData();
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Выберите транзакцию для удаления.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });

        

        this.updateData();
    }

    private JLabel createDetailRow(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        JLabel valueLabel = new JLabel();
        panel.add(label);
        panel.add(valueLabel);
        return valueLabel;
    }

    private void updateDetailsPanel(int selectedRow) {
        String transactionType = String.valueOf(tableModel.getValueAt(selectedRow, 1));
        if (transactionType.equals("Выдача")){
            try {
                DispensingDTO dispensingDTO = controller.getDispensing(Map.of("transaction_id", tableModel.getValueAt(selectedRow, 0))).stream().findFirst().orElse(null);
                PatientDTO patientDTO = controller.getParients(Map.of("id", dispensingDTO.getPatientId())).stream().findFirst().orElse(null);
                transactionType += ": полис -> " +patientDTO.getPolicy() + " | " + dispensingDTO.getStatus();
            }catch (NullPointerException e){
                transactionType += ": пациент не найден";
            }
        }
        DrugDTO drugDTO = controller.getDrugById(tableModel.getValueAt(selectedRow, 2));
        EmployeeDTO employeeDTO = controller.getEmployeeById(tableModel.getValueAt(selectedRow, 4));
        DepartmentDTO departmentDTO = controller.getDepartmentById(employeeDTO.getDepartmentId());
        StoreDTO storeDTO = controller.getStoresById(tableModel.getValueAt(selectedRow, 6));
        idLabel.setText(String.valueOf(tableModel.getValueAt(selectedRow, 0)));
        typeLabel.setText(transactionType);
        drugInfoLabel.setText("Название: " + drugDTO.getName() + "; Форма выпуска: " + drugDTO.getReleaseForm() + "; Дозировка :" + drugDTO.getDose());
        countLabel.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
        employeeInfoLabel.setText("Id: " + employeeDTO.getId() + "; ФИО: " + employeeDTO.getFullName() + "; Паспорт: " + employeeDTO.getPassport() + "; Отделение :" + departmentDTO.getName());
        dateLabel.setText(String.valueOf(tableModel.getValueAt(selectedRow, 5)));
        storeInfoLabel.setText("Id: " + storeDTO.getId() + "; Отделение: " + controller.getDepartmentById(storeDTO.getDepartmentId()).getName() + "; Администратор: " + controller.getEmployeeById(storeDTO.getAdminostratorId()).getFullName());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void updateData() {
        tableModel.setRowCount(0);
        Collection<TransactionDTO> transactions = controller.getAllTransactions();
        for (TransactionDTO transaction : transactions) {
            tableModel.addRow(new Object[]{
                    transaction.getId(),
                    transaction.getTransactionType(),
                    transaction.getDrugId(),
                    transaction.getDrugsCount(),
                    transaction.getEmployeeId(),
                    transaction.getOperationDate(),
                    transaction.getStoreId()
            });
        }
    }

    private void performSearch(String column, String searchText) {
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Введите текст для поиска.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Collection<TransactionDTO> searchResults = controller.searchTransactions(column, searchText);

            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Совпадений не найдено.", "Результат поиска", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Очистить текущую таблицу и заполнить её результатами поиска
                tableModel.setRowCount(0);
                for (TransactionDTO transaction : searchResults) {
                    tableModel.addRow(new Object[]{
                            transaction.getId(),
                            transaction.getTransactionType(),
                            transaction.getDrugId(),
                            transaction.getDrugsCount(),
                            transaction.getEmployeeId(),
                            transaction.getOperationDate(),
                            transaction.getStoreId()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Ошибка при выполнении поиска: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showEditDialog(TransactionDTO transaction) {
        // Окно редактирования транзакции
        JDialog editDialog = new JDialog((Frame) null, "Редактирование транзакции", true);
        editDialog.setLayout(new GridBagLayout());
        editDialog.setSize(400, 300);
        editDialog.setLocationRelativeTo(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Поля редактирования


        JTextField typeField = new JTextField(transaction.getTransactionType());

        JTextField drugIdField = new JTextField(transaction.getDrugId().toString());
        JTextField countField = new JTextField(transaction.getDrugsCount().toString());
        JTextField employeeIdField = new JTextField(transaction.getEmployeeId().toString());
        JTextField storeIdField = new JTextField(transaction.getStoreId().toString());

        gbc.gridx = 0; gbc.gridy = 0;
        editDialog.add(new JLabel("Тип:"), gbc);
        gbc.gridx = 1;


        editDialog.add(typeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        editDialog.add(new JLabel("ID Препарата:"), gbc);
        gbc.gridx = 1;
        editDialog.add(drugIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        editDialog.add(new JLabel("Кол-во:"), gbc);
        gbc.gridx = 1;
        editDialog.add(countField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        editDialog.add(new JLabel("ID Сотрудника:"), gbc);
        gbc.gridx = 1;
        editDialog.add(employeeIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        editDialog.add(new JLabel("ID Склада:"), gbc);
        gbc.gridx = 1;
        editDialog.add(storeIdField, gbc);

        JButton saveButton = new JButton("Сохранить");
//        saveButton.addActionListener(e -> {
//            // Обновление данных транзакции
//            transaction.setTransactionType(typeField.getText());
//            transaction.setDrugId(Integer.parseInt(drugIdField.getText()));
//            transaction.setDrugsCount(Integer.parseInt(countField.getText()));
//            transaction.setEmployeeId(Integer.parseInt(employeeIdField.getText()));
//            transaction.setStoreId(Integer.parseInt(storeIdField.getText()));
//            controller.updateTransaction(transaction); // Обновление через контроллер
//            editDialog.dispose();
//        });

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        editDialog.add(saveButton, gbc);

        editDialog.setVisible(true);
    }
}

