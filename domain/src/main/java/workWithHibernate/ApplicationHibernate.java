package workWithHibernate;

import entity.Application;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ApplicationHibernate {
    public static void addApplication(Application application) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(application);
            transaction.commit();
        }
    }

    public static void updateApplication(Application application) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(application);
            transaction.commit();
        }
    }

    public static Application getApplicationById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Application.class, id);
        }
    }

    public static List<Application> findApplicationsByStudent(Long studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Application a WHERE a.student.user.id = :studentId",
                Application.class
            )
            .setParameter("studentId", studentId)
            .list();
        }
    }

    public static List<Application> findApplicationsByDormitory(Long dormitoryId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Application a WHERE a.dormitory.id = :dormitoryId",
                Application.class
            )
            .setParameter("dormitoryId", dormitoryId)
            .list();
        }
    }

    public static List<Application> getAllApplications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Application", Application.class).list();
        }
    }
} 