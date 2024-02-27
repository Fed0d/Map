package org.example.objects;

import com.bbn.openmap.omGraphics.OMRaster;

import javax.swing.*;

public class Point extends OMRaster {
    private String name;

    public Point(String name, double latitude, double longitude, double course) {
        super(latitude, longitude, new ImageIcon("src/main/resources/images/point.png"));
        setRotationAngle(course);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
