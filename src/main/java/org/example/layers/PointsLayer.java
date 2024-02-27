package org.example.layers;

import com.bbn.openmap.event.SelectMouseMode;
import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.layer.policy.BufferedImageRenderPolicy;
import com.bbn.openmap.layer.policy.StandardPCPolicy;
import com.bbn.openmap.omGraphics.*;
import net.miginfocom.swing.MigLayout;
import org.example.objects.Point;
import org.example.objects.Polygon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.List;

public class PointsLayer extends OMGraphicHandlerLayer {
    private JPanel mainPanel = null;
    private JFrame addFrame = new JFrame();
    private JFrame editFrame = new JFrame();

    public PointsLayer() {
        setName("Points Layer");
        setProjectionChangePolicy(new StandardPCPolicy(this, true));
        setMouseModeIDsForEvents(new String[]{SelectMouseMode.modeID});
    }

    public boolean isHighlightable(OMGraphic omg) {
        return true;
    }

    public boolean isSelectable(OMGraphic omg) {
        return true;
    }

    @Override
    public String getToolTipTextFor(OMGraphic omg) {
        if (omg instanceof Point) {
            return ((Point) omg).getName();
        }
        return super.getToolTipTextFor(omg);
    }

    @Override
    public List<Component> getItemsForOMGraphicMenu(OMGraphic omg) {
        List<Component> items = new ArrayList<>();
        if (omg instanceof Point) {
            JMenuItem removeItem = new JMenuItem("Удалить");
            removeItem.addActionListener(actionEvent -> {
                if (getList() != null) {
                    getList().remove(omg);
                    doPrepare();
                }
            });

            JMenuItem editItem = new JMenuItem("Изменить");
            editItem.addActionListener(actionEvent -> {
                if (!editFrame.isVisible()) {
                    editFrame = new JFrame("Изменение точки");
                    editFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    JPanel editPanel = new JPanel(new MigLayout());

                    JLabel nameLabel = new JLabel("Название:");
                    editPanel.add(nameLabel);
                    JTextField nameField = new JTextField(((Point) omg).getName());
                    editPanel.add(nameField, "wrap");

                    JLabel coordinatesLabel = new JLabel("Координаты:");
                    JLabel xLabel = new JLabel("X");
                    JTextField xField = new JTextField(String.valueOf(((Point) omg).getLat()));
                    JLabel yLabel = new JLabel("Y");
                    JTextField yField = new JTextField(String.valueOf(((Point) omg).getLon()));

                    editPanel.add(coordinatesLabel);
                    editPanel.add(xLabel, "split 4");
                    editPanel.add(xField);
                    editPanel.add(yLabel);
                    editPanel.add(yField, "wrap");

                    JLabel courseLabel = new JLabel("Курс:");
                    JTextField courseField = new JTextField(String.valueOf(((Point) omg).getRotationAngle()));

                    editPanel.add(courseLabel);
                    editPanel.add(courseField, "wrap");

                    JButton editButton = new JButton("Изменить");
                    editButton.addActionListener(actionEvent1 -> {
                        ((Point) omg).setName(nameField.getText());
                        ((Point) omg).setLat(Double.parseDouble(xField.getText()));
                        ((Point) omg).setLon(Double.parseDouble(yField.getText()));
                        ((Point) omg).setRotationAngle(Double.parseDouble(courseField.getText()));
                        doPrepare();
                        editFrame.dispose();
                    });

                    editPanel.add(editButton, "wrap, span, align center");

                    editFrame.add(editPanel);
                    editFrame.pack();
                    editFrame.setResizable(false);
                    editFrame.setVisible(true);
                }
            });

            items.add(editItem);
            items.add(removeItem);
        }
        return items;
    }

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

            JButton addButton = new JButton("Добавить точку");
            addButton.addActionListener(actionEvent -> {
                if (!addFrame.isVisible()) {
                    addFrame = new JFrame(("Добавление точки"));
                    addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                    JPanel addPanel = new JPanel(new MigLayout());
                    JTextField nameField = new JTextField(10);
                    JLabel nameLabel = new JLabel("Введите название точки:");

                    addPanel.add(nameLabel);
                    addPanel.add(nameField, "wrap");

                    JLabel coordinatesLabel = new JLabel("Введите координаты:");
                    addPanel.add(coordinatesLabel);
                    JLabel xLabel = new JLabel("X");
                    JTextField xField = new JTextField(5);
                    JLabel yLabel = new JLabel("Y");
                    JTextField yField = new JTextField(5);

                    addPanel.add(xLabel, "split 4");
                    addPanel.add(xField);
                    addPanel.add(yLabel);
                    addPanel.add(yField, "wrap");

                    JLabel courseLabel = new JLabel("Введите курс:");
                    JTextField courseField = new JTextField(5);

                    addPanel.add(courseLabel);
                    addPanel.add(courseField, "wrap");
                    JButton addPointButton = new JButton("Добавить");

                    addPointButton.addActionListener(actionEvent1 -> {
                        String name = nameField.getText();
                        double x = Double.parseDouble(xField.getText());
                        double y = Double.parseDouble(yField.getText());
                        double course = Double.parseDouble(courseField.getText());
                        Point raster = new Point(name, y, x, course);

                        if (getList() == null)
                            setList(new OMGraphicList());

                        getList().add(raster);
                        doPrepare();
                        addFrame.dispose();
                    });

                    addPanel.add(addPointButton, "wrap, span, align center");

                    addFrame.add(addPanel);
                    addFrame.pack();
                    addFrame.setResizable(false);
                    addFrame.setVisible(true);
                }
            });
            mainPanel.add(addButton, "wrap, span, align center");
        }
        return mainPanel;
    }

}
