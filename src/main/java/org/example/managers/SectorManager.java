package org.example.managers;

import org.example.objects.Sector;
import org.hibernate.Session;

import java.util.List;

/**
 * Менеджер для работы с секторами.
 */
public class SectorManager implements Manager<Sector> {

    /**
     * Получение всех секторов из базы данных.
     *
     * @return список всех секторов
     */
    @Override
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
