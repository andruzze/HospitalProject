package com.akulov.hospital.view;

import com.akulov.hospital.controller.Controller;
import com.akulov.hospital.controller.ControllerImpl;
import com.akulov.hospital.model.dto.entity.DrugDTO;
import com.akulov.hospital.model.dto.entity.TransactionDTO;
import org.postgresql.util.PSQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Map;

public class DrugPage implements Page {
    private final Controller controller;
    private final JPanel panel; // Главная панель страницы

    // Секция 1: Таблица препаратов
    private final JTable drugTable;
    private final DefaultTableModel drugTableModel;

    // Секция 2: Поиск
    private final JComboBox<String> columnSelector;
    private final JTextField searchField;
    private final JButton searchButton;
    private final JPanel drugDetailsPanel;

    private final JButton addButton;
    private final JButton updateButton;
    private final JButton deleteButton;



    public DrugPage(Controller controller) {
        this.controller = controller;
        this.panel = new JPanel(new BorderLayout());


        // Основная панель с пропорциональным распределением
        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // Вертикальное деление
        verticalSplit.setDividerLocation(0.6); // Верхняя часть занимает 60% высоты
        verticalSplit.setResizeWeight(0.6); // Верхняя часть получает преимущество при изменении размера

        // Секция 1: Таблица препаратов
        String[] drugColumnNames = {"ID", "Название", "Форма выпуска", "Дозировка", "Поставщик", "Срок годности", "Описание"};
        drugTableModel = new DefaultTableModel(drugColumnNames, 0);
        drugTable = new JTable(drugTableModel);
        updateDrugTable();

        JScrollPane drugScrollPane = new JScrollPane(drugTable);

        // Панель кнопок управления
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Добавить");
        updateButton = new JButton("Обновить");
        deleteButton = new JButton("Удалить");

        controlPanel.add(addButton);
        controlPanel.add(updateButton);
        controlPanel.add(deleteButton);

        // Обработчики кнопок управления
        addButton.addActionListener(e -> addDrug());
        updateButton.addActionListener(e -> updateDrug());
        deleteButton.addActionListener(e -> deleteDrug());

        // Секция 2: Поиск
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        columnSelector = new JComboBox<>(drugColumnNames); // Выбор колонки для поиска
        searchField = new JTextField(20);
        searchButton = new JButton("Найти");

        searchPanel.add(new JLabel("Поиск по:"));
        searchPanel.add(columnSelector);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Добавляем обработчик для кнопки поиска
        searchButton.addActionListener(e -> searchDrugs());

        // Верхняя панель: Поиск + Таблица препаратов + кнопки
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(drugScrollPane, BorderLayout.CENTER);
        topPanel.add(controlPanel, BorderLayout.SOUTH);

        verticalSplit.setTopComponent(topPanel);

        // Секция 3: Подробности
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.add(new JLabel("Детали по складам:"), BorderLayout.NORTH);
        verticalSplit.setBottomComponent(detailsPanel); // Нижняя часть разделителя

        this.drugDetailsPanel = new JPanel(new BorderLayout());
        drugDetailsPanel.setMinimumSize(new Dimension(100, 250));
        detailsPanel.add(drugDetailsPanel, BorderLayout.CENTER);




        panel.add(verticalSplit, BorderLayout.CENTER);

        // Обработчик выбора строки в таблице препаратов
        drugTable.getSelectionModel().addListSelectionListener(e -> showDrugDetails());
    }

