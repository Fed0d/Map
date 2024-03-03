package org.example.managers;

import org.example.startApp.OpenMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public interface Manager<T> {

    default SessionFactory getSessionFactory() {
        return OpenMap.getSessionFactory();
    }

    default void add(T object) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
        }
    }

    default void remove(T object) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(object);
            session.getTransaction().commit();
        }
    }

    default void update(T object) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
        }
    }

    default void addAll(List<T> objects) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            objects.forEach(session::save);
            session.getTransaction().commit();
        }
    }

    default void clear(Class<T> clazz) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("delete from " + clazz.getName()).executeUpdate();
            session.getTransaction().commit();
        }
    }
}