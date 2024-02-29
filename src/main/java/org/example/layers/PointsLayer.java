package org.example.layers;

import com.bbn.openmap.event.SelectMouseMode;
import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.layer.policy.BufferedImageRenderPolicy;
import com.bbn.openmap.layer.policy.StandardPCPolicy;
import com.bbn.openmap.omGraphics.*;
import net.miginfocom.swing.MigLayout;
import org.example.managers.PointManager;
import org.example.objects.Point;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A layer for displaying points on the map.
 */
public class PointsLayer extends OMGraphicHandlerLayer {
    /**
     * The main panel.
     */
    private JPanel mainPanel = null;
    /**
     * The add frame.
     */
    private JFrame addFrame = new JFrame();
    /**
     * The edit frame.
     */
    private JFrame editFrame = new JFrame();
    /**
     * The decimal format.
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
    /**
     * The point counter.
     */
    private int pointCounter = 0;
    /**
     * The property change support.
     */
    private static final PropertyChangeSupport pcs = new PropertyChangeSupport(PointsLayer.class);

    /**
     * Create a new PointsLayer.
     */
    public PointsLayer() {
        setProjectionChangePolicy(new StandardPCPolicy(this, true));
        setRenderPolicy(new BufferedImageRenderPolicy());
        setMouseModeIDsForEvents(new String[]{SelectMouseMode.modeID});
    }

    /**
     * Get the initial list of points.
     *
     * @return the initial list of points
     */
    public OMGraphicList init() {
        OMGraphicList omList = new OMGraphicList();
        List<Point> points = PointManager.getAllPoints();
        pointCounter += points.size();
        omList.addAll(points);
        return omList;
    }

    /**
     * Prepare the layer.
     *
     * @return the OMGraphicList
     */
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

    /**
     * Get the tooltip text for the given OMGraphic.
     *
     * @param omg the OMGraphic to get the tooltip text for.
     * @return the tooltip text for the given OMGraphic.
     */
    @Override
    public String getToolTipTextFor(OMGraphic omg) {
        if (omg instanceof Point) {
            return ((Point) omg).getName();
        }
        return super.getToolTipTextFor(omg);
    }

    /**
     * Get the items for the OMGraphicMenu. This method is called when the user
     * right-clicks on an OMGraphic. The List of Components returned is added to
     * the OMGraphicMenu, and the OMGraphicMenu is displayed at the mouse
     * location.
     *
     * @param omg the OMGraphic that was right-clicked on.
     * @return a List of Components to add to the OMGraphicMenu.
     */
    @Override
    public List<Component> getItemsForOMGraphicMenu(OMGraphic omg) {
        List<Component> items = new ArrayList<>();
        if (omg instanceof Point) {
            JMenuItem removeItem = new JMenuItem("Удалить");
            removeItem.addActionListener(actionEvent -> {
                if (getList() != null) {
                    PointManager.removePoint((Point) omg);
                    getList().remove(omg);
                    pointCounter--;
                    pcs.firePropertyChange("pointCounter", pointCounter + 1, pointCounter);
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
                        PointManager.updatePoint((Point) omg);
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

    /**
     * Get the GUI for this layer.
     *
     * @return the GUI for this layer.
     */
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

            JButton addPointsFromCSVButton = getAddPointsFromCSVButton();

            mainPanel.add(addPointsFromCSVButton, "wrap, span, align center");

            JButton savePointsToCSVButton = getSavePointsToCSVButton();

            mainPanel.add(savePointsToCSVButton, "wrap, span, align center");
        }
        return mainPanel;
    }

    /**
     * Get the save points to CSV button.
     *
     * @return the save points to CSV button
     */
    private JButton getSavePointsToCSVButton() {
        JButton savePointsToCSVButton = new JButton("Сохранить точки в CSV");
        savePointsToCSVButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int ret = fileChooser.showDialog(null, "Сохранить файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                savePointsToCSV(file.getAbsolutePath());
            }
        });
        return savePointsToCSVButton;
    }

    /**
     * Get the add points from CSV button.
     *
     * @return the add points from CSV button
     */
    private JButton getAddPointsFromCSVButton() {
        JButton addPointsFromCSVButton = new JButton("Добавить точки из CSV");
        addPointsFromCSVButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int ret = fileChooser.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                addPointsFromCSV(file.getAbsolutePath());
                doPrepare();
            }
        });
        return addPointsFromCSVButton;
    }

    /**
     * Get the add button.
     *
     * @return the add button
     */
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

                    PointManager.addPoint(raster);

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

    /**
     * Add points from the given CSV file.
     *
     * @param path the path to the CSV file
     */
    private void addPointsFromCSV(String path) {
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
            PointManager.addAllPoints(points);
            getList().addAll(points);
            pcs.firePropertyChange("pointCounter", pointCounter - points.size(), pointCounter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Save points to the given CSV file.
     *
     * @param path the path to the CSV file
     */
    private void savePointsToCSV(String path) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            bufferedWriter.write("Название;Широта;Долгота;Курс\n");
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