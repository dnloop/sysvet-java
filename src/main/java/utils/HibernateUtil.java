package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static final Logger log = (Logger) LogManager.getLogger(HibernateUtil.class);
    private static final Marker marker = MarkerManager.getMarker("CLASS");
    static SessionFactory sessionFactory;

    public static void setUp() throws DatabaseInitException {
        // A SessionFactory is set up once for an application!
        log.debug(marker, "Setting up SessionFactory");
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings
                                                                                                  // from
                                                                                                  // hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            log.debug(marker, "SessionFactory was set up");
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble
            // building the SessionFactory
            // so destroy it manually.
            log.error("Could not create SessionFactory", e);
            StandardServiceRegistryBuilder.destroy(registry);
            throw new DatabaseInitException("Base de datos no iniciada.");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}