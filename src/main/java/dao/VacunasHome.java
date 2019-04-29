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

import model.Pacientes;
import model.Vacunas;
import utils.HibernateUtil;

/**
 * Home object for domain model class Vacunas.
 *
 * @see dao.Vacunas
 * @author Hibernate Tools
 */
public class VacunasHome {

    protected static final Logger log = (Logger) LogManager.getLogger(VacunasHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(Vacunas instance) {
        log.debug(marker, "persisting Vacunas instance");
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
    public List<Vacunas> displayRecords() {
        log.debug(marker, "retrieving Vacunas list");
        List<Vacunas> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Vacunas D").list();
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
    public List<Object> displayRecordsWithVaccines() {
        log.debug(marker, "retrieving Vacunas list with Vacunas");
        List<Object> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("select VC.id, VC.pacientes from model.Vacunas VC where exists("
                    + "select 1 from model.Pacientes PA where VC.id = PA.Vacunas)").list();
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
    public Vacunas showById(Integer id) {
        log.debug(marker, "getting Vacunas instance with id: " + id);
        Vacunas instance;
        Session session = sessionFactory.openSession();
        Query<Vacunas> query = session.createQuery("from model.Vacunas D where D.id = :id");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        return instance;
    }

    public void update(Vacunas instance) {
        log.debug(marker, "updating Vacunas instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Vacunas instance updated");
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

    @SuppressWarnings("unchecked")
    public List<Vacunas> showByPatient(Pacientes id) {
        log.debug(marker, "retrieving Vacunas (by Ficha) list");
        List<Vacunas> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query<Vacunas> query = session.createQuery("from model.Vacunas VC where VC.pacientes = :id");
            query.setParameter("id", id);
            list = query.list();
            for (Vacunas vacuna : list) {
                Pacientes pa = vacuna.getPacientes();
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

    public void attachClean(Vacunas instance) {
        log.debug("attaching clean Vacunas instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(long id) {
        log.debug("deleting Vacunas instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Vacunas instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(Vacunas.class, id);
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
