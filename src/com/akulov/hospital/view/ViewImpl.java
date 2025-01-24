package com.akulov.hospital.view;

import com.akulov.hospital.controller.Controller;
import com.akulov.hospital.controller.ControllerImpl;
import javax.swing.*;
import java.awt.*;
import java.util.Map;


public class ViewImpl implements View {
    private final Controller controller;
    private JFrame frame;
    private JPanel mainPanel; // Главная панель с CardLayout
    private CardLayout cardLayout;
    private DrugPage drugsPage;
    private StoresPage storesPage;
    private TransactionPage transactionsPage;
    private InventoryPage inventoryPage;
    private DispensingPage dispensingPage;

    public ViewImpl(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void start() {
        // Окно авторизации
        if (!showLoginDialog()) {
            return; // Если авторизация не пройдена, программа не запускается
        }

        frame = new JFrame("Управление госпиталем");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        drugsPage = new DrugPage(controller);
        storesPage = new StoresPage(controller);
        transactionsPage = new TransactionPage(controller);
        inventoryPage = new InventoryPage(controller);
        dispensingPage = new DispensingPage(controller);

        mainPanel.add(transactionsPage.getPanel(), "TransactionsPage");
        mainPanel.add(drugsPage.getPanel(), "DrugsPage");
        mainPanel.add(storesPage.getPanel(), "StoresPage");
        mainPanel.add(inventoryPage.getPanel(), "InventoryPage");
        mainPanel.add(dispensingPage.getPanel(), "DispensingPage");

        frame.setJMenuBar(createMenuBar());

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Создает меню с навигацией и пунктом выхода.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu navigationMenu = new JMenu("Навигация");

        JMenuItem drugsPageItem = new JMenuItem("Препараты");
        JMenuItem storesPageItem = new JMenuItem("Склады");
        JMenuItem transactionsPageItem = new JMenuItem("Журнал транзакций");
        JMenuItem inventoryPageItem = new JMenuItem("Инвентаризация");
        JMenuItem dispensingPageItem = new JMenuItem("Выдача препаратов");
        JMenuItem logoutItem = new JMenuItem("Выход");

        drugsPageItem.addActionListener(e ->{
                cardLayout.show(mainPanel, "DrugsPage");
                storesPage.updateData();});
        storesPageItem.addActionListener(e -> cardLayout.show(mainPanel, "StoresPage"));
        transactionsPageItem.addActionListener(e -> {
            transactionsPage.updateData(); // Обновляем данные перед отображением
            cardLayout.show(mainPanel, "TransactionsPage");
        });
        inventoryPageItem.addActionListener(e -> cardLayout.show(mainPanel, "InventoryPage"));
        dispensingPageItem.addActionListener(e->cardLayout.show(mainPanel, "DispensingPage"));
        logoutItem.addActionListener(e -> System.exit(0)); // Завершает приложение

        navigationMenu.add(drugsPageItem);
        navigationMenu.add(storesPageItem);
        navigationMenu.add(transactionsPageItem);
        navigationMenu.add(inventoryPageItem);
        navigationMenu.add(dispensingPageItem);
        navigationMenu.addSeparator(); // Разделитель
        navigationMenu.add(logoutItem);

        menuBar.add(navigationMenu);
        return menuBar;
    }

    /**
     * Отображает диалоговое окно авторизации.
     * @return true, если авторизация успешна, иначе false.
     */
    private boolean showLoginDialog() {
        // Диалоговое окно авторизации
        JDialog loginDialog = new JDialog((Frame) null, "Авторизация", true);
        loginDialog.setLayout(new GridBagLayout());
        loginDialog.setSize(300, 200);
        loginDialog.setLocationRelativeTo(null);

        // Поля для ввода логина и пароля
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Войти");
        JButton cancelButton = new JButton("Отмена");

        // Разметка
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        loginDialog.add(new JLabel("Логин:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        loginDialog.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginDialog.add(new JLabel("Пароль:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        loginDialog.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        loginDialog.add(loginButton, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        loginDialog.add(cancelButton, gbc);

        // Обработчики кнопок
        final boolean[] isAuthenticated = {false};

        loginButton.addActionListener(e -> {
            String login = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            boolean auth = controller.authEmployee(login, password);

            if (controller.authEmployee(login, password)) {
                isAuthenticated[0] = true;
                loginDialog.dispose(); // Закрыть диалог
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Неверный логин или пароль!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            loginDialog.dispose(); // Закрыть диалог без авторизации
        });

        loginDialog.setVisible(true);
        return isAuthenticated[0];
    }
}


