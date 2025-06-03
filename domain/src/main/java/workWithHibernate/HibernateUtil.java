package workWithHibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    static {
        try {
            registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();

            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();

        } catch (Exception e) {
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
            System.err.println("Initial SessionFactory creation failed: " + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
} 