package org.example.objects;

import com.bbn.openmap.omGraphics.OMRaster;

import javax.persistence.*;
import javax.swing.*;

/**
 * The Point class.
 */
@Entity
@Table(name = "points")
public class Point extends OMRaster {
    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * The name.
     */
    @Column(name = "name")
    private String name;
    /**
     * The latitude.
     */
    @Column(name = "latitude")
    private double latitude;
    /**
     * The longitude.
     */
    @Column(name = "longitude")
    private double longitude;
    /**
     * The course.
     */
    @Column(name = "course")
    private double course;

    /**
     * Instantiates a new Point.
     *
     * @param name      the name
     * @param latitude  the latitude
     * @param longitude the longitude
     * @param course    the course
     */
    public Point(String name, double latitude, double longitude, double course) {
        super(latitude, longitude, new ImageIcon("src/main/resources/images/point.png"));
        setRotationAngle(course);
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.course = course;
    }

    /**
     * Instantiates a new Point.
     */
    public Point() {
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
        setLat(latitude);
        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
        setLon(longitude);
        this.longitude = longitude;
    }

    /**
     * Gets the course.
     *
     * @return the course
     */
    public double getCourse() {
        return course;
    }

    /**
     * Sets the course.
     *
     * @param course the course
     */
    public void setCourse(double course) {
        setRotationAngle(course);
        this.course = course;
    }
}
