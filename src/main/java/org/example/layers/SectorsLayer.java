package org.example.layers;

import com.bbn.openmap.event.SelectMouseMode;
import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.layer.policy.BufferedImageRenderPolicy;
import com.bbn.openmap.layer.policy.StandardPCPolicy;
import com.bbn.openmap.omGraphics.*;
import net.miginfocom.swing.MigLayout;
import org.example.managers.SectorManager;
import org.example.objects.Sector;

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

public class SectorsLayer extends OMGraphicHandlerLayer {
    private JPanel mainPanel = null;
    private JFrame addFrame = new JFrame();
    private JFrame editFrame = new JFrame();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
    private final DecimalFormat integerFormat = new DecimalFormat("#");
    private int sectorCounter = 0;
    private static final PropertyChangeSupport pcs = new PropertyChangeSupport(SectorsLayer.class);

    public SectorsLayer() {
        setProjectionChangePolicy(new StandardPCPolicy(this, true));
        setRenderPolicy(new BufferedImageRenderPolicy());
        setMouseModeIDsForEvents(new String[]{SelectMouseMode.modeID});
    }

    public OMGraphicList init() {
        OMGraphicList omList = new OMGraphicList();
        List<Sector> sectors = SectorManager.getAllSectors();
        sectorCounter += sectors.size();
        omList.addAll(sectors);
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

    @Override
    public boolean isSelectable(OMGraphic omg) {
        return true;
    }

    @Override
    public boolean isHighlightable(OMGraphic omg) {
        return true;
    }

    @Override
    public String getToolTipTextFor(OMGraphic omg) {
        if (omg instanceof Sector)
            return ((Sector) omg).getName();
        return super.getToolTipTextFor(omg);
    }

    @Override
    public List<Component> getItemsForOMGraphicMenu(OMGraphic omg) {
        List<Component> items = new ArrayList<>();

        if (omg instanceof Sector) {
            Sector sector = (Sector) omg;
            JMenuItem removeItem = new JMenuItem("Удалить");
            removeItem.addActionListener(actionEvent -> {
                SectorManager.removeSector(sector);
                getList().remove(sector);
                doPrepare();
            });

            JMenuItem editItem = new JMenuItem("Редактировать");
            editItem.addActionListener(actionEvent -> {
                if (!editFrame.isVisible()) {
                    editFrame = new JFrame(("Редактирование сектора"));
                    editFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                    JPanel editPanel = new JPanel(new MigLayout());

                    JTextField nameField = new JTextField(sector.getName(), 10);
                    JLabel nameLabel = new JLabel("Введите название сектора:");

                    editPanel.add(nameLabel);
                    editPanel.add(nameField, "wrap");

                    JLabel coordinatesLabel = new JLabel("Введите координаты центра:");
                    editPanel.add(coordinatesLabel);

                    JLabel xLabel = new JLabel("Lat:");
                    JFormattedTextField latField = new JFormattedTextField(decimalFormat);
                    latField.setColumns(5);
                    latField.setValue(sector.getLatitudeCenter());

                    JLabel yLabel = new JLabel("Lon:");
                    JFormattedTextField lonField = new JFormattedTextField(decimalFormat);
                    lonField.setColumns(5);
                    lonField.setValue(sector.getLongitudeCenter());

                    editPanel.add(xLabel, "split 4");
                    editPanel.add(latField);

                    editPanel.add(yLabel);
                    editPanel.add(lonField, "wrap");

                    JLabel radiusLabel = new JLabel("Введите X радиус:");
                    JFormattedTextField radiusXField = new JFormattedTextField(integerFormat);
                    radiusXField.setColumns(5);
                    radiusXField.setValue(sector.getxRadius());

                    JLabel radiusYLabel = new JLabel("Введите Y радиус:");
                    JFormattedTextField radiusYField = new JFormattedTextField(integerFormat);
                    radiusYField.setColumns(5);
                    radiusYField.setValue(sector.getyRadius());

                    editPanel.add(radiusLabel);
                    editPanel.add(radiusXField, "wrap");

                    editPanel.add(radiusYLabel);
                    editPanel.add(radiusYField, "wrap");

                    JLabel startAngleLabel = new JLabel("Введите начальный угол:");
                    JFormattedTextField startAngleField = new JFormattedTextField(decimalFormat);
                    startAngleField.setColumns(5);
                    startAngleField.setValue(sector.getStartAngle());

                    JLabel extentAngleLabel = new JLabel("Введите угол дуги:");
                    JFormattedTextField extentAngleField = new JFormattedTextField(decimalFormat);
                    extentAngleField.setColumns(5);
                    extentAngleField.setValue(sector.getExtentAngle());

                    editPanel.add(startAngleLabel);
                    editPanel.add(startAngleField, "wrap");

                    editPanel.add(extentAngleLabel);
                    editPanel.add(extentAngleField, "wrap");

                    JLabel courseLabel = new JLabel("Введите курс:");
                    JFormattedTextField courseField = new JFormattedTextField(decimalFormat);
                    courseField.setColumns(5);
                    courseField.setValue(sector.getCourse());

                    editPanel.add(courseLabel);
                    editPanel.add(courseField, "wrap");

                    JLabel colorLabel = new JLabel("Выберите цвет:");
                    JComboBox<Color> colorComboBox = new JComboBox<>(new Color[]{Color.RED, Color.GREEN, Color.BLUE
                            , Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.WHITE, Color.BLACK});
                    colorComboBox.setRenderer(new ColorComboBoxRenderer());
                    colorComboBox.setSelectedItem(new Color(Integer.parseInt(sector.getColor())));

                    editPanel.add(colorLabel);
                    editPanel.add(colorComboBox, "wrap");

                    JButton editSectorButton = new JButton("Изменить");
                    editSectorButton.addActionListener(actionEvent1 -> {
                        String name = nameField.getText();
                        double lat = Double.parseDouble(latField.getText());
                        double lon = Double.parseDouble(lonField.getText());
                        int radiusX = Integer.parseInt(radiusXField.getText());
                        int radiusY = Integer.parseInt(radiusYField.getText());
                        double startAngle = Double.parseDouble(startAngleField.getText());
                        double extentAngle = Double.parseDouble(extentAngleField.getText());
                        double course = Double.parseDouble(courseField.getText());
                        Color color = (Color) colorComboBox.getSelectedItem();

                        sector.setName(name);
                        sector.setLatLonCenter(lat, lon);
                        sector.setxRadius(radiusX);
                        sector.setyRadius(radiusY);
                        sector.setStartAngle(startAngle);
                        sector.setExtentAngle(extentAngle);
                        sector.setCourse(course);
                        sector.setColor(String.valueOf(color.getRGB()));

                        SectorManager.updateSector(sector);

                        doPrepare();
                        addFrame.dispose();
                    });
                    editPanel.add(editSectorButton, "span, align center");

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

            JButton addButton = new JButton("Добавить сектор");
            addButton.addActionListener(actionEvent -> {
                if (!addFrame.isVisible()) {
                    addFrame = new JFrame(("Добавление сектора"));
                    addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                    JPanel addPanel = new JPanel(new MigLayout());

                    JTextField nameField = new JTextField(10);
                    JLabel nameLabel = new JLabel("Введите название сектора:");

                    addPanel.add(nameLabel);
                    addPanel.add(nameField, "wrap");

                    JLabel coordinatesLabel = new JLabel("Введите координаты центра:");
                    addPanel.add(coordinatesLabel);

                    JLabel xLabel = new JLabel("Lat:");
                    JFormattedTextField latField = new JFormattedTextField(decimalFormat);
                    latField.setColumns(5);

                    JLabel yLabel = new JLabel("Lon:");
                    JFormattedTextField lonField = new JFormattedTextField(decimalFormat);
                    lonField.setColumns(5);

                    addPanel.add(xLabel, "split 4");
                    addPanel.add(latField);

                    addPanel.add(yLabel);
                    addPanel.add(lonField, "wrap");

                    JLabel radiusLabel = new JLabel("Введите X радиус:");
                    JFormattedTextField radiusXField = new JFormattedTextField(integerFormat);
                    radiusXField.setColumns(5);

                    JLabel radiusYLabel = new JLabel("Введите Y радиус:");
                    JFormattedTextField radiusYField = new JFormattedTextField(integerFormat);
                    radiusYField.setColumns(5);

                    addPanel.add(radiusLabel);
                    addPanel.add(radiusXField, "wrap");

                    addPanel.add(radiusYLabel);
                    addPanel.add(radiusYField, "wrap");

                    JLabel startAngleLabel = new JLabel("Введите начальный угол:");
                    JFormattedTextField startAngleField = new JFormattedTextField(decimalFormat);
                    startAngleField.setColumns(5);

                    JLabel extentAngleLabel = new JLabel("Введите угол дуги:");
                    JFormattedTextField extentAngleField = new JFormattedTextField(decimalFormat);
                    extentAngleField.setColumns(5);

                    addPanel.add(startAngleLabel);
                    addPanel.add(startAngleField, "wrap");

                    addPanel.add(extentAngleLabel);
                    addPanel.add(extentAngleField, "wrap");

                    JLabel courseLabel = new JLabel("Введите курс:");
                    JFormattedTextField courseField = new JFormattedTextField(decimalFormat);
                    courseField.setColumns(5);

                    addPanel.add(courseLabel);
                    addPanel.add(courseField, "wrap");

                    JLabel colorLabel = new JLabel("Выберите цвет:");

                    JComboBox<Color> colorComboBox = new JComboBox<>(new Color[]{Color.RED, Color.GREEN, Color.BLUE
                            , Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.WHITE, Color.BLACK});
                    colorComboBox.setRenderer(new ColorComboBoxRenderer());

                    addPanel.add(colorLabel);
                    addPanel.add(colorComboBox, "wrap");

                    JButton addSectorButton = new JButton("Добавить");
                    addSectorButton.addActionListener(actionEvent1 -> {
                        String name = nameField.getText();
                        double lat = Double.parseDouble(latField.getText());
                        double lon = Double.parseDouble(lonField.getText());
                        int radiusX = Integer.parseInt(radiusXField.getText());
                        int radiusY = Integer.parseInt(radiusYField.getText());
                        double startAngle = Double.parseDouble(startAngleField.getText());
                        double extentAngle = Double.parseDouble(extentAngleField.getText());
                        double course = Double.parseDouble(courseField.getText());
                        Color color = (Color) colorComboBox.getSelectedItem();

                        Sector sector = new Sector(name, lat, lon, radiusX, radiusY, startAngle, extentAngle, course
                                , String.valueOf(color.getRGB()));

                        SectorManager.addSector(sector);

                        getList().add(sector);
                        doPrepare();
                        addFrame.dispose();
                    });
                    addPanel.add(addSectorButton, "span, align center");
                    addFrame.add(addPanel);
                    addFrame.pack();
                    addFrame.setResizable(false);
                    addFrame.setVisible(true);
                }
            });
            mainPanel.add(addButton, "span, align center");

            JButton removeButton = new JButton("Удалить все сектора");
            removeButton.addActionListener(actionEvent -> {
                SectorManager.clear();
                getList().clear();
                doPrepare();
            });
            mainPanel.add(removeButton, "span, align center");

            JButton saveSectorsToCSVButton = new JButton("Сохранить сектора в CSV");
            saveSectorsToCSVButton.addActionListener(actionEvent -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
                fileChooser.setAcceptAllFileFilterUsed(false);
                int ret = fileChooser.showDialog(null, "Сохранить файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    saveSectorsToCSV(file.getAbsolutePath());
                }
            });

            mainPanel.add(saveSectorsToCSVButton, "span, align center");

            JButton addSectorsFromCSVButton = new JButton("Добавить сектора из CSV");
            addSectorsFromCSVButton.addActionListener(actionEvent -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
                fileChooser.setAcceptAllFileFilterUsed(false);
                int ret = fileChooser.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    addPointsFromCSV(file.getAbsolutePath());
                }
            });

            mainPanel.add(addSectorsFromCSVButton, "span, align center");
        }
        return mainPanel;
    }

    private static class ColorComboBoxRenderer extends DefaultListCellRenderer {
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

        private Color getContrastColor(Color color) {
            int luminance = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
            return luminance > 128 ? Color.BLACK : Color.WHITE;
        }

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

    private void addPointsFromCSV(String path) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String str;
            List<String> features = new ArrayList<>();
            List<Sector> sectors = new ArrayList<>();
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                str = bufferedReader.readLine();
                features.addAll(Arrays.asList(str.split(";")));

                if (features.isEmpty())
                    break;

                Sector sector = getSectorsFromCSV(features);

                features.clear();
                sectors.add(sector);
                sectorCounter++;
            }
            SectorManager.addAllSectors(sectors);
            getList().addAll(sectors);
            doPrepare();
            pcs.firePropertyChange("sectorCounter", sectorCounter - sectors.size(), sectorCounter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Sector getSectorsFromCSV(List<String> features) {
        String name = features.get(0);
        double latitudeCenter = Double.parseDouble(features.get(1));
        double longitudeCenter = Double.parseDouble(features.get(2));
        int xRadius = Integer.parseInt(features.get(3));
        int yRadius = Integer.parseInt(features.get(4));
        double startAngle = Double.parseDouble(features.get(5));
        double extentAngle = Double.parseDouble(features.get(6));
        double course = Double.parseDouble(features.get(7));
        String color = features.get(8);

        Sector sector = new Sector(name, latitudeCenter, longitudeCenter, xRadius, yRadius, startAngle, extentAngle
                , course, color);
        return sector;
    }

    private void saveSectorsToCSV(String path) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            bufferedWriter.write("name;latitudeCenter;longitudeCenter;xRadius;yRadius;startAngle;extentAngle;course;color\n");
            for (OMGraphic omGraphic : getList())
                if (omGraphic instanceof Sector) {
                    Sector sector = (Sector) omGraphic;
                    bufferedWriter.write(sector.getName() + ";" + sector.getLatitudeCenter() + ";" + sector.getLongitudeCenter()
                            + ";" + sector.getxRadius() + ";" + sector.getyRadius() + ";" + sector.getStartAngle()
                            + ";" + sector.getExtentAngle() + ";" + sector.getCourse() + ";" + sector.getColor() + "\n");
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
