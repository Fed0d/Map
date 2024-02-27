package org.example.layers;

import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.omGraphics.*;
import com.bbn.openmap.proj.coords.LatLonPoint;
import javafx.scene.shape.Ellipse;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class SectorsLayer extends OMGraphicHandlerLayer {
    private JPanel mainPanel = null;
    private JFrame addFrame = new JFrame();

    @Override
    public synchronized OMGraphicList prepare() {
        OMGraphicList list = getList();
        if (list == null) {
            list = new OMGraphicList();
            setList(list);
        }
        list.generate(getProjection());
        return list;
    }

    public Component getGUI() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new MigLayout());
            JButton addButton = new JButton("Добавить сектор");
            addButton.addActionListener(actionEvent -> {
                if (!addFrame.isVisible()) {
                    addFrame = new JFrame(("Добавление сектора"));
                    addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    JPanel addPanel = new JPanel(new MigLayout());
                    JTextField nameField = new JTextField(20);
                    JLabel nameLabel = new JLabel("Введите название сектора");
                    addPanel.add(nameLabel);
                    addPanel.add(nameField, "wrap");
                    JLabel coordinatesLabel = new JLabel("Введите координаты центра");
                    addPanel.add(coordinatesLabel);
                    JLabel xLabel = new JLabel("X");
                    JTextField xField = new JTextField(5);
                    JLabel yLabel = new JLabel("Y");
                    JTextField yField = new JTextField(5);
                    addPanel.add(xLabel, "split 4");
                    addPanel.add(xField);
                    addPanel.add(yLabel);
                    addPanel.add(yField, "wrap");
                    JLabel radiusLabel = new JLabel("Введите X радиус");
                    JTextField radiusXField = new JTextField(5);
                    JLabel radiusYLabel = new JLabel("Введите Y радиус");
                    JTextField radiusYField = new JTextField(5);
                    addPanel.add(radiusLabel);
                    addPanel.add(radiusXField, "wrap");
                    addPanel.add(radiusYLabel);
                    addPanel.add(radiusYField, "wrap");
                    JButton addSectorButton = new JButton("Добавить");
                    addSectorButton.addActionListener(actionEvent1 -> {
                        // Добавить реализацию сектора
                        String name = nameField.getText();
                        double x = Double.parseDouble(xField.getText());
                        double y = Double.parseDouble(yField.getText());
                        LatLonPoint latLonPoint = new LatLonPoint.Double(y, x);
                        double radiusX = Double.parseDouble(radiusXField.getText());
                        double radiusY = Double.parseDouble(radiusYField.getText());
                        if (getList() == null)
                            setList(new OMGraphicList());

                    });
                    addPanel.add(addSectorButton, "span, align center");
                    addFrame.add(addPanel);
                    addFrame.pack();
                    addFrame.setResizable(false);
                    addFrame.setVisible(true);
                }
            });
            mainPanel.add(addButton, "span, align center");
        }
        return mainPanel;
    }
}
