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

    public Polygon(String name, String latLon, String color) {
        super(parseStringToDouble(latLon), OMPoly.DECIMAL_DEGREES, OMPoly.LINETYPE_STRAIGHT);
        setFillPaint(Color.decode(color));
        this.name = name;
        this.latLon = latLon;
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

    public String getLatLon() {
        return latLon;
    }

    public void setLatLon(String latLon) {
        this.latLon = latLon;
        setLocation(parseStringToDouble(latLon), OMPoly.DECIMAL_DEGREES);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        setFillPaint(Color.decode(color));
    }

    public static double[] parseStringToDouble(String str) {
        String[] latLonString = str.substring(1, str.length() - 1).split(", ");
        return Arrays.stream(latLonString)
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
}
