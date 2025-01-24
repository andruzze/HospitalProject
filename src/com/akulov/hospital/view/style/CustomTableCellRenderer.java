package com.akulov.hospital.view.style;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Получение стандартного компонента рендера
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Получение значений "drugsCount" и "drugsCountBefore" для текущей строки
        int drugsCount = (int) table.getValueAt(row, 3); // Индекс столбца "drugsCount"
        int drugsCountBefore = (int) table.getValueAt(row, 4); // Индекс столбца "drugsCountBefore"

        // Логика окраски строки
        if (drugsCount < drugsCountBefore) {
            c.setBackground(Color.RED); // Красный, если количество уменьшилось
            c.setForeground(Color.WHITE); // Белый текст
        } else if (drugsCount > drugsCountBefore) {
            c.setBackground(Color.GREEN); // Зеленый, если количество увеличилось
            c.setForeground(Color.BLACK); // Черный текст
        } else {
            c.setBackground(Color.WHITE); // Обычный цвет для равного количества
            c.setForeground(Color.BLACK); // Черный текст
        }

        // Если строка выделена, оставить цвет выделения
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        }

        return c;
    }
}