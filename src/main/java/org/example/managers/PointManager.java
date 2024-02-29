package org.example.managers;

import org.example.objects.Point;
import org.example.startApp.OpenMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * The Point manager.
 */
public class PointManager {
    /**
     * The Session factory.
     */
    private static final SessionFactory sessionFactory = OpenMap.getSessionFactory();

    /**
     * Get all points list.
     *
     * @return the list
     */
    public static List<Point> getAllPoints() {
        try (Session session = sessionFactory.openSession()) {
            List<Point> points = session.createQuery("from Point", Point.class).list();
            points.replaceAll(point -> {
                Point newPoint = new Point(point.getName(), point.getLatitude(), point.getLongitude(), point.getCourse());
                newPoint.setId(point.getId());
                return newPoint;
            });
            return points;
        }
    }

    /**
     * Add point.
     *
     * @param point the point
     */
    public static void addPoint(Point point) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(point);
            session.getTransaction().commit();
        }
    }

    /**
     * Remove point.
     *
     * @param point the point
     */
    public static void removePoint(Point point) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(point);
            session.getTransaction().commit();
        }
    }

    /**
     * Update point.
     *
     * @param point the point
     */
    public static void updatePoint(Point point) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(point);
            session.getTransaction().commit();
        }
    }

    /**
     * Add all points.
     *
     * @param points the points
     */
    public static void addAllPoints(List<Point> points) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            points.forEach(session::save);
            session.getTransaction().commit();
        }
    }
}
