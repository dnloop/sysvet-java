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
            list = session.createQuery("from model.Vacunas VC where VC.deleted = false").list();
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
    public List<Vacunas> displayDeletedRecords() {
        log.debug(marker, "retrieving Vacunas list");
        List<Vacunas> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Vacunas VC where VC.deleted = true").list();
            for (Vacunas vacuna : list)
                Hibernate.initialize(vacuna.getPacientes());
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
    public List<Pacientes> displayRecordsWithVaccines() {
        log.debug(marker, "retrieving Vacunas list with Vacunas");
        List<Pacientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery(
                    "select VC.pacientes from model.Vacunas VC " + "where exists(select 1 from model.Pacientes PA "
                            + "where VC.id = PA.id and VC.deleted = false and PA.deleted = false)")
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
    public Vacunas showById(Integer id) {
        log.debug(marker, "getting Vacunas instance with id: " + id);
        Vacunas instance;
        Session session = sessionFactory.openSession();
        Query<Vacunas> query = session.createQuery("from model.Vacunas VC where VC.id = :id and VC.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        session.close();
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
            Query<Vacunas> query = session
                    .createQuery("from model.Vacunas VC where VC.pacientes = :id and VC.deleted = false");
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

    public void delete(Integer id) {
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
            session.close();
        }
    }

    public void deleteAll(Integer id) {
        log.debug("deleting Vacunas by paciente ");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery(
                "UPDATE model.Vacunas V " + "SET V.deleted = true, V.deletedAt = now() WHERE V.pacientes = " + id);
        try {
            tx = session.beginTransaction();
            query.executeUpdate();
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
                "UPDATE model.Vacunas vc " + "SET vc.deleted = false, vc.updatedAt = now() WHERE vc.id = " + id);
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
