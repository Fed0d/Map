package org.example.objects;

import com.bbn.openmap.omGraphics.OMScalingIcon;

import javax.persistence.*;
import javax.swing.*;

/**
 * Класс представляющий растровый объект.
 */
@Entity
@Table(name = "points")
public class Point extends OMScalingIcon {
    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * Название.
     */
    @Column(name = "name")
    private String name;
    /**
     * Широта.
     */
    @Column(name = "latitude")
    private double latitude;
    /**
     * Долгота.
     */
    @Column(name = "longitude")
    private double longitude;
    /**
     * Курс.
     */
    @Column(name = "course")
    private double course;

    /**
     * Создание растрового объекта.
     *
     * @param name      название
     * @param latitude  широта
     * @param longitude долгота
     * @param course    курс
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
     * Конструктор по умолчанию
     */
    public Point() {
    }

    /**
     * Возвращает название.
     *
     * @return название
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название.
     *
     * @param name название
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает идентификатор.
     *
     * @return идентификатор
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор.
     *
     * @param id идентификатор
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает широту.
     *
     * @return широта
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Устанавливает широту.
     *
     * @param latitude широта
     */
    public void setLatitude(double latitude) {
        setLat(latitude);
        this.latitude = latitude;
    }

    /**
     * Возвращает долготу.
     *
     * @return долгота
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Устанавливает долготу.
     *
     * @param longitude долгота
     */
    public void setLongitude(double longitude) {
        setLon(longitude);
        this.longitude = longitude;
    }

    /**
     * Возвращает курс.
     *
     * @return курс
     */
    public double getCourse() {
        return course;
    }

    /**
     * Устанавливает курс.
     *
     * @param course курс
     */
    public void setCourse(double course) {
        setRotationAngle(course);
        this.course = course;
    }
}
