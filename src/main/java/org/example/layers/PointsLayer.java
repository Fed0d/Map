package org.example.layers;

import com.bbn.openmap.event.SelectMouseMode;
import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.layer.policy.BufferedImageRenderPolicy;
import com.bbn.openmap.layer.policy.StandardPCPolicy;
import com.bbn.openmap.omGraphics.*;
import net.miginfocom.swing.MigLayout;
import org.example.managers.PointManager;
import org.example.objects.Point;
import org.example.tools.Buttons;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PointsLayer extends OMGraphicHandlerLayer implements Buttons {
    private JPanel mainPanel = null;
    private JFrame addFrame = new JFrame();
    private JFrame editFrame = new JFrame();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
    private int pointCounter = 0;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(PointsLayer.class);
    private final PointManager pointManager = new PointManager();

    public PointsLayer() {
        setProjectionChangePolicy(new StandardPCPolicy(this, true));
        setRenderPolicy(new BufferedImageRenderPolicy());
        setMouseModeIDsForEvents(new String[]{SelectMouseMode.modeID});
    }

    public OMGraphicList init() {
        OMGraphicList omList = new OMGraphicList();
        List<Point> points = pointManager.getAll();
        pointCounter += points.size();
        omList.addAll(points);
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

    @Override
    public String getToolTipTextFor(OMGraphic omg) {
        if (omg instanceof Point)
            return ((Point) omg).getName();
        return super.getToolTipTextFor(omg);
    }

    @Override
    public List<Component> getItemsForOMGraphicMenu(OMGraphic omg) {
        List<Component> items = new ArrayList<>();

        if (omg instanceof Point) {
            JMenuItem removeItem = new JMenuItem("Удалить");
            removeItem.addActionListener(actionEvent -> {
                pointManager.remove((Point) omg);
                getList().remove(omg);
                pointCounter--;
                pcs.firePropertyChange("pointCounter", pointCounter + 1, pointCounter);
                doPrepare();
            });

            JMenuItem editItem = new JMenuItem("Изменить");
            editItem.addActionListener(actionEvent -> {
                if (!editFrame.isVisible()) {
                    editFrame = new JFrame("Изменение точки");
                    editFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    JPanel editPanel = new JPanel(new MigLayout());

                    JLabel nameLabel = new JLabel("Название:");
                    editPanel.add(nameLabel);
                    JFormattedTextField nameField = new JFormattedTextField(((Point) omg).getName());
                    nameField.setColumns(10);
                    editPanel.add(nameField, "wrap");

                    JLabel coordinatesLabel = new JLabel("Координаты:");
                    JLabel latLabel = new JLabel("Lat:");
                    JFormattedTextField latField = new JFormattedTextField(decimalFormat);
                    latField.setValue(((Point) omg).getLat());
                    latField.setColumns(5);
                    JLabel lonLabel = new JLabel("Lon:");
                    JFormattedTextField lonField = new JFormattedTextField(decimalFormat);
                    lonField.setValue(((Point) omg).getLon());
                    lonField.setColumns(5);

                    editPanel.add(coordinatesLabel);
                    editPanel.add(latLabel, "split 4");
                    editPanel.add(latField);
                    editPanel.add(lonLabel);
                    editPanel.add(lonField, "wrap");

                    JLabel courseLabel = new JLabel("Курс:");
                    JFormattedTextField courseField = new JFormattedTextField(decimalFormat);
                    courseField.setValue(((Point) omg).getRotationAngle());
                    courseField.setColumns(5);

                    editPanel.add(courseLabel);
                    editPanel.add(courseField, "wrap");

                    JButton editButton = new JButton("Изменить");
                    editButton.addActionListener(actionEvent1 -> {
                        ((Point) omg).setName(nameField.getText());
                        ((Point) omg).setLatitude(Double.parseDouble(latField.getText()));
                        ((Point) omg).setLongitude(Double.parseDouble(lonField.getText()));
                        ((Point) omg).setCourse(Double.parseDouble(courseField.getText()));
                        pointManager.update((Point) omg);
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

    public Component getGUI() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new MigLayout());

            JLabel pointsNumberLabel = new JLabel("Количество точек:");
            JLabel pointsCounterLabel = new JLabel(String.valueOf(pointCounter));
            pcs.addPropertyChangeListener("pointCounter", evt
                    -> pointsCounterLabel.setText(String.valueOf(pointCounter)));
            mainPanel.add(pointsNumberLabel);
            mainPanel.add(pointsCounterLabel, "wrap");

            JButton addButton = getAddButton();
            mainPanel.add(addButton, "wrap, span, align center");

            JButton addPointsFromCSVButton = getAddFromCSVButton(this);

            mainPanel.add(addPointsFromCSVButton, "wrap, span, align center");

            JButton savePointsToCSVButton = getSaveToCSVButton();

            mainPanel.add(savePointsToCSVButton, "wrap, span, align center");
        }
        return mainPanel;
    }

    private JButton getAddButton() {
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
                JLabel latLabel = new JLabel("Lat:");
                JFormattedTextField latField = new JFormattedTextField(decimalFormat);
                latField.setColumns(5);
                JLabel lonLabel = new JLabel("Lon:");
                JFormattedTextField lonField = new JFormattedTextField(decimalFormat);
                lonField.setColumns(5);

                addPanel.add(latLabel, "split 4");
                addPanel.add(latField);
                addPanel.add(lonLabel);
                addPanel.add(lonField, "wrap");

                JLabel courseLabel = new JLabel("Введите курс:");
                JFormattedTextField courseField = new JFormattedTextField(decimalFormat);
                courseField.setColumns(5);

                addPanel.add(courseLabel);
                addPanel.add(courseField, "wrap");
                JButton addPointButton = new JButton("Добавить");

                addPointButton.addActionListener(actionEvent1 -> {
                    String name = nameField.getText();
                    double latitude = Double.parseDouble(latField.getText());
                    double longitude = Double.parseDouble(lonField.getText());
                    double course = Double.parseDouble(courseField.getText());
                    Point raster = new Point(name, latitude, longitude, course);

                    pointManager.add(raster);

                    getList().add(raster);
                    doPrepare();
                    pointCounter++;
                    pcs.firePropertyChange("pointCounter", pointCounter - 1, pointCounter);
                    addFrame.dispose();
                });

                addPanel.add(addPointButton, "wrap, span, align center");

                addFrame.add(addPanel);
                addFrame.pack();
                addFrame.setResizable(false);
                addFrame.setVisible(true);
            }
        });
        return addButton;
    }

    @Override
    public void addFromCSV(String path) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String str;
            List<String> features = new ArrayList<>();
            List<Point> points = new ArrayList<>();
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                str = bufferedReader.readLine();
                features.addAll(Arrays.asList(str.split(";")));

                if (features.isEmpty())
                    break;

                String name = features.get(0);
                double latitude = Double.parseDouble(features.get(1));
                double longitude = Double.parseDouble(features.get(2));
                double course = Double.parseDouble(features.get(3));

                features.clear();
                points.add(new Point(name, latitude, longitude, course));
                pointCounter++;
            }
            pointManager.addAll(points);
            getList().addAll(points);
            doPrepare();
            pcs.firePropertyChange("pointCounter", pointCounter - points.size(), pointCounter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveToCSV(String path) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            bufferedWriter.write("name;latitude;longitude;course\n");
            for (OMGraphic omGraphic : getList())
                if (omGraphic instanceof Point) {
                    Point point = (Point) omGraphic;
                    bufferedWriter.write(point.getName() + ";" + point.getLat() + ";" + point.getLon() + ";"
                            + point.getRotationAngle() + "\n");
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}