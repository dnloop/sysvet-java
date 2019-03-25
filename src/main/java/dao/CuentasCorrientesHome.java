package dao;
// Generated Feb 11, 2019 1:57:02 PM by Hibernate Tools 5.3.6.Final

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.CuentasCorrientes;
import utils.HibernateUtilTest;

/**
 * Home object for domain model class CuentasCorrientes.
 * @see dao.CuentasCorrientes
 * @author Hibernate Tools
 */

public class CuentasCorrientesHome {

    protected static final Logger log = (Logger) LogManager.getLogger(CuentasCorrientesHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtilTest.getSessionFactory();

    public void add(CuentasCorrientes instance) {
        log.debug(marker, "persisting CuentasCorrientes instance");
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
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<CuentasCorrientes> displayRecords() {
        log.debug(marker, "retrieving CuentasCorrientes list");
        List<CuentasCorrientes> list= new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.CuentasCorrientes CC where deleted = false").list();
            tx.commit();
            log.debug(marker, "retrieve successful, result size: " + list.size());
            log.debug(marker, "Initializing lazy loaded");
            for (CuentasCorrientes  cc : list)
                Hibernate.initialize(cc.getPropietarios());
        } catch (RuntimeException re) {
            if (tx != null) {
                tx.rollback();
            }
            log.debug(marker, "retrieve failed", re);
            throw re;
        } finally {
            session.close();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public CuentasCorrientes showById(long id) {
        log.debug(marker, "getting CuentasCorrientes instance with id: " + id);
        CuentasCorrientes instance;
        Session session = sessionFactory.openSession();
        Query<CuentasCorrientes> query = session.createQuery("from model.CuentasCorrientes CC where CC.id = :id");
        query.setParameter("id", id);
        instance = (CuentasCorrientes) query.uniqueResult();
        return instance;
    }

    public void update(CuentasCorrientes instance) {
        log.debug(marker, "updating CuentasCorrientes instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "CuentasCorrientes instance updated");
        } catch (RuntimeException re) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(CuentasCorrientes instance) {
        log.debug("attaching clean CuentasCorrientes instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Integer id) {
        log.debug("deleting CuentasCorrientes instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        CuentasCorrientes instance;
        try {
            tx = session.beginTransaction();
            instance = (CuentasCorrientes) session.load(CuentasCorrientes.class, id);
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
            session.close();
        }
    }
}
