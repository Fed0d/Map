package org.example.tools;

import javax.swing.*;
import java.awt.*;

/**
 * A renderer for color combo boxes.
 */
public class ColorComboBoxRenderer extends DefaultListCellRenderer {

    /**
     * Возвращает компонент для отображения в списке.
     *
     * @param list         список
     * @param value        значение
     * @param index        индекс
     * @param isSelected   выбран ли элемент
     * @param cellHasFocus фокус на ячейке
     * @return компонент для отображения в списке
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Color) {
            Color color = (Color) value;
            label.setBackground(color);
            label.setForeground(getContrastColor(color));
            label.setText(getColorName(color));
        }

        return label;
    }

    /**
     * Возвращает контрастный цвет для данного цвета.
     *
     * @param color цвет
     * @return контрастный цвет
     */
    private Color getContrastColor(Color color) {
        int luminance = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
        return luminance > 128 ? Color.BLACK : Color.WHITE;
    }

    /**
     * Возвращает имя цвета.
     *
     * @param color цвет
     * @return имя цвета
     */
    private String getColorName(Color color) {
        if (color.equals(Color.RED)) return "Red";
        else if (color.equals(Color.GREEN)) return "Green";
        else if (color.equals(Color.BLUE)) return "Blue";
        else if (color.equals(Color.YELLOW)) return "Yellow";
        else if (color.equals(Color.ORANGE)) return "Orange";
        else if (color.equals(Color.CYAN)) return "Cyan";
        else if (color.equals(Color.MAGENTA)) return "Magenta";
        else if (color.equals(Color.PINK)) return "Pink";
        else if (color.equals(Color.WHITE)) return "White";
        else if (color.equals(Color.BLACK)) return "Black";
        else return "";
    }
}