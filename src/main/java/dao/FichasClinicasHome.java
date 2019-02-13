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

import model.FichasClinicas;
import utils.HibernateUtil;

/**
 * Home object for domain model class FichasClinicas.
 * @see dao.FichasClinicas
 * @author Hibernate Tools
 */
public class FichasClinicasHome {

    protected static final Logger log = (Logger) LogManager.getLogger(FichasClinicasHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(FichasClinicas instance) {
        log.debug(marker, "persisting FichasClinicas instance");
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
    public List<FichasClinicas> displayRecords() {
        log.debug(marker, "retrieving FichasClinicas list");
        List<FichasClinicas> list= new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.FichasClinicas D").list();
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
    public FichasClinicas showById(long id) {
        log.debug(marker, "getting FichasClinicas instance with id: " + id);
        FichasClinicas instance;
        Session session = sessionFactory.openSession();
        Query<FichasClinicas> query = session.createQuery("from model.FichasClinicas D where D.id = :id");
        query.setParameter("id", id);
        instance = (FichasClinicas) query.uniqueResult();
        return instance;
    }

    public void update(FichasClinicas instance) {
        log.debug(marker, "updating FichasClinicas instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "FichasClinicas instance updated");
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

    public void attachClean(FichasClinicas instance) {
        log.debug("attaching clean FichasClinicas instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(long id) {
        log.debug("deleting FichasClinicas instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        FichasClinicas instance;
        try {
            tx = session.beginTransaction();
            instance = (FichasClinicas) session.load(FichasClinicas.class, id);
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
