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

import model.FichasClinicas;
import model.HistoriaClinica;
import utils.HibernateUtil;

/**
 * Home object for domain model class HistoriaClinica.
 *
 * @see dao.HistoriaClinica
 * @author Hibernate Tools
 */
public class HistoriaClinicaHome {

    protected static final Logger log = (Logger) LogManager.getLogger(HistoriaClinicaHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(HistoriaClinica instance) {
        log.debug(marker, "persisting HistoriaClinica instance");
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
            session.flush();
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<HistoriaClinica> displayRecords() {
        log.debug(marker, "retrieving HistoriaClinica list");
        List<HistoriaClinica> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.HistoriaClinica D").list();
            tx.commit();
            log.debug("retrieve successful, result size: " + list.size());
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.debug(marker, "retrieve failed", re);
            throw re;
        } finally {
            session.flush();
            session.close();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public HistoriaClinica showById(Integer id) {
        log.debug(marker, "getting HistoriaClinica instance with id: " + id);
        HistoriaClinica instance;
        Session session = sessionFactory.openSession();
        Query<HistoriaClinica> query = session.createQuery("from model.HistoriaClinica D where D.id = :id");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<HistoriaClinica> showByFicha(FichasClinicas id) {
        log.debug(marker, "retrieving HistoriaClinica (by Ficha) list");
        List<HistoriaClinica> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query<HistoriaClinica> query = session
                    .createQuery("from model.HistoriaClinica HC where HC.fichasClinicas = :id");
            query.setParameter("id", id);
            list = query.list();
            for (HistoriaClinica historiaClinica : list) {
                FichasClinicas fc = historiaClinica.getFichasClinicas();
                Hibernate.initialize(fc);
                Hibernate.initialize(fc.getPacientes());
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

    public void update(HistoriaClinica instance) {
        log.debug(marker, "updating HistoriaClinica instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "HistoriaClinica instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.flush();
            session.close();
        }
    }

    public void attachClean(HistoriaClinica instance) {
        log.debug("attaching clean HistoriaClinica instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(long id) {
        log.debug("deleting HistoriaClinica instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        HistoriaClinica instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(HistoriaClinica.class, id);
            session.delete(instance);
            tx.commit();
            log.debug("delete successful");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("delete failed", re);
            throw re;
        } finally {
            session.flush();
            session.close();
        }
    }
}
