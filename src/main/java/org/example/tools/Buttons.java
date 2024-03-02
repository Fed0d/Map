package org.example.tools;

import com.bbn.openmap.layer.OMGraphicHandlerLayer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public interface Buttons {
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

    void addFromCSV(String path);

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

    void saveToCSV(String path);
}