    private void createTransaction() {
        int selectedDrugRow = drugTable.getSelectedRow();

        if (selectedDrugRow == -1) {
            JOptionPane.showMessageDialog(panel, "Пожалуйста, выберите препарат из таблицы!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer selectedDrugId = (Integer) drugTableModel.getValueAt(selectedDrugRow, 0);

        // Создание диалогового окна
        JDialog transactionDialog = new JDialog((Frame) null, "Создание транзакции", true);
        transactionDialog.setLayout(new GridBagLayout());
        transactionDialog.setSize(400, 450);
        transactionDialog.setLocationRelativeTo(null);

        // Элементы интерфейса
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Отступы
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Тип транзакции
        transactionDialog.add(new JLabel("Тип транзакции:"), gbc);
        JComboBox<String> transactionTypeCombo = new JComboBox<>(new String[]{"Списание", "Выдача", "Возврат", "Пополнение"});
        gbc.gridx = 1;
        transactionDialog.add(transactionTypeCombo, gbc);

        // Количество препаратов
        gbc.gridx = 0;
        gbc.gridy++;
        transactionDialog.add(new JLabel("Количество препаратов:"), gbc);
        JTextField drugCountField = new JTextField(10);
        gbc.gridx = 1;
        transactionDialog.add(drugCountField, gbc);

        // ID сотрудника
        gbc.gridx = 0;
        gbc.gridy++;
        transactionDialog.add(new JLabel("ID сотрудника:"), gbc);
        JLabel employeeIdField = new JLabel(controller.getAuthUser().toString());
        gbc.gridx = 1;
        transactionDialog.add(employeeIdField, gbc);

        // Дата операции
        gbc.gridx = 0;
        gbc.gridy++;
        transactionDialog.add(new JLabel("Дата операции:"), gbc);
        JLabel operationDateField = new JLabel(LocalDate.now().toString()); // По умолчанию текущая дата
        gbc.gridx = 1;
        transactionDialog.add(operationDateField, gbc);

        // ID склада
        gbc.gridx = 0;
        gbc.gridy++;
        transactionDialog.add(new JLabel("ID склада:"), gbc);
        JTextField storeIdField = new JTextField(10);
        gbc.gridx = 1;
        transactionDialog.add(storeIdField, gbc);

        // Поле для полиса пациента (изначально скрыто)
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel patientIdLabel = new JLabel("Полис пациента:");
        JTextField patientIdField = new JTextField(15);
        patientIdLabel.setVisible(false);
        patientIdField.setVisible(false);
        transactionDialog.add(patientIdLabel, gbc);
        gbc.gridx = 1;
        transactionDialog.add(patientIdField, gbc);

        // Слушатель для ComboBox
        transactionTypeCombo.addActionListener(e -> {
            String selectedType = (String) transactionTypeCombo.getSelectedItem();
            boolean isDispensing = "Выдача".equals(selectedType);
            patientIdLabel.setVisible(isDispensing);
            patientIdField.setVisible(isDispensing);
            transactionDialog.pack(); // Обновляем размер окна
        });

        // Кнопки
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton saveButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Отмена");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        transactionDialog.add(buttonPanel, gbc);

        // Логика кнопки "Сохранить"
        saveButton.addActionListener(e -> {
            try {
                String transactionType = (String) transactionTypeCombo.getSelectedItem();
                int drugsCount = Integer.parseInt(drugCountField.getText().trim());
                int employeeId = Integer.parseInt(employeeIdField.getText().trim());
                LocalDate operationDate = LocalDate.parse(operationDateField.getText().trim());
                int storeId = Integer.parseInt(storeIdField.getText().trim());

                Integer patientId = -1;
                if ("Выдача".equals(transactionType)) {
                    if (patientIdField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(transactionDialog, "Введите полис пациента.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    patientId = controller.getPatientId(Map.of("policy", patientIdField.getText()));
                }

                // Создание нового объекта TransactionDTO
                TransactionDTO transaction = new TransactionDTO(
                        null,
                        transactionType,
                        selectedDrugId,
                        drugsCount,
                        employeeId,
                        operationDate,
                        storeId
                );
                try{
                    if(patientId == -1){
                        controller.addTransaction(transaction);
                    }else {
                        controller.addTransaction(transaction, patientId);
                    }
                }catch (PSQLException exception) {
                    JOptionPane.showMessageDialog(transactionDialog, exception.getMessage(), "ОШИБКА", JOptionPane.ERROR_MESSAGE);
                    return;

                }
                JOptionPane.showMessageDialog(transactionDialog, "Транзакция успешно создана!");
                transactionDialog.dispose();


                // Добавление транзакции через контроллер


            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(transactionDialog, "Пожалуйста, введите корректные числовые значения.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(transactionDialog, "Введите дату в формате YYYY-MM-DD.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Логика кнопки "Отмена"
        cancelButton.addActionListener(e -> transactionDialog.dispose());

        transactionDialog.setVisible(true);
    }


    private void deleteDrug() {
        int selectedRow = drugTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Пожалуйста, выберите препарат для удаления!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(panel, "Вы уверены, что хотите удалить этот препарат?", "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Integer drugId = (Integer) drugTableModel.getValueAt(selectedRow, 0);

        try {
            controller.deleteDrug(drugId); // Вызов метода контроллера для удаления препарата
            updateDrugTable(); // Обновляем таблицу после удаления
            JOptionPane.showMessageDialog(panel, "Препарат успешно удален.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Ошибка при удалении препарата: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDrug() {
        int selectedRow = drugTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Пожалуйста, выберите препарат для обновления!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer drugId = (Integer) drugTableModel.getValueAt(selectedRow, 0);
        DrugDTO existingDrug = controller.getDrugs(Map.of("id", drugId)).stream().findFirst().orElse(null);

        if (existingDrug == null) {
            JOptionPane.showMessageDialog(panel, "Не удалось найти информацию о выбранном препарате.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Создаем диалоговое окно для обновления данных
        JDialog updateDrugDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(panel), "Обновить препарат", true);
        updateDrugDialog.setLayout(new GridBagLayout());
        updateDrugDialog.setSize(400, 300);
        updateDrugDialog.setLocationRelativeTo(panel);

        // Поля для ввода данных
        JTextField nameField = new JTextField(existingDrug.getName(), 20);
        JTextField releaseFormField = new JTextField(existingDrug.getReleaseForm(), 20);
        JTextField doseField = new JTextField(existingDrug.getDose(), 20);
        JTextField supplierField = new JTextField(existingDrug.getSupplier(), 20);
        JTextField shelfLifeField = new JTextField(existingDrug.getShelfLife().toString(), 20);
        JTextField descriptionField = new JTextField(existingDrug.getDescription(), 20);

        // Разметка формы
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        updateDrugDialog.add(new JLabel("Название:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        updateDrugDialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        updateDrugDialog.add(new JLabel("Форма выпуска:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        updateDrugDialog.add(releaseFormField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        updateDrugDialog.add(new JLabel("Дозировка:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        updateDrugDialog.add(doseField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        updateDrugDialog.add(new JLabel("Поставщик:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        updateDrugDialog.add(supplierField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        updateDrugDialog.add(new JLabel("Срок годности (дни):"), gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        updateDrugDialog.add(shelfLifeField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        updateDrugDialog.add(new JLabel("Описание:"), gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        updateDrugDialog.add(descriptionField, gbc);

        // Кнопки
        JButton saveButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Отмена");

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        updateDrugDialog.add(saveButton, gbc);

        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 1;
        updateDrugDialog.add(cancelButton, gbc);

        // Обработчики кнопок
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String releaseForm = releaseFormField.getText().trim();
                String dose = doseField.getText().trim();
                String supplier = supplierField.getText().trim();
                int shelfLife = Integer.parseInt(shelfLifeField.getText().trim());
                String description = descriptionField.getText().trim();

                if (name.isEmpty() || releaseForm.isEmpty() || dose.isEmpty() || supplier.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(updateDrugDialog, "Заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DrugDTO updatedDrug = new DrugDTO(drugId, name, releaseForm, dose, supplier, shelfLife, description);
                controller.updateDrug(updatedDrug, Map.of("id", drugId));
                updateDrugTable(); // Обновляем таблицу
                updateDrugDialog.dispose();
                JOptionPane.showMessageDialog(panel, "Препарат успешно обновлен.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(updateDrugDialog, "Срок годности должен быть числом!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> updateDrugDialog.dispose());

        updateDrugDialog.setVisible(true);
    }

    private void addDrug() {
        // Создаем диалог для добавления препарата
        JDialog addDrugDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(panel), "Добавить препарат", true);
        addDrugDialog.setLayout(new GridBagLayout());
        addDrugDialog.setSize(400, 300);
        addDrugDialog.setLocationRelativeTo(panel);


        // Поля для ввода данных
        JTextField nameField = new JTextField(20);
        JTextField releaseFormField = new JTextField(20);
        JTextField doseField = new JTextField(20);
        JTextField supplierField = new JTextField(20);
        JTextField shelfLifeField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);

        // Разметка формы
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        addDrugDialog.add(new JLabel("Название:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        addDrugDialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        addDrugDialog.add(new JLabel("Форма выпуска:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        addDrugDialog.add(releaseFormField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        addDrugDialog.add(new JLabel("Дозировка:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        addDrugDialog.add(doseField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        addDrugDialog.add(new JLabel("Поставщик:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        addDrugDialog.add(supplierField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        addDrugDialog.add(new JLabel("Срок годности (дни):"), gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        addDrugDialog.add(shelfLifeField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        addDrugDialog.add(new JLabel("Описание:"), gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        addDrugDialog.add(descriptionField, gbc);

        // Кнопки
        JButton saveButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Отмена");

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        addDrugDialog.add(saveButton, gbc);

        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 1;
        addDrugDialog.add(cancelButton, gbc);

        // Обработчики кнопок
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String releaseForm = releaseFormField.getText().trim();
                String dose = doseField.getText().trim();
                String supplier = supplierField.getText().trim();
                int shelfLife = Integer.parseInt(shelfLifeField.getText().trim());
                String description = descriptionField.getText().trim();

                if (name.isEmpty() || releaseForm.isEmpty() || dose.isEmpty() || supplier.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(addDrugDialog, "Заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Добавляем препарат через контроллер
                controller.addDrug(new DrugDTO(null, name, releaseForm, dose, supplier, shelfLife, description));
                updateDrugTable();
                addDrugDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addDrugDialog, "Срок годности должен быть числом!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> addDrugDialog.dispose());

        addDrugDialog.setVisible(true);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void updateData() {
        updateDrugTable();
    }

    /**
     * Обновляет таблицу препаратов.
     */
    private void updateDrugTable() {
        drugTableModel.setRowCount(0); // Очистка таблицы
        for (DrugDTO drug : controller.getAllDrugs()) {
            drugTableModel.addRow(new Object[]{
                    drug.getId(),
                    drug.getName(),
                    drug.getReleaseForm(),
                    drug.getDose(),
                    drug.getSupplier(),
                    drug.getShelfLife(),
                    drug.getDescription()
            });
        }
    }

    /**
     * Выполняет поиск препаратов по выбранной колонке и введенному значению.
     */
    private void searchDrugs() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            updateDrugTable();
            JOptionPane.showMessageDialog(panel, "Введите значение для поиска.");
            return;
        }
        Object arg = keyword;
        String selectedColumnItem = columnSelector.getSelectedItem().toString();
        if(selectedColumnItem == "ID"){
            arg = Integer.parseInt(keyword);
        }
        Collection<DrugDTO> filteredDrugs = controller.searchDrugs(selectedColumnItem, arg.toString());

        drugTableModel.setRowCount(0); // Очистка таблицы
        for (DrugDTO drug : filteredDrugs) {
            drugTableModel.addRow(new Object[]{
                    drug.getId(),
                    drug.getName(),
                    drug.getReleaseForm(),
                    drug.getDose(),
                    drug.getSupplier(),
                    drug.getShelfLife(),
                    drug.getDescription()
            });
        }
    }

    /**
     * Отображает подробную информацию о количестве препарата на складах.
     */
    private void showDrugDetails() {
        drugDetailsPanel.updateUI();
        int selectedRow = drugTable.getSelectedRow();
        if (selectedRow == -1){
            drugDetailsPanel.removeAll();
            return;
        }
        drugDetailsPanel.removeAll();
        Integer drugId = (Integer) drugTableModel.getValueAt(selectedRow, 0);

        Object[] drugDetails = controller.getStoregeDrugsById(drugId).toArray();
        DrugDTO drugDTOS = controller.getDrugs(Map.of("id", drugId)).stream().findFirst().orElse(null);
        JLabel drugName = new JLabel("Название :" + drugDTOS.getName());
        JLabel drugReleaseForm = new JLabel("Форма выпуска : " + drugDTOS.getReleaseForm());
        JLabel drugDose = new JLabel("Дозировка " + drugDTOS.getDose());
        JLabel shelfLife = new JLabel("Срок годности : " + drugDTOS.getShelfLife() + " дней");
        JLabel description = new JLabel("Описание: " + drugDTOS.getDescription());
        JButton transactionButton = new JButton("Создать новую транзакцию");
        drugDetailsPanel.add(transactionButton, BorderLayout.SOUTH);

        transactionButton.addActionListener(e -> createTransaction());
        JPanel drugsInfo = new JPanel(new GridBagLayout());
        JPanel storegeDrugsDetails = new JPanel(new GridBagLayout());

        drugDetailsPanel.add(drugsInfo, BorderLayout.NORTH);
        drugDetailsPanel.add(storegeDrugsDetails, BorderLayout.CENTER);

        //Разметка
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Отступы вокруг элементов
        gbc.fill = GridBagConstraints.HORIZONTAL; // Растягивать элементы по горизонтали

        // Первый столбец: метки
        gbc.weightx = 0.3; // Первый столбец занимает меньше места
        gbc.anchor = GridBagConstraints.EAST; // Выравнивание по правому краю
        gbc.gridy = 0; gbc.gridx = 0;
        drugsInfo.add(new JLabel("Название:"), gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        drugsInfo.add(new JLabel("Форма выпуска:"), gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        drugsInfo.add(new JLabel("Дозировка:"), gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        drugsInfo.add(new JLabel("Срок годности:"), gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        drugsInfo.add(new JLabel("Описание:"), gbc);

        // Второй столбец: данные
        gbc.weightx = 0.7; // Второй столбец занимает больше места
        gbc.anchor = GridBagConstraints.WEST; // Выравнивание по левому краю
        gbc.gridy = 0; gbc.gridx = 1;
        drugsInfo.add(new JLabel(drugDTOS.getName()), gbc);

        gbc.gridy = 1; gbc.gridx = 1;
        drugsInfo.add(new JLabel(drugDTOS.getReleaseForm()), gbc);

        gbc.gridy = 2; gbc.gridx = 1;
        drugsInfo.add(new JLabel(drugDTOS.getDose()), gbc);

        gbc.gridy = 3; gbc.gridx = 1;
        drugsInfo.add(new JLabel(drugDTOS.getShelfLife() + " дней"), gbc);

        gbc.gridy = 4; gbc.gridx = 1;
        drugsInfo.add(new JLabel(drugDTOS.getDescription()), gbc);


        gbc.weightx = 0.3; // Второй столбец занимает больше места
        gbc.anchor = GridBagConstraints.CENTER; // Выравнивание по левому краю
        gbc.gridy = 0; gbc.gridx =0;
        storegeDrugsDetails.add(new JLabel("Отделение : "), gbc);

        gbc.gridy = 0; gbc.gridx =1;
        storegeDrugsDetails.add(new JLabel("Номер склада : "), gbc);

        gbc.gridy = 0; gbc.gridx =2;
        storegeDrugsDetails.add(new JLabel("Кол-во препарата на складе : "), gbc);

        for(int i = 0; i<drugDetails.length; i ++ ){
            gbc.gridy = i/3 + 1; gbc.gridx =i%3;
            storegeDrugsDetails.add(new JLabel(drugDetails[i].toString()), gbc);
        }
    }
}
