package workWithHibernate;

import entity.User;
import entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserHibernate {

    public static User findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User WHERE login = :username",
                User.class
            );
            query.setParameter("username", username);
            return query.uniqueResult();
        }
    }

    public static void addUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
    }

    public static User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    public static List<User> findAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            return query.list();
        }
    }

    public static boolean deleteUserByName(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                // First find the user
                User user = findByUsername(username);
                if (user == null) {
                    return false;
                }

                // Delete associated student record if exists
                Query<?> deleteStudentQuery = session.createQuery(
                    "DELETE FROM Student WHERE user.id = :userId"
                );
                deleteStudentQuery.setParameter("userId", user.getId());
                deleteStudentQuery.executeUpdate();

                // Now delete the user
                session.remove(user);
                transaction.commit();
                return true;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public static void updateUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        }
    }

    public static boolean hasStudentRecords(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(s) FROM Student s WHERE s.user.id = :userId",
                Long.class
            );
            query.setParameter("userId", userId);
            return query.uniqueResult() > 0;
        }
    }

    public static User getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }
} 