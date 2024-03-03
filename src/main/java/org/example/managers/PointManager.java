package org.example.managers;

import org.example.objects.Point;
import org.hibernate.Session;

import java.util.List;

public class PointManager implements Manager<Point> {

    @Override
    public List<Point> getAll() {
        try (Session session = getSessionFactory().openSession()) {
            List<Point> points = session.createQuery("from Point", Point.class).list();
            points.replaceAll(point -> {
                Point newPoint = new Point(point.getName(), point.getLatitude(), point.getLongitude(), point.getCourse());
                newPoint.setId(point.getId());
                return newPoint;
            });
            return points;
        }
    }
}
