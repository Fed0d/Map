package org.example.objects;

import com.bbn.openmap.omGraphics.OMArc;

import javax.persistence.*;
import java.awt.*;

/**
 * Класс представляющий сектор.
 */
@Entity
@Table(name = "sectors")
public class Sector extends OMArc {
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
     * Широта центра.
     */
    @Column(name = "latitudeCenter")
    private double latitudeCenter;
    /**
     * Долгота центра.
     */
    @Column(name = "longitudeCenter")
    private double longitudeCenter;
    /**
     * Радиус по оси X.
     */
    @Column(name = "xRadius")
    private int xRadius;
    /**
     * Радиус по оси Y.
     */
    @Column(name = "yRadius")
    private int yRadius;
    /**
     * Начальный угол.
     */
    @Column(name = "start")
    private double startAngle;
    /**
     * Угол развёртки.
     */
    @Column(name = "extent")
    private double extentAngle;
    /**
     * Курс.
     */
    @Column(name = "course")
    private double course;
    /**
     * Цвет.
     */
    @Column(name = "color")
    private String color;

    /**
     * Конструктор по умолчанию
     */
    public Sector() {
    }

    /**
     * Создание сектора.
     *
     * @param name            название
     * @param latitudeCenter  широта центра
     * @param longitudeCenter долгота центра
     * @param xRadius         радиус по оси X
     * @param yRadius         радиус по оси Y
     * @param startAngle      начальный угол
     * @param extentAngle     угол развёртки
     * @param course          курс
     * @param color           цвет
     */
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

    /**
     * Получение идентификатора.
     *
     * @return идентификатор
     */
    public Long getId() {
        return id;
    }

    /**
     * Установка идентификатора.
     *
     * @param id идентификатор
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получение названия.
     *
     * @return название
     */
    public String getName() {
        return name;
    }

    /**
     * Установка названия.
     *
     * @param name название
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Установка центра сектора.
     *
     * @param latitude  широта
     * @param longitude долгота
     */
    public void setLatLonCenter(double latitude, double longitude) {
        this.latitudeCenter = latitude;
        this.longitudeCenter = longitude;
        setLatLon(latitude, longitude);
    }

    /**
     * Получение широты центра.
     *
     * @return широта центра
     */
    public double getLatitudeCenter() {
        return latitudeCenter;
    }

    /**
     * Получение радиуса по оси X.
     *
     * @return радиус по оси X
     */
    public int getxRadius() {
        return xRadius;
    }

    /**
     * Установка радиуса по оси X.
     *
     * @param xRadius радиус по оси X
     */
    public void setxRadius(int xRadius) {
        this.xRadius = xRadius;
        setWidth(2 * xRadius);
    }

    /**
     * Получение радиуса по оси Y.
     *
     * @return радиус по оси Y
     */
    public int getyRadius() {
        return yRadius;
    }

    /**
     * Установка радиуса по оси Y.
     *
     * @param yRadius радиус по оси Y
     */
    public void setyRadius(int yRadius) {
        this.yRadius = yRadius;
        setHeight(2 * yRadius);
    }

    /**
     * Получение начального угла.
     *
     * @return начальный угол
     */
    @Override
    public double getStartAngle() {
        return startAngle;
    }

    /**
     * Установка начального угла.
     *
     * @param startAngle начальный угол
     */
    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
        setStart(startAngle);
    }

    /**
     * Получение угла развёртки.
     *
     * @return угол развёртки
     */
    @Override
    public double getExtentAngle() {
        return extentAngle;
    }

    /**
     * Установка угла развёртки.
     *
     * @param extentAngle угол развёртки
     */
    public void setExtentAngle(double extentAngle) {
        this.extentAngle = extentAngle;
        setExtent(extentAngle);
    }

    /**
     * Получение долготы центра.
     *
     * @return долгота центра
     */
    public double getLongitudeCenter() {
        return longitudeCenter;
    }

    /**
     * Получение цвета.
     *
     * @return цвет
     */
    public String getColor() {
        return color;
    }

    /**
     * Установка цвета.
     *
     * @param color цвет
     */
    public void setColor(String color) {
        this.color = color;
        setFillPaint(Color.decode(color));
    }

    /**
     * Получение курса.
     *
     * @return курс
     */
    public double getCourse() {
        return course;
    }

    /**
     * Установка курса.
     *
     * @param course курс
     */
    public void setCourse(double course) {
        this.course = course;
        setRotationAngle(course);
    }
}
