package workWithHibernate;

import entity.Dormitory;
import entity.Room;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;

import java.util.List;

public class DormitoryHibernate {

    public static void addDormitory(Dormitory dormitory) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
            if (dormitory.getNumber() == null || dormitory.getAddress() == null || dormitory.getTotalCapacity() == null) {
                throw new IllegalArgumentException("Dormitory number, address and total capacity cannot be null");
            }
            
            session.persist(dormitory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to add dormitory: " + e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public static void updateDormitory(Dormitory dormitory) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(dormitory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static void deleteDormitory(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Dormitory dormitory = session.get(Dormitory.class, id);
            if (dormitory != null) {
                session.remove(dormitory);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static Dormitory getDormitoryById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Dormitory.class, id);
        }
    }

    public static List<Dormitory> getAllDormitories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Dormitory> query = session.createQuery("FROM Dormitory", Dormitory.class);
            return query.list();
        }
    }

    public static List<Dormitory> getAvailableDormitories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Получаем общежития, где есть свободные места в комнатах
            Query<Dormitory> query = session.createQuery(
                "SELECT DISTINCT r.dormitory FROM Room r " +
                "WHERE SIZE(r.students) < r.capacity",
                Dormitory.class
            );
            return query.list();
        }
    }

    public static boolean hasAvailableSpace(Long dormitoryId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Проверяем, есть ли свободные места в комнатах общежития
            Query<Long> query = session.createQuery(
                "SELECT COUNT(r) FROM Room r " +
                "WHERE r.dormitory.id = :dormitoryId " +
                "AND SIZE(r.students) < r.capacity",
                Long.class
            );
            query.setParameter("dormitoryId", dormitoryId);
            return query.uniqueResult() > 0;
        }
    }

    public static int getOccupiedSpaces(Long dormitoryId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Подсчитываем общее количество студентов в комнатах общежития
            Query<Long> query = session.createQuery(
                "SELECT COALESCE(SUM(SIZE(r.students)), 0) FROM Room r " +
                "WHERE r.dormitory.id = :dormitoryId",
                Long.class
            );
            query.setParameter("dormitoryId", dormitoryId);
            Long result = query.uniqueResult();
            return result != null ? result.intValue() : 0;
        }
    }
} 