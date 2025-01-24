package com.akulov.hospital.view;

import com.akulov.hospital.controller.Controller;
import com.akulov.hospital.model.dto.entity.DepartmentDTO;
import com.akulov.hospital.model.dto.entity.EmployeeDTO;
import com.akulov.hospital.model.dto.entity.StoreDTO;
import com.akulov.hospital.model.dto.entity.StoreDrugsDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;


public class StoresPage implements Page {
    private final Controller controller;
    private final JPanel panel;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTable drugsTable;
    private final DefaultTableModel drugsTableModel;
    private final JPanel storeDetailsPanel;
    private final JComboBox<String> columnSelector;
    private final JTextField searchField;
    private final JButton searchButton;

    public StoresPage(Controller controller) {
        this.controller = controller;
        this.panel = new JPanel(new BorderLayout());

        // Создаем таблицу складов
        String[] columnNames = {"ID", "Номер кабинета", "Отделение", "Администратор", "Вместительность", "Текущая заполненность"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Добавляем поиск
        JButton updateTable = new JButton("Обновить");
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        searchButton = new JButton("Поиск");

        String[] searchColumns = {"ID", "Номер кабинета", "Отделение", "Администратор", "Вместительность", "Текущая заполненность"};
        columnSelector = new JComboBox<>(searchColumns);
        searchPanel.add(new JLabel("Искать по:"));
        searchPanel.add(columnSelector);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(updateTable);
        topPanel.add(searchPanel, BorderLayout.WEST);

        // Кнопки управления


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Добавить склад");
        JButton deleteButton = new JButton("Удалить склад");
        buttonPanel.add(addButton);

        buttonPanel.add(deleteButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        // Обработчик поиска
        searchButton.addActionListener(e -> performSearch());
        updateTable.addActionListener(e -> this.updateData());

        // Таблица складов
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Секция под таблицей складов
        JPanel detailsSection = new JPanel(new GridBagLayout());
        GridBagConstraints detailsGbc = new GridBagConstraints();
        detailsGbc.insets = new Insets(5, 5, 5, 5);
        detailsGbc.fill = GridBagConstraints.BOTH;

        // Левая часть: таблица деталей хранения препаратов
        detailsGbc.gridx = 0;
        detailsGbc.gridy = 0;
        detailsGbc.weightx = 0.7; // Таблица занимает больше пространства по горизонтали
        detailsGbc.weighty = 1.0; // Таблица растягивается вертикально
        JPanel drugsPanel = new JPanel(new BorderLayout());
        drugsPanel.setBorder(BorderFactory.createTitledBorder("Детали хранения препаратов"));
        String[] drugsColumnNames = {"ID", "Номер препарата", "Количество", "Дата последнего пополнения", "Дата последнего списния"};
        drugsTableModel = new DefaultTableModel(drugsColumnNames, 0);
        drugsTable = new JTable(drugsTableModel);
        drugsPanel.add(new JScrollPane(drugsTable), BorderLayout.CENTER);
        detailsSection.add(drugsPanel, detailsGbc);

        // Правая часть: подробная информация о складе
        detailsGbc.gridx = 1;
        detailsGbc.gridy = 0;
        detailsGbc.weightx = 0.3; // Панель с информацией занимает меньше пространства
        detailsGbc.weighty = 1.0; // Вертикальное растяжение
        JPanel storeDetailsContainer = new JPanel(new BorderLayout());
        storeDetailsContainer.setBorder(BorderFactory.createTitledBorder("Информация о складе"));
        storeDetailsPanel = new JPanel(new GridBagLayout());
        storeDetailsContainer.add(new JScrollPane(storeDetailsPanel), BorderLayout.CENTER);
        detailsSection.add(storeDetailsContainer, detailsGbc);

        // Используем JSplitPane для разделения секций
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, detailsSection);
        splitPane.setDividerLocation(400); // Устанавливаем первичный размер таблицы складов
        splitPane.setResizeWeight(0.7); // 70% пространства для таблицы складов

        panel.add(splitPane, BorderLayout.CENTER);

        // Обработчик выбора строки в таблице складов
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Integer storeId = (Integer) table.getValueAt(selectedRow, 0);
                updateDetailsSection(storeId);
            }
        });

        // Кнопки управления
        addButton.addActionListener(e -> showAddStoreDialog());
        deleteButton.addActionListener(e -> deleteSelectedStore());

        updateData();
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void updateData() {
        System.out.println(1);
        tableModel.setRowCount(0);
        for (StoreDTO store : controller.getAllStores()) {
            tableModel.addRow(new Object[]{
                    store.getId(),
                    store.getCabinetNumber(),
                    store.getDepartmentId(),
                    store.getAdminostratorId(),
                    store.getCapacity(),
                    store.getCurrentFill()
            });
        }
    }

    private void performSearch() {
        String keyword = searchField.getText().trim(); // Получаем значение из текстового поля
        if (keyword.isEmpty()) {
            updateData(); // Обновляем таблицу, если поле пустое
            JOptionPane.showMessageDialog(panel, "Введите значение для поиска.");
            return;
        }

        Object arg = keyword; // Изначально аргумент — строка
        String selectedColumnItem = columnSelector.getSelectedItem().toString(); // Получаем выбранный столбец
        try {
            // Преобразуем аргумент в целое число, если выбранный столбец — ID, вместительность или заполненность
            if (selectedColumnItem.equals("ID") || selectedColumnItem.equals("Вместительность") || selectedColumnItem.equals("Текущая заполненность")) {
                arg = Integer.parseInt(keyword);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Введите корректное числовое значение для столбца \"" + selectedColumnItem + "\".", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Получаем отфильтрованные данные от контроллера
        Collection<StoreDTO> filteredStores = controller.searchStores(selectedColumnItem, arg.toString());

        // Обновляем таблицу
        tableModel.setRowCount(0); // Очистка таблицы
        for (StoreDTO store : filteredStores) {
            tableModel.addRow(new Object[]{
                    store.getId(),
                    store.getCabinetNumber(),
                    store.getDepartmentId(),
                    store.getAdminostratorId(),
                    store.getCapacity(),
                    store.getCurrentFill()
            });
        }
    }

    private void updateDetailsSection(Integer storeId) {
        StoreDTO store = controller.getStoresById(storeId);
        Collection<StoreDrugsDTO> storeDrugs = controller.getDrugsByStoreId(storeId);

        // Очистка панели
        storeDetailsPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Добавляем метки с данными о складе
        gbc.gridx = 0;
        gbc.gridy = 0;
        storeDetailsPanel.add(new JLabel("ID Склада:"), gbc);
        gbc.gridx = 1;
        storeDetailsPanel.add(new JLabel(String.valueOf(store.getId())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        storeDetailsPanel.add(new JLabel("Отделение:"), gbc);
        gbc.gridx = 1;
        storeDetailsPanel.add(new JLabel(String.valueOf("id:"+store.getDepartmentId())), gbc);
        DepartmentDTO dep = controller.getDepartmentById(store.getDepartmentId());
        gbc.gridy = 2;
        storeDetailsPanel.add(new JLabel(dep.getName() ), gbc);
        gbc.gridy = 3;
        storeDetailsPanel.add(new JLabel("Контакты: " + dep.getTelephone()), gbc);



        gbc.gridx = 0;
        gbc.gridy = 4;
        storeDetailsPanel.add(new JLabel("Администратор:"), gbc);
        gbc.gridx = 1;
        storeDetailsPanel.add(new JLabel(String.valueOf("id: " +store.getAdminostratorId())), gbc);
        EmployeeDTO admin = controller.getEmployeeById(store.getAdminostratorId());
        gbc.gridy =5;
        storeDetailsPanel.add(new JLabel("ФИО: " + admin.getFullName().toString()), gbc);
        gbc.gridy =6;
        storeDetailsPanel.add(new JLabel("Номер телефона: " + admin.getTelephone()), gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        storeDetailsPanel.add(new JLabel("Вместительность:"), gbc);
        gbc.gridx = 1;
        storeDetailsPanel.add(new JLabel(String.valueOf(store.getCapacity())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        storeDetailsPanel.add(new JLabel("Текущая заполненность:"), gbc);
        gbc.gridx = 1;

        // Создаем ProgressBar для отображения заполненности
        JProgressBar progressBar = new JProgressBar(0, store.getCapacity()); // Задаем диапазон от 0 до Capacity
        progressBar.setValue(store.getCurrentFill()); // Устанавливаем текущее значение
        progressBar.setStringPainted(true); // Включаем отображение текста (например, 50%)
        storeDetailsPanel.add(progressBar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        storeDetailsPanel.add(new JLabel("Кабинет:"), gbc);
        gbc.gridx = 1;
        storeDetailsPanel.add(new JLabel(String.valueOf(store.getCabinetNumber())), gbc);

        // Обновление таблицы деталей хранения препаратов
        drugsTableModel.setRowCount(0);
        for (StoreDrugsDTO drug : storeDrugs) {
            drugsTableModel.addRow(new Object[]{
                    drug.getId(),
                    drug.getDrugId(),
                    drug.getDrugsCount(),
                    drug.getLastRefillDate(),
                    drug.getLastWriteOff()
            });
        }

        // Перерисовываем панель
        storeDetailsPanel.revalidate();
        storeDetailsPanel.repaint();
    }

    private void showAddStoreDialog() {
        // Создание диалогового окна
        JDialog dialog = new JDialog((Frame) null, "Добавить склад", true);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(panel);

        // Поля ввода
        JLabel departmentIdLabel = new JLabel("ID отдела:");
        JTextField departmentIdField = new JTextField();

        JLabel administratorIdLabel = new JLabel("ID администратора:");
        JTextField administratorIdField = new JTextField();

        JLabel capacityLabel = new JLabel("Вместимость склада:");
        JTextField capacityField = new JTextField();


        JLabel cabinetNumberLabel = new JLabel("Номер кабинета:");
        JTextField cabinetNumberField = new JTextField();

        // Кнопки
        JButton addButton = new JButton("Добавить");
        JButton cancelButton = new JButton("Отмена");

        // Добавление компонентов в диалог
        dialog.add(departmentIdLabel);
        dialog.add(departmentIdField);
        dialog.add(administratorIdLabel);
        dialog.add(administratorIdField);
        dialog.add(capacityLabel);
        dialog.add(capacityField);
        dialog.add(cabinetNumberLabel);
        dialog.add(cabinetNumberField);
        dialog.add(new JLabel()); // Пустая ячейка для выравнивания кнопок
        dialog.add(addButton);
        dialog.add(new JLabel());
        dialog.add(cancelButton);

        // Обработчик для кнопки "Добавить"
        addButton.addActionListener(e -> {
            String departmentIdText = departmentIdField.getText().trim();
            String administratorIdText = administratorIdField.getText().trim();
            String capacityText = capacityField.getText().trim();
            String cabinetNumberText = cabinetNumberField.getText().trim();

            // Проверка, что все поля заполнены
            if (departmentIdText.isEmpty() || administratorIdText.isEmpty() ||
                    capacityText.isEmpty() || cabinetNumberText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Заполните все поля.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Преобразование полей в соответствующие типы
                int departmentId = Integer.parseInt(departmentIdText);
                int administratorId = Integer.parseInt(administratorIdText);
                int capacity = Integer.parseInt(capacityText);
                int currentFill = 0;
                int cabinetNumber = Integer.parseInt(cabinetNumberText);

                // Вызов метода контроллера для добавления склада
                StoreDTO store = new StoreDTO(null, departmentId, administratorId, capacity, currentFill, cabinetNumber);
                controller.addStore(store);
                JOptionPane.showMessageDialog(dialog, "Склад успешно добавлен.", "Успех", JOptionPane.INFORMATION_MESSAGE);
                updateData(); // Обновление данных в таблице
                dialog.dispose(); // Закрытие диалога

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Все числовые поля должны содержать только числа.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Обработчик для кнопки "Отмена"
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }



    private void deleteSelectedStore() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            Integer storeId = (Integer) table.getValueAt(selectedRow, 0);
            controller.deleteStore(storeId);
            updateData();
        } else {
            JOptionPane.showMessageDialog(panel, "Выберите склад для удаления.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}


