package org.example.startApp;

import com.bbn.openmap.MapHandler;
import com.bbn.openmap.PropertyHandler;
import com.bbn.openmap.gui.MapPanel;
import com.bbn.openmap.gui.OpenMapFrame;
import com.bbn.openmap.gui.OverlayMapPanel;

import javax.swing.*;
import java.io.IOException;

/**
 * A simple OpenMap application.
 */
public class OpenMap {
    /****
     * The map panel.
     */
    private MapPanel mapPanel;

    /***
     * Main method to create and show the OpenMap instance.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> create("./map.properties").showInFrame());
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
