package org.example.managers;

import org.example.objects.Point;
import org.example.startApp.OpenMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class PointManager {
    private static final SessionFactory sessionFactory = OpenMap.getSessionFactory();

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

    public static void addPoint(Point point) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(point);
            session.getTransaction().commit();
        }
    }

    public static void removePoint(Point point) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(point);
            session.getTransaction().commit();
        }
    }

    public static void updatePoint(Point point) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(point);
            session.getTransaction().commit();
        }
    }

    public static void addAllPoints(List<Point> points) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            points.forEach(session::save);
            session.getTransaction().commit();
        }
    }
}
