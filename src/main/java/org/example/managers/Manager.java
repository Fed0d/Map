package org.example.managers;

import org.example.startApp.OpenMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Интерфейс для работы с объектами
 *
 * @param <T> тип объекта
 */
public interface Manager<T> {

    /**
     * Получение фабрики сессий
     *
     * @return фабрика сессий
     */
    default SessionFactory getSessionFactory() {
        return OpenMap.getSessionFactory();
    }

    /**
     * Добавление объекта в базу данных
     *
     * @param object объект
     */
    default void add(T object) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
        }
    }

    /**
     * Удаление объекта из базы данных
     *
     * @param object объект
     */
    default void remove(T object) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(object);
            session.getTransaction().commit();
        }
    }

    /**
     * Обновление объекта в базе данных
     *
     * @param object объект
     */
    default void update(T object) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
        }
    }

    /**
     * Добавление списка объектов в базу данных
     *
     * @param objects список объектов
     */
    default void addAll(List<T> objects) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            objects.forEach(session::save);
            session.getTransaction().commit();
        }
    }

    /**
     * Удаление всех объектов из базы данных
     *
     * @param clazz класс объекта
     */
    default void clear(Class<T> clazz) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("delete from " + clazz.getName()).executeUpdate();
            session.getTransaction().commit();
        }
    }

    /**
     * Получение всех объектов из базы данных
     *
     * @return список объектов
     */
    List<T> getAll();
}