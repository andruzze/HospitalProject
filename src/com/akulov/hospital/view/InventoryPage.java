package com.akulov.hospital.view;

import com.akulov.hospital.controller.Controller;
import com.akulov.hospital.model.dto.entity.DrugDTO;
import com.akulov.hospital.model.dto.entity.InventoryDTO;
import com.akulov.hospital.model.dto.entity.InventoryDetailsDTO;
import com.akulov.hospital.model.dto.entity.StoreDTO;
import com.akulov.hospital.view.style.CustomTableCellRenderer;
import org.postgresql.util.PSQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class InventoryPage implements Page {
    private final Controller controller;
    private final JPanel panel;
    private final JTable inventoryTable;
    private final DefaultTableModel tableModel;
    private final JPanel detailsPanel;
    private final JTable detailsTable;
    private final DefaultTableModel detailsTableModel;


    public InventoryPage(Controller controller) {
        this.controller = controller;
        panel = new JPanel(new BorderLayout());

        // Основная таблица с инвентаризациями
        String[] columnNames = {"ID", "ID Сотрудника", "Номер кабинета", "Id склада", "Дата инвентаризации","Подпись"};
        tableModel = new DefaultTableModel(columnNames, 0);
        inventoryTable = new JTable(tableModel);

        JScrollPane inventoryScrollPane = new JScrollPane(inventoryTable);

        // Панель управления
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JComboBox<String> searchColumnComboBox = new JComboBox<>(columnNames);
        controlPanel.add(new JLabel("Поиск:"));
        controlPanel.add(searchColumnComboBox);

        JTextField searchField = new JTextField(15);
        controlPanel.add(searchField);

        JButton searchButton = new JButton("Найти");
        controlPanel.add(searchButton);

        JButton addInventoryButton = new JButton("Добавить");
        addInventoryButton.addActionListener(e -> addInventory());
        controlPanel.add(addInventoryButton);

        JButton updateButton = new JButton("Обновить");
        updateButton.addActionListener(e -> this.updateData());
        controlPanel.add(updateButton);

        JButton deleteInventoryButton = new JButton("Удалить");
        deleteInventoryButton.addActionListener(e -> DeleteInventory());
        controlPanel.add(deleteInventoryButton);

        JButton signButton = new JButton("Утвердить");
        signButton.addActionListener(e -> SignInventory());
        controlPanel.add(signButton);


        panel.add(controlPanel, BorderLayout.NORTH);

        // Панель деталей инвентаризации
        detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Детали инвентаризации"));

        String[] detailsColumnNames = {"Номер записи", "Номер препарата", "Название препарата", "Текущее количество", "Количество до инвентаризации"};
        detailsTableModel = new DefaultTableModel(detailsColumnNames, 0);
        detailsTable = new JTable(detailsTableModel);

        JScrollPane detailsScrollPane = new JScrollPane(detailsTable);

        JPanel detailsControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addDrugButton = new JButton("Добавить препарат");
        addDrugButton.addActionListener(e -> addInventoryDetail());
        detailsControlPanel.add(addDrugButton);

        JButton deleteDrugButton = new JButton("Удалить запись");
        deleteDrugButton.addActionListener(e -> deleteInventoryDetails());
        detailsControlPanel.add(deleteDrugButton);

        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);
        detailsPanel.add(detailsControlPanel, BorderLayout.SOUTH);

        // Используем JSplitPane для разделения панелей
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inventoryScrollPane, detailsPanel);
        splitPane.setResizeWeight(0.5); // Устанавливаем равное распределение пространства
        splitPane.setDividerLocation(0.5); // Стартовое положение делителя

        panel.add(splitPane, BorderLayout.CENTER);

        // Обработчик кнопки поиска
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            String selectedColumn = (String) searchColumnComboBox.getSelectedItem();
            performSearch(selectedColumn, searchText);
        });

        // Обработчик выбора строки в таблице
        inventoryTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                Integer inventoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
                updateDetailsTable(inventoryId);
            }
        });

        updateData();
    }

    private void SignInventory() {

        //todo impl
        JDialog dialog = new JDialog((Frame) null, "Утверждение инвентаризации", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(panel);

        // Проверяем, выбрана ли строка
        final int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(dialog, "Выберите инвентаризацию");
            return;
        }

        // Получаем ID авторизованного пользователя и проверяем права доступа
        final int authUserId = controller.getAuthUser();
        final Object currentStoreId = inventoryTable.getValueAt(selectedRow, 3);
        StoreDTO storeDTO = controller.getStoreById((Integer) currentStoreId);
        if (!storeDTO.getAdminostratorId().equals(authUserId)) {
            JOptionPane.showMessageDialog(dialog, "Вы не можете подписать эту инвентаризацию", "Отказано в доступе", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Создаем текст и кнопку
        JLabel signLabel = new JLabel("<html><p style='text-align: center;'>Вы действительно хотите подписать инвентаризацию?</p></html>", SwingConstants.CENTER);
        JButton signBtn = new JButton("Подписать");

        // Обработчик кнопки
        signBtn.addActionListener(e -> {
            controller.signInventory((Integer) inventoryTable.getValueAt(selectedRow, 0), authUserId);
            dialog.dispose(); // Закрываем диалог после подтверждения
            this.updateData();
        });

        // Используем панель с BoxLayout для вертикального расположения
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(signLabel);
        contentPanel.add(Box.createVerticalStrut(10)); // Добавляем отступ между текстом и кнопкой
        contentPanel.add(signBtn);

        // Центрируем содержимое
        signLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }


    private void DeleteInventory() {
        final int selectedRow = inventoryTable.getSelectedRow();
        controller.deleteInventory((Integer)inventoryTable.getValueAt(selectedRow, 0));
        updateData();
    }

    private void deleteInventoryDetails() {
        final int selectedRow = detailsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Выберите запись о препарате", "Ошибка", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        controller.deleteInventoryDetail(detailsTable.getValueAt(selectedRow, 0));
        updateDetailsTable((Integer) inventoryTable.getValueAt(inventoryTable.getSelectedRow(), 0));
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void updateData() {
        // Очистка основной таблицы
        tableModel.setRowCount(0);

        // Загрузка данных инвентаризаций
        Collection<InventoryDTO> inventories = controller.getAllInventories();
        for (InventoryDTO inventory : inventories) {
            tableModel.addRow(new Object[]{
                    inventory.getId(),
                    inventory.getEmployeeId(),
                    controller.getStoresById(inventory.getStoreId()).getCabinetNumber(),
                    inventory.getStoreId(),
                    inventory.getInventoryDate(),
                    inventory.getSign()
            });
        }

        // Очистка таблицы деталей
        detailsTableModel.setRowCount(0);
    }

    private void performSearch(String column, String searchText) {
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Введите текст для поиска.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Collection<InventoryDTO> searchResults = controller.searchInventories(column, searchText);

            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Совпадений не найдено.", "Результат поиска", JOptionPane.INFORMATION_MESSAGE);
            } else {
                tableModel.setRowCount(0);
                for (InventoryDTO inventory : searchResults) {
                    tableModel.addRow(new Object[]{
                            inventory.getId(),
                            inventory.getEmployeeId(),
                            controller.getStoresById(inventory.getStoreId()).getCabinetNumber(),
                            inventory.getStoreId(),
                            inventory.getInventoryDate()
                    });
                }
            }
        }catch (NullPointerException e){
            JOptionPane.showMessageDialog(panel, "Ошибка при выполнении поиска: Такой записи не найдено", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Ошибка при выполнении поиска: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDetailsTable(Integer inventoryId) {
        // Очистка таблицы деталей
        detailsTableModel.setRowCount(0);
        // Загрузка деталей инвентаризации
        Collection<InventoryDetailsDTO> details = controller.getInventoryDetails(inventoryId);
        for (InventoryDetailsDTO detail : details) {
            detailsTableModel.addRow(new Object[]{
                    detail.getId(),
                    detail.getDrugId(),
                    controller.getDrugById(detail.getDrugId()).getName(), // Метод для получения названия препарата
                    detail.getDrugsCount(),
                    detail.getDrugsCountBefore()
            });
        }

        detailsTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
    }

    private void addInventory() {
        // Окно для добавления новой инвентаризации
        JDialog dialog = new JDialog((Frame) null, "Добавить инвентаризацию", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(500, 200);
        dialog.setLocationRelativeTo(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> storeField = new JComboBox(controller.getStoresNames());

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Выберите склад: "), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        dialog.add(storeField, gbc);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            try {

                Integer storeId = Integer.parseInt(storeField.getSelectedItem().toString().split(" ")[0]);
                controller.addInventory(new InventoryDTO(null, controller.getAuthUser(), storeId, LocalDate.now(), "Не подписана")); // Создание новой инвентаризации
                dialog.dispose();
                updateData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        dialog.setVisible(true);
        updateData();
    }

    private void addInventoryDetail() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Не выбрана инвентаризация", "Информация", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object date = LocalDate.parse(inventoryTable.getValueAt(selectedRow, 4).toString());
        if(!date.equals(LocalDate.now())){
            JOptionPane.showMessageDialog(panel, "Вы не можете редактировать данные инвентаризации за прошедшие дни", "Информация", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JDialog dialog = new JDialog((Frame) null, "Добавить инвентаризацию", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(500, 200);
        dialog.setLocationRelativeTo(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Увеличенные отступы для лучшего внешнего вида
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Collection<DrugDTO> drugsDTO = controller.getAllDrugs();
        ArrayList<String> drugsName = new ArrayList<>();
        drugsDTO.forEach(obj -> drugsName.add(obj.getId() + " " + obj.getName() + " " + obj.getReleaseForm()));
        JComboBox<String> drugsComboBox = new JComboBox<>(drugsName.toArray(new String[0]));

        // Поле выбора препарата
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        dialog.add(new JLabel("Выберите препарат: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        dialog.add(drugsComboBox, gbc);

        // Поле для ввода количества на складе
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        dialog.add(new JLabel("Кол-во препарата на складе: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JTextField drugCountField = new JTextField();
        drugCountField.setPreferredSize(new Dimension(200, 25)); // Увеличение размера TextField
        dialog.add(drugCountField, gbc);

        // Кнопка подтверждения
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton confirmButton = new JButton("Добавить");
        confirmButton.addActionListener(e -> {
            String selectedDrug = (String) drugsComboBox.getSelectedItem();
            String drugCount = drugCountField.getText().trim();
            if (selectedDrug == null || drugCount.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    controller.addInventoryDetails(
                            (Integer)inventoryTable.getValueAt(selectedRow, 0),
                            (Integer) inventoryTable.getValueAt(selectedRow, 3),
                            Integer.parseInt(drugsComboBox.getSelectedItem().toString().split(" ")[0]),
                            Integer.parseInt(drugCountField.getText())
                    );
                }catch (PSQLException er){
                    JOptionPane.showMessageDialog(dialog, er.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(dialog, "Препарат добавлен: " + selectedDrug + " (" + drugCount + ")", "Успех", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            }
        });
        dialog.add(confirmButton, gbc);
        dialog.setVisible(true);
        updateDetailsTable((Integer) inventoryTable.getValueAt(selectedRow, 0 ));
    }

}
