package workWithHibernate;

import entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class DormitoryHibernate {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Методы для работы с пользователями
    public static User findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE login = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }

    public static User findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }

    public static void addUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
    }

    public static boolean deleteUserByName(String username) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = findByUsername(username);
            if (user != null) {
                session.remove(user);
                transaction.commit();
                return true;
            }
            return false;
        }
    }

    public static List<User> findAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    // Методы для работы со студентами
    public static void addStudent(Student student) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        }
    }

    public static void updateStudent(Student student) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(student);
            transaction.commit();
        }
    }

    public static Student findStudentById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Student.class, id);
        }
    }

    public static Student findStudentByUser(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Student WHERE user.id = :userId", Student.class)
                    .setParameter("userId", userId)
                    .uniqueResult();
        }
    }

    public static List<Student> findAllStudents() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        }
    }

    public static boolean deleteStudent(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Student student = findStudentById(id);
            if (student != null) {
                session.remove(student);
                transaction.commit();
                return true;
            }
            return false;
        }
    }

    // Методы для работы с заявлениями
    public static void addApplication(Application application) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(application);
            transaction.commit();
        }
    }

    public static void updateApplication(Application application) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(application);
            transaction.commit();
        }
    }

    public static Application getApplicationById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Application.class, id);
        }
    }

    public static List<Application> findApplicationsByStudent(Long studentId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Application a WHERE a.student.user.id = :studentId", 
                Application.class
            )
            .setParameter("studentId", studentId)
            .list();
        }
    }

    public static List<Application> findApplicationsByDormitory(Long dormitoryId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Application a WHERE a.dormitory.id = :dormitoryId", 
                Application.class
            )
            .setParameter("dormitoryId", dormitoryId)
            .list();
        }
    }

    public static List<Application> getAllApplications() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Application", Application.class).list();
        }
    }
} 