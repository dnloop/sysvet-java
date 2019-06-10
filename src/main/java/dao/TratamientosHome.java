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

import model.Internaciones;
import model.Tratamientos;
import utils.HibernateUtil;

/**
 * Home object for domain model class Tratamientos.
 *
 * @see dao.Tratamientos
 * @author Hibernate Tools
 */
public class TratamientosHome {

    protected static final Logger log = (Logger) LogManager.getLogger(TratamientosHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(Tratamientos instance) {
        log.debug(marker, "persisting Tratamientos instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.save(instance);
            tx.commit();
            log.debug(marker, "persist successful");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error(marker, "persist failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Tratamientos> displayRecords() {
        log.debug(marker, "retrieving Tratamientos list");
        List<Tratamientos> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Tratamientos T where T.deleted = false").list();
            tx.commit();
            log.debug("retrieve successful, result size: " + list.size());
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.debug(marker, "retrieve failed", re);
            throw re;
        } finally {
            session.close();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Tratamientos> displayDeletedRecords() {
        log.debug(marker, "retrieving Tratamientos list");
        List<Tratamientos> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Tratamientos T where T.deleted = true").list();
            tx.commit();
            log.debug("retrieve successful, result size: " + list.size());
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.debug(marker, "retrieve failed", re);
            throw re;
        } finally {
            session.close();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public Tratamientos showById(Integer id) {
        log.debug(marker, "getting Tratamientos instance with id: " + id);
        Tratamientos instance;
        Session session = sessionFactory.openSession();
        Query<Tratamientos> query = session
                .createQuery("from model.Tratamientos T where T.id = :id and T.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        session.close();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<Tratamientos> showByInternacion(Internaciones id) {
        log.debug(marker, "retrieving Tratamientos (by Ficha) list");
        List<Tratamientos> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query<Tratamientos> query = session
                    .createQuery("from model.Tratamientos T where T.internaciones = :id and T.deleted = false");
            query.setParameter("id", id);
            list = query.list();
            for (Tratamientos tratamiento : list) {
                Internaciones fc = tratamiento.getInternaciones();
                Hibernate.initialize(fc);
                Hibernate.initialize(fc.getFichasClinicas().getPacientes());
            }
            tx.commit();
            log.debug("retrieve successful, result size: " + list.size());
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.debug(marker, "retrieve failed", re);
            throw re;
        } finally {
            session.close();
        }
        return list;
    }

    public void update(Tratamientos instance) {
        log.debug(marker, "updating Tratamientos instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Tratamientos instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(Tratamientos instance) {
        log.debug("attaching clean Tratamientos instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Integer id) {
        log.debug("deleting Tratamientos instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Tratamientos instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(Tratamientos.class, id);
            session.delete(instance);
            tx.commit();
            log.debug("delete successful");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("delete failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void recover(Integer id) {
        log.debug("recovering register");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery(
                "UPDATE model.Tratamientos tr " + "SET tr.deleted = false, tr.updatedAt = now() WHERE tr.id = " + id);
        try {
            tx = session.beginTransaction();
            query.executeUpdate();
            tx.commit();
            log.debug("recover successful");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("delete failed", re);
            throw re;
        } finally {
            session.close();
        }
    }
}
