package org.example.startApp;

import com.bbn.openmap.MapHandler;
import com.bbn.openmap.PropertyHandler;
import com.bbn.openmap.gui.MapPanel;
import com.bbn.openmap.gui.OpenMapFrame;
import com.bbn.openmap.gui.OverlayMapPanel;
import org.example.objects.Point;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.io.IOException;

/**
 * A simple OpenMap application.
 *
 * @author Dmitriy Samorodov
 */
public class OpenMap {
    /**
     * The map panel.
     */
    private MapPanel mapPanel;
    /**
     * The session factory.
     */
    private static SessionFactory sessionFactory;
    /**
     * Get the session factory.
     *
     * @return the session factory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * The main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Point.class)
                .buildSessionFactory();
        SwingUtilities.invokeLater(() -> create("./src/main/resources/map.properties").showInFrame());
    }

    /***
     * Create an OpenMap instance with the given property handler.
     * @param propertyHandler the property handler to use, or null if none
     */
    private OpenMap(PropertyHandler propertyHandler) {
        configureMapPanel(propertyHandler);
    }

    /***
     * Create an OpenMap instance with the given properties file.
     * @param propertiesFile the properties file to use
     * @return the OpenMap instance
     */
    public static OpenMap create(String propertiesFile) {
        return new OpenMap(configurePropertyHandler(propertiesFile));
    }

    /***
     * Configure the property handler with the given properties file.
     * @param propertiesFile the properties file to use
     * @return the property handler
     */
    private static PropertyHandler configurePropertyHandler(String propertiesFile) {
        try {
            return new PropertyHandler.Builder().setPropertiesFile(propertiesFile).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the map panel.
     *
     * @return the map panel
     */
    public MapHandler getMapHandler() {
        return mapPanel.getMapHandler();
    }

    /**
     * Show the map in a frame.
     */
    private void showInFrame() {
        MapHandler mapHandler = getMapHandler();
        OpenMapFrame omf = mapHandler.get(OpenMapFrame.class);
        omf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Configure the map panel with the given property handler.
     *
     * @param propertyHandler the property handler to use, or null if none
     */
    private void configureMapPanel(PropertyHandler propertyHandler) {
        mapPanel = new OverlayMapPanel(propertyHandler, false);
    }
}
