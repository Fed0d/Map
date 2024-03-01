package org.example.managers;

import org.example.objects.Sector;
import org.example.startApp.OpenMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class SectorManager {
    private static final SessionFactory sessionFactory = OpenMap.getSessionFactory();

    public static void addSector(Sector sector) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(sector);
            session.getTransaction().commit();
        }
    }

    public static void removeSector(Sector sector) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(sector);
            session.getTransaction().commit();
        }
    }

    public static void updateSector(Sector sector) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(sector);
            session.getTransaction().commit();
        }
    }

    public static List<Sector> getAllSectors() {
        try (Session session = sessionFactory.openSession()) {
            List<Sector> sectors = session.createQuery("from Sector", Sector.class).list();
            sectors.replaceAll(sector -> {
                Sector newSector = new Sector(sector.getName(), sector.getLatitudeCenter(), sector.getLongitudeCenter()
                        , sector.getxRadius(), sector.getyRadius(), sector.getStartAngle(), sector.getExtentAngle()
                        , sector.getCourse(), sector.getColor());
                newSector.setId(sector.getId());
                newSector.setArcType(2);
                return newSector;
            });
            return sectors;
        }
    }

    public static void addAllSectors(List<Sector> sectors) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (Sector sector : sectors) {
                session.save(sector);
            }
            session.getTransaction().commit();
        }
    }

    public static void clear() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete from Sector").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
