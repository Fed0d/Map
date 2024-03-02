package org.example.managers;

import org.example.objects.Sector;
import org.example.startApp.OpenMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class SectorManager  implements Manager<Sector> {

    public List<Sector> getAll() {
        try (Session session = getSessionFactory().openSession()) {
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
}
