package org.example.layers;

import com.bbn.openmap.event.SelectMouseMode;
import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.layer.policy.BufferedImageRenderPolicy;
import com.bbn.openmap.layer.policy.StandardPCPolicy;
import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.omGraphics.OMGraphicList;
import net.miginfocom.swing.MigLayout;
import org.example.managers.PolygonManager;
import org.example.objects.Polygon;
import org.example.tools.Buttons;
import org.example.tools.ColorComboBoxRenderer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс слоя для отображения полигонов.
 */
public class PolygonsLayer extends OMGraphicHandlerLayer implements Buttons {
    /**
     * Панель для отображения кнопок.
     */
    private JPanel mainPanel = null;
    /**
     * Фрейм для добавления полигона.
     */
    private JFrame addFrame = new JFrame();
    /**
     * Фрейм для изменения полигона.
     */
    private JFrame editFrame = new JFrame();
    /**
     * Менеджер для работы с полигонами.
     */
    private final PolygonManager polygonManager = new PolygonManager();

    /**
     * Создание слоя для отображения полигонов.
     */
    public PolygonsLayer() {
        setProjectionChangePolicy(new StandardPCPolicy(this, true));
        setRenderPolicy(new BufferedImageRenderPolicy());
        setMouseModeIDsForEvents(new String[]{SelectMouseMode.modeID});
    }

    /**
     * Инициализация слоя.
     *
     * @return список полигонов
     */
    public OMGraphicList init() {
        OMGraphicList omList = new OMGraphicList();
        omList.addAll(polygonManager.getAll());
        return omList;
    }

    /**
     * Подготовка слоя.
     *
     * @return список полигонов
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

    /**
     * Показывает, можно ли выделить полигон.
     *
     * @param omg полигон
     * @return true, если можно выделить полигон, иначе - false
     */
    public boolean isHighlightable(OMGraphic omg) {
        return true;
    }

    /**
     * Показывает, можно ли выбрать полигон.
     *
     * @param omg полигон
     * @return true, если можно выбрать полигон, иначе - false
     */
    public boolean isSelectable(OMGraphic omg) {
        return true;
    }

    /**
     * Получение названия полигона.
     *
     * @param omg полигон
     * @return название полигона
     */
    @Override
    public String getToolTipTextFor(OMGraphic omg) {
        if (omg instanceof Polygon) {
            Polygon polygon = (Polygon) omg;
            return polygon.getName();
        }
        return super.getToolTipTextFor(omg);
    }

    /**
     * Получение списка компонентов для меню.
     *
     * @param omg полигон
     * @return список компонентов
     */
    @Override
    public List<Component> getItemsForOMGraphicMenu(OMGraphic omg) {
        if (omg instanceof Polygon) {
            List<Component> items = new ArrayList<>();

            Polygon polygon = (Polygon) omg;

            JMenuItem editItem = new JMenuItem("Изменить");
            editItem.addActionListener(actionEvent -> {
                if (!editFrame.isVisible()) {
                    editFrame = new JFrame("Изменение полигона");
                    editFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                    JPanel innerPanel = new JPanel(new MigLayout());

                    JLabel nameLabel = new JLabel("Название:");
                    JTextField textField = new JTextField(20);
                    textField.setText(polygon.getName());

                    innerPanel.add(nameLabel);
                    innerPanel.add(textField, "wrap");

                    JLabel latLonLabel = new JLabel("Координаты:");
                    JTextField latLonField = new JTextField(20);
                    latLonField.setText(polygon.getLatLon());

                    innerPanel.add(latLonLabel);
                    innerPanel.add(latLonField, "wrap");

                    JLabel colorLabel = new JLabel("Цвет:");
                    JComboBox<Color> colorComboBox = new JComboBox<>(new Color[]{Color.RED, Color.GREEN, Color.BLUE
                            , Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.WHITE, Color.BLACK});
                    colorComboBox.setRenderer(new ColorComboBoxRenderer());
                    colorComboBox.setSelectedItem(polygon.getFillPaint());

                    innerPanel.add(colorLabel);
                    innerPanel.add(colorComboBox, "wrap");

                    JButton button = new JButton("Изменить");
                    button.addActionListener(actionEvent1 -> {
                        if (textField.getText().isEmpty() || latLonField.getText().equals("[]")) {
                            JOptionPane.showMessageDialog(null, "Заполните все поля.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        } else {
                            polygon.setName(textField.getText());
                            polygon.setLatLon(latLonField.getText());
                            polygon.setColor(String.valueOf(((Color) colorComboBox.getSelectedItem()).getRGB()));

                            polygonManager.update(polygon);

                            doPrepare();
                            editFrame.dispose();
                        }
                    });
                    innerPanel.add(button, "span, align center");

                    editFrame.add(innerPanel);
                    editFrame.pack();
                    editFrame.setLocationRelativeTo(null);
                    editFrame.setVisible(true);
                }
            });

            JMenuItem deleteItem = new JMenuItem("Удалить");
            deleteItem.addActionListener(actionEvent -> {
                polygonManager.remove(polygon);
                getList().remove(polygon);
                doPrepare();
            });
            items.add(editItem);
            items.add(deleteItem);

            return items;
        }
        return null;
    }

    /**
     * Получение панели с кнопками.
     *
     * @return панель с кнопками
     */
    @Override
    public Component getGUI() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new MigLayout());
            JButton addButton = new JButton("Добавить полигон");
            addButton.addActionListener(actionEvent -> {
                if (!addFrame.isVisible())
                    addPolygon();
            });
            mainPanel.add(addButton, "align center, wrap");

