package org.example.startApp;

import com.bbn.openmap.MapHandler;
import com.bbn.openmap.PropertyHandler;
import com.bbn.openmap.gui.MapPanel;
import com.bbn.openmap.gui.OpenMapFrame;
import com.bbn.openmap.gui.OverlayMapPanel;
import org.example.objects.Point;
import org.example.objects.Polygon;
import org.example.objects.Sector;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.io.IOException;

/**
 * Класс для отображения карты.
 */
public class OpenMap {
    /**
     * Панель карты.
     */
    private MapPanel mapPanel;
    /**
     * Фабрика сессий.
     */
    private static SessionFactory sessionFactory;

    /**
     * Получение фабрики сессий.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Точка входа в программу.
     */
    public static void main(String[] args) {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Point.class)
                .addAnnotatedClass(Sector.class)
                .addAnnotatedClass(Polygon.class)
                .buildSessionFactory();
        SwingUtilities.invokeLater(() -> create("./src/main/resources/map.properties").showInFrame());
    }

    /**
     * Создание OpenMap с заданным property handler.
     */
    private OpenMap(PropertyHandler propertyHandler) {
        configureMapPanel(propertyHandler);
    }

    /**
     * Создание OpenMap с заданным properties file.
     */
    public static OpenMap create(String propertiesFile) {
        return new OpenMap(configurePropertyHandler(propertiesFile));
    }

    /**
     * Конфигурация property handler.
     */
    private static PropertyHandler configurePropertyHandler(String propertiesFile) {
        try {
            return new PropertyHandler.Builder().setPropertiesFile(propertiesFile).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение обработчика карты.
     */
    public MapHandler getMapHandler() {
        return mapPanel.getMapHandler();
    }

    /**
     * Отображение карты во фрейме.
     */
    private void showInFrame() {
        MapHandler mapHandler = getMapHandler();
        OpenMapFrame omf = mapHandler.get(OpenMapFrame.class);
        omf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Конфигурация панели карты.
     */
    private void configureMapPanel(PropertyHandler propertyHandler) {
        mapPanel = new OverlayMapPanel(propertyHandler, false);
    }
}
