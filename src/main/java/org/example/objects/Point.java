package org.example.objects;

import com.bbn.openmap.omGraphics.OMScalingIcon;

import javax.persistence.*;
import javax.swing.*;

@Entity
@Table(name = "points")
public class Point extends OMScalingIcon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "course")
    private double course;

    public Point(String name, double latitude, double longitude, double course) {
        super(latitude, longitude, new ImageIcon("src/main/resources/images/point.png"));
        setRotationAngle(course);
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.course = course;
    }

    public Point() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        setLat(latitude);
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        setLon(longitude);
        this.longitude = longitude;
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        setRotationAngle(course);
        this.course = course;
    }
}
