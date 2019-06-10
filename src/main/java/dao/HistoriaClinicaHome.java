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
import model.Pacientes;
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
            list = session.createQuery("from model.HistoriaClinica HC where HC.deleted = false").list();
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
    public List<Pacientes> displayRecordsWithClinicHistory() {
        log.debug(marker, "retrieving FichasClinicas list with Clinic History");
        List<Pacientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("select FC.pacientes from model.FichasClinicas FC where exists("
                    + "select 1 from model.HistoriaClinica HC where FC.id = HC.fichasClinicas and FC.deleted = false and HC.deleted = false)")
                    .list();
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
    public HistoriaClinica showById(Integer id) {
        log.debug(marker, "getting HistoriaClinica instance with id: " + id);
        HistoriaClinica instance;
        Session session = sessionFactory.openSession();
        Query<HistoriaClinica> query = session
                .createQuery("from model.HistoriaClinica HC where HC.id = :id and HC.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<HistoriaClinica> showByPatient(Pacientes id) {
        log.debug(marker, "retrieving FichasClinicas (by Pacientes) list");
        List<HistoriaClinica> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query<HistoriaClinica> query = session.createQuery(
                    "from model.HistoriaClinica HC where HC.fichasClinicas.pacientes = :id and HC.deleted = false");
            query.setParameter("id", id);
            list = query.list();
            for (HistoriaClinica fichas : list) {
                FichasClinicas fc = fichas.getFichasClinicas();
                Pacientes pa = fc.getPacientes();
                Hibernate.initialize(fc);
                Hibernate.initialize(pa);
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

    public void delete(Integer id) {
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
            session.close();
        }
    }

    public void recover(Integer id) {
        log.debug("recovering register");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery("UPDATE model.HistoriaClinica hc "
                + "SET hc.deleted = false, hc.updatedAt = now() WHERE hc.id = " + id);
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
