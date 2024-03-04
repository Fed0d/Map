package org.example.managers;

import org.example.objects.Polygon;
import org.hibernate.Session;

import java.util.List;

/**
 * Менеджер для работы с полигонами.
 */
public class PolygonManager implements Manager<Polygon> {

    /**
     * Получение всех полигонов из базы данных.
     *
     * @return список всех полигонов
     */
    @Override
    public List<Polygon> getAll() {
        try (Session session = getSessionFactory().openSession()) {
            List<Polygon> polygons = session.createQuery("from Polygon", Polygon.class).list();
            polygons.replaceAll(polygon -> {
                Polygon newPolygon = new Polygon(polygon.getName(), polygon.getLatLon(), polygon.getColor());
                newPolygon.setId(polygon.getId());
                return newPolygon;
            });
            return polygons;
        }
    }
}
