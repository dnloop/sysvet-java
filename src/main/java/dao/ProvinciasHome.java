package dao;
// Generated Feb 11, 2019 1:57:02 PM by Hibernate Tools 5.3.6.Final

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Provincias;
import utils.HibernateUtilTest;

/**
 * Home object for domain model class Provincias.
 * @see dao.Provincias
 * @author Hibernate Tools
 */
public class ProvinciasHome {

    protected static final Logger log = (Logger) LogManager.getLogger(ProvinciasHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtilTest.getSessionFactory();

    public void add(Provincias instance) {
        log.debug(marker, "persisting Provincias instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.save(instance);
            tx.commit();
            log.debug(marker, "persist successful");
        } catch (RuntimeException re) {
            if (tx != null) {
                tx.rollback();
            }
            log.error(marker, "persist failed", re);
            throw re;
        } finally {
            session.flush();
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Provincias> displayRecords() {
        log.debug(marker, "retrieving Provincias list");
        List<Provincias> list= new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Provincias D").list();
            tx.commit();
            log.debug("retrieve successful, result size: " + list.size());
        } catch (RuntimeException re) {
            if (tx != null) {
                tx.rollback();
            }
            log.debug(marker, "retrieve failed", re);
            throw re;
        } finally {
            session.flush();
            session.close();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public Provincias showById(long id) {
        log.debug(marker, "getting Provincias instance with id: " + id);
        Provincias instance;
        Session session = sessionFactory.openSession();
        Query<Provincias> query = session.createQuery("from model.Provincias D where D.id = :id");
        query.setParameter("id", id);
        instance = (Provincias) query.uniqueResult();
        return instance;
    }

    public void update(Provincias instance) {
        log.debug(marker, "updating Provincias instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Provincias instance updated");
        } catch (RuntimeException re) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("update failed", re);
            throw re;
        } finally {
            session.flush();
            session.close();
        }
    }

    public void attachClean(Provincias instance) {
        log.debug("attaching clean Provincias instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(long id) {
        log.debug("deleting Provincias instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Provincias instance;
        try {
            tx = session.beginTransaction();
            instance = (Provincias) session.load(Provincias.class, id);
            session.delete(instance);
            tx.commit();
            log.debug("delete successful");
        } catch (RuntimeException re) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("delete failed", re);
            throw re;
        } finally {
            session.flush();
            session.close();
        }
    }
}
