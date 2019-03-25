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

import model.Localidades;
import utils.HibernateUtilTest;

/**
 * Home object for domain model class Localidades.
 * @see dao.Localidades
 * @author Hibernate Tools
 */
public class LocalidadesHome {

    protected static final Logger log = (Logger) LogManager.getLogger(LocalidadesHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtilTest.getSessionFactory();

    public void add(Localidades instance) {
        log.debug(marker, "persisting Localidades instance");
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
    public List<Localidades> displayRecords() {
        log.debug(marker, "retrieving Localidades list");
        List<Localidades> list= new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Localidades D").list();
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
    public Localidades showById(long id) {
        log.debug(marker, "getting Localidades instance with id: " + id);
        Localidades instance;
        Session session = sessionFactory.openSession();
        Query<Localidades> query = session.createQuery("from model.Localidades D where D.id = :id");
        query.setParameter("id", id);
        instance = (Localidades) query.uniqueResult();
        return instance;
    }

    public void update(Localidades instance) {
        log.debug(marker, "updating Localidades instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Localidades instance updated");
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

    public void attachClean(Localidades instance) {
        log.debug("attaching clean Localidades instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(long id) {
        log.debug("deleting Localidades instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Localidades instance;
        try {
            tx = session.beginTransaction();
            instance = (Localidades) session.load(Localidades.class, id);
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
