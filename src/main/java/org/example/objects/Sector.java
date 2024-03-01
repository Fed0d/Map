package org.example.objects;

import com.bbn.openmap.omGraphics.OMArc;

import javax.persistence.*;
import java.awt.*;

@Entity
@Table(name = "sectors")
public class Sector extends OMArc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "latitudeCenter")
    private double latitudeCenter;
    @Column(name = "longitudeCenter")
    private double longitudeCenter;
    @Column(name = "xRadius")
    private int xRadius;
    @Column(name = "yRadius")
    private int yRadius;
    @Column(name = "start")
    private double startAngle;
    @Column(name = "extent")
    private double extentAngle;
    @Column(name = "course")
    private double course;
    @Column(name = "color")
    private String color;

    public Sector() {
    }

    public Sector(String name, double latitudeCenter, double longitudeCenter, int xRadius, int yRadius, double startAngle
            , double extentAngle, double course, String color) {
        super(latitudeCenter, longitudeCenter, 0, 0, 2 * xRadius, 2 * yRadius, startAngle, extentAngle);
        setFillPaint(Color.decode(color));
        setRotationAngle(course);
        setArcType(2);
        this.name = name;
        this.latitudeCenter = latitudeCenter;
        this.longitudeCenter = longitudeCenter;
        this.xRadius = xRadius;
        this.yRadius = yRadius;
        this.startAngle = startAngle;
        this.extentAngle = extentAngle;
        this.course = course;
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

    public void setLatLonCenter(double latitude, double longitude) {
        this.latitudeCenter = latitude;
        this.longitudeCenter = longitude;
        setLatLon(latitude, longitude);
    }

    public double getLatitudeCenter() {
        return latitudeCenter;
    }

    public int getxRadius() {
        return xRadius;
    }

    public void setxRadius(int xRadius) {
        this.xRadius = xRadius;
        setWidth(2 * xRadius);
    }

    public int getyRadius() {
        return yRadius;
    }

    public void setyRadius(int yRadius) {
        this.yRadius = yRadius;
        setHeight(2 * yRadius);
    }

    @Override
    public double getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
        setStart(startAngle);
    }

    @Override
    public double getExtentAngle() {
        return extentAngle;
    }

    public void setExtentAngle(double extentAngle) {
        this.extentAngle = extentAngle;
        setExtent(extentAngle);
    }

    public double getLongitudeCenter() {
        return longitudeCenter;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
        setRotationAngle(course);
    }
}
