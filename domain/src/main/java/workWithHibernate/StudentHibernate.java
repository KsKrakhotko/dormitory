package workWithHibernate;

import entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StudentHibernate {

    public static void addStudent(Student student) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        }
    }

    public static void updateStudent(Student student) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(student);
            transaction.commit();
        }
    }

    public static Student findStudentById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        }
    }

    public static Student findStudentByUser(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery(
                "FROM Student WHERE user.id = :userId",
                Student.class
            );
            query.setParameter("userId", userId);
            return query.uniqueResult();
        }
    }

    public static List<Student> findAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery(
                "FROM Student ORDER BY lastName, firstName",
                Student.class
            );
            return query.list();
        }
    }

    public static boolean deleteStudent(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.remove(student);
                transaction.commit();
                return true;
            }
            transaction.rollback();
            return false;
        }
    }
} 