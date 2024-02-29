package org.example.layers;

import com.bbn.openmap.layer.OMGraphicHandlerLayer;

import javax.swing.*;
import java.awt.*;

/**
 * The Polygons layer.
 */
public class PolygonsLayer extends OMGraphicHandlerLayer {
    /**
     * The Panel.
     */
    private JPanel panel = null;
    /**
     * The Add frame.
     */
    private JFrame addFrame = new JFrame();

    /**
     * Instantiates a new Polygons layer.
     */
    public Component getGUI() {
        if (panel == null) {
            GridBagLayout gridBag = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            panel = new JPanel(gridBag);
            JButton addButton = new JButton("Добавить полигон");
            addButton.addActionListener(actionEvent -> {
                if (!addFrame.isVisible())
                    addPolygon();
            });
            gridBag.setConstraints(addButton, c);
            panel.add(addButton);
        }
        return panel;
    }

    private void addPolygon() {
        addFrame = new JFrame("Добавление полигона");
        addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addFrame.setSize(400, 200);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("Введите название полигона");
        JTextField textField = new JTextField(20);

        JButton button = new JButton("Добавить");
        button.addActionListener(actionEvent -> {
            String name = textField.getText();
            if (name != null && !name.isEmpty()) {
                System.out.println("Добавлен полигон " + name);
            }
        });

        innerPanel.add(label);
        innerPanel.add(textField);

        JButton plusButton = getPlusButton(innerPanel);

        innerPanel.add(plusButton);
        innerPanel.add(button);

        addFrame.getContentPane().add(innerPanel);

        addFrame.setLocationRelativeTo(null);
        addFrame.setVisible(true);
    }


    private static JButton getPlusButton(JPanel innerPanel) {
        JButton plusButton = new JButton("+");
        plusButton.addActionListener(actionEvent -> {
            JPanel coordinatePanel = new JPanel();
            coordinatePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            JTextField fieldX = new JTextField(5);
            JTextField fieldY = new JTextField(5);
            JButton removeButton = new JButton("-");
            removeButton.addActionListener(actionEvent1 -> {
                innerPanel.remove(coordinatePanel);
                innerPanel.revalidate();
                innerPanel.repaint();
            });

            coordinatePanel.add(fieldX);
            coordinatePanel.add(fieldY);
            coordinatePanel.add(removeButton);

            // Вставляем перед кнопкой "+"
            int index = innerPanel.getComponentCount() - 2;
            innerPanel.add(coordinatePanel, index);
            innerPanel.revalidate();
            innerPanel.repaint();
        });
        return plusButton;
    }
}
