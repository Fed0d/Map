package org.example.objects;

import com.bbn.openmap.omGraphics.OMPoly;

import javax.persistence.*;
import java.awt.*;
import java.util.Arrays;

@Entity
@Table(name = "polygons")
public class Polygon extends OMPoly {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "latLon")
    String latLon;
    @Column(name = "color")
    String color;

    public Polygon() {
    }

    public Polygon(String name, double[] latLon, String color) {
        super(latLon, OMPoly.DECIMAL_DEGREES, OMPoly.LINETYPE_RHUMB, 0);
        this.name = name;
        this.latLon = Arrays.toString(latLon);
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getLatLon() {
        String[] latLonString = latLon.substring(1, latLon.length() - 1).split(", ");
        return Arrays.stream(latLonString)
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    public void setLatLon(double[] latLon) {
        this.latLon = Arrays.toString(latLon);
        setLocation(latLon, OMPoly.DECIMAL_DEGREES);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        setFillPaint(Color.decode(color));
    }
}
