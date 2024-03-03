package org.example.tools;

import com.bbn.openmap.layer.OMGraphicHandlerLayer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Интерфейс для создания кнопок
 */
public interface Buttons {
    /**
     * Создание кнопки "Добавить из CSV"
     *
     * @param omGraphicHandlerLayer слой для добавления объектов
     * @return кнопка "Добавить из CSV"
     */
    default JButton getAddFromCSVButton(OMGraphicHandlerLayer omGraphicHandlerLayer) {
        JButton addFromCSVButton = new JButton("Добавить из CSV");
        addFromCSVButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int ret = fileChooser.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                addFromCSV(file.getAbsolutePath());
                omGraphicHandlerLayer.doPrepare();
            }
        });
        return addFromCSVButton;
    }

    /**
     * Добавление объектов из CSV
     *
     * @param path путь к файлу
     */
    void addFromCSV(String path);

    /**
     * Создание кнопки "Сохранить в CSV"
     *
     * @return кнопка "Сохранить в CSV"
     */
    default JButton getSaveToCSVButton() {
        JButton saveToCSVButton = new JButton("Сохранить в CSV");
        saveToCSVButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int ret = fileChooser.showDialog(null, "Сохранить файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveToCSV(file.getAbsolutePath());
            }
        });
        return saveToCSVButton;
    }

    /**
     * Сохранение объектов в CSV
     *
     * @param path путь к файлу
     */
    void saveToCSV(String path);
}
