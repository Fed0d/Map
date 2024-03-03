package org.example.layers;

import com.bbn.openmap.event.SelectMouseMode;
import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.layer.policy.BufferedImageRenderPolicy;
import com.bbn.openmap.layer.policy.StandardPCPolicy;
import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.omGraphics.OMGraphicList;
import com.bbn.openmap.omGraphics.OMPoly;
import net.miginfocom.swing.MigLayout;
import org.example.managers.PolygonManager;
import org.example.tools.ColorComboBoxRenderer;

import javax.swing.*;
import java.awt.*;

public class PolygonsLayer extends OMGraphicHandlerLayer {
    private JPanel mainPanel = null;
    private JFrame addFrame = new JFrame();
    private PolygonManager polygonManager = new PolygonManager();

    public PolygonsLayer() {
        setProjectionChangePolicy(new StandardPCPolicy(this, true));
        setRenderPolicy(new BufferedImageRenderPolicy());
        setMouseModeIDsForEvents(new String[]{SelectMouseMode.modeID});
    }

    public OMGraphicList init() {
        OMGraphicList omList = new OMGraphicList();
        double[] latLonPoints = {0, 0, 0, 10, 5, 15, 10, 10, 10, 0, 0, 0};

        // Создаем экземпляр полигона
        OMPoly polygon = new OMPoly(latLonPoints, OMPoly.DECIMAL_DEGREES, OMPoly.LINETYPE_RHUMB, 0);
        polygon.setFillPaint(Color.RED);
        polygon.setLinePaint(Color.BLACK);
        omList.add(polygon);
        return omList;
    }

    @Override
    public synchronized OMGraphicList prepare() {
        OMGraphicList list = getList();
        if (list == null) {
            list = init();
        }
        list.generate(getProjection());
        return list;
    }

    public boolean isHighlightable(OMGraphic omg) {
        return true;
    }

    public boolean isSelectable(OMGraphic omg) {
        return true;
    }

    public Component getGUI() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new MigLayout());
            JButton addButton = new JButton("Добавить полигон");
            addButton.addActionListener(actionEvent -> {
                if (!addFrame.isVisible())
                    addPolygon();
            });
            mainPanel.add(addButton);
        }
        return mainPanel;
    }

    private void addPolygon() {
        addFrame = new JFrame("Добавление полигона");
        addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel innerPanel = new JPanel(new MigLayout());

        JLabel nameLabel = new JLabel("Введите название полигона:");
        JTextField textField = new JTextField(20);

        innerPanel.add(nameLabel);
        innerPanel.add(textField, "wrap");

        JLabel latLonLabel = new JLabel("Введите координаты полигона:");
        JTextField latLonField = new JTextField(20);
        latLonField.setEditable(false);
        latLonField.setText("[]");
        JButton latLonButton = getLatLonButton(latLonField);

        JButton clearButton = new JButton("-");
        clearButton.addActionListener(actionEvent -> latLonField.setText("[]"));

        innerPanel.add(latLonLabel);
        innerPanel.add(latLonField);
        innerPanel.add(latLonButton);
        innerPanel.add(clearButton, "wrap");

        JLabel colorLabel = new JLabel("Выберите цвет полигона:");
        JComboBox<Color> colorComboBox = new JComboBox<>(new Color[]{Color.RED, Color.GREEN, Color.BLUE
                , Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.WHITE, Color.BLACK});
        colorComboBox.setRenderer(new ColorComboBoxRenderer());

        innerPanel.add(colorLabel);
        innerPanel.add(colorComboBox, "wrap");

        JButton button = new JButton("Добавить");
        button.addActionListener(actionEvent -> {

        });

        innerPanel.add(button, "span, align center");

        addFrame.add(innerPanel);
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
        addFrame.setVisible(true);
    }

    private static JButton getLatLonButton(JTextField latLonField) {
        JButton latLonButton = new JButton("+");
        latLonButton.addActionListener(actionEvent -> {
            Double lat = showDoubleInputDialog("Введите широту:");
            if (lat != null) {
                Double lon = showDoubleInputDialog("Введите долготу:");
                if (lon != null)
                    if (latLonField.getText().equals("[]"))
                        latLonField.setText("[" + lat + ", " + lon + "]");
                    else
                        latLonField.setText(latLonField.getText().substring(0, latLonField.getText().length() - 1) + ", " + lat + ", " + lon + "]");
            }
        });
        return latLonButton;
    }

    private static Double showDoubleInputDialog(String message) {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(message);
                if (input == null)
                    return null;
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Введите корректное число формата double.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
