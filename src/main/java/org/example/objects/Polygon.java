package org.example.objects;

import com.bbn.openmap.omGraphics.OMPoly;

import javax.persistence.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Класс представляющий полигон.
 */
@Entity
@Table(name = "polygons")
public class Polygon extends OMPoly {
    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    /**
     * Название.
     */
    @Column(name = "name")
    private String name;
    /**
     * Координаты точек.
     */
    @Column(name = "latLon")
    String latLon;
    /**
     * Цвет.
     */
    @Column(name = "color")
    String color;

    /**
     * Конструктор по умолчанию
     */
    public Polygon() {
    }

    /**
     * Создание полигона.
     *
     * @param name   название
     * @param latLon координаты точек
     * @param color  цвет
     */
    public Polygon(String name, String latLon, String color) {
        super(parseStringToDouble(latLon), OMPoly.DECIMAL_DEGREES, OMPoly.LINETYPE_STRAIGHT);
        setFillPaint(Color.decode(color));
        this.name = name;
        this.latLon = latLon;
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
     * Получение координат точек.
     *
     * @return координаты точек
     */
    public String getLatLon() {
        return latLon;
    }

    /**
     * Установка координат точек.
     *
     * @param latLon координаты точек
     */
    public void setLatLon(String latLon) {
        this.latLon = latLon;
        setLocation(parseStringToDouble(latLon), OMPoly.DECIMAL_DEGREES);
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
     * Преобразование строки в массив double.
     *
     * @param str строка
     * @return массив double
     */
    public static double[] parseStringToDouble(String str) {
        String[] latLonString = str.substring(1, str.length() - 1).split(", ");
        return Arrays.stream(latLonString)
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
}