            JButton clearButton = new JButton("Очистить");
            clearButton.addActionListener(actionEvent -> {
                polygonManager.clear(Polygon.class);
                getList().clear();

                doPrepare();
            });
            mainPanel.add(clearButton, "align center, wrap");

            JButton addFromCSVButton = getAddFromCSVButton(this);
            mainPanel.add(addFromCSVButton, "align center, wrap");

            JButton saveToCSVButton = getSaveToCSVButton();
            mainPanel.add(saveToCSVButton, "align center, wrap");
        }
        return mainPanel;
    }

    /**
     * Добавление полигона.
     */
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

        JButton button = getAddButton(textField, latLonField, colorComboBox);

        innerPanel.add(button, "span, align center");

        addFrame.add(innerPanel);
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
        addFrame.setVisible(true);
    }

    /**
     * Получение кнопки добавления полигона.
     *
     * @param textField     текстовое поле для названия
     * @param latLonField   текстовое поле для координат
     * @param colorComboBox комбобокс для цвета
     * @return кнопка добавления полигона
     */
    private JButton getAddButton(JTextField textField, JTextField latLonField, JComboBox<Color> colorComboBox) {
        JButton button = new JButton("Добавить");
        button.addActionListener(actionEvent -> {
            if (textField.getText().isEmpty() || latLonField.getText().equals("[]")) {
                JOptionPane.showMessageDialog(null, "Заполните все поля.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else {
                String name = textField.getText();
                String latLon = latLonField.getText();
                Color color = (Color) colorComboBox.getSelectedItem();
                Polygon polygon = new Polygon(name, latLon, String.valueOf(color.getRGB()));
                polygonManager.add(polygon);

                getList().add(polygon);
                doPrepare();

                addFrame.dispose();
            }

        });
        return button;
    }

    /**
     * Получение кнопки добавления координат.
     *
     * @param latLonField текстовое поле для координат
     * @return кнопка добавления координат
     */
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

    /**
     * Показывает диалоговое окно для ввода double.
     *
     * @param message сообщение
     * @return double
     */
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

    /**
     * Добавление полигонов из CSV файла.
     *
     * @param path путь к файлу
     */
    @Override
    public void addFromCSV(String path) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String str;
            List<String> features = new ArrayList<>();
            List<Polygon> polygons = new ArrayList<>();
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                str = bufferedReader.readLine();
                features.addAll(Arrays.asList(str.split(";")));

                if (features.isEmpty())
                    break;

                String name = features.get(0);
                String latLon = features.get(1);
                String color = features.get(2);

                features.clear();
                polygons.add(new Polygon(name, latLon, color));
            }
            polygonManager.addAll(polygons);
            getList().addAll(polygons);
            doPrepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Сохранение полигонов в CSV файл.
     *
     * @param path путь к файлу
     */
    @Override
    public void saveToCSV(String path) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            bufferedWriter.write("name;latLon;color\n");
            for (OMGraphic omGraphic : getList()) {
                if (omGraphic instanceof Polygon) {
                    Polygon polygon = (Polygon) omGraphic;
                    bufferedWriter.write(polygon.getName() + ";" + polygon.getLatLon() + ";" + polygon.getColor() + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
