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
import utils.HibernateUtil;

/**
 * Home object for domain model class Pacientes.
 *
 * @see dao.Pacientes
 * @author Hibernate Tools
 */
public class PacientesHome {

    private Long totalRecords;

    protected static final Logger log = (Logger) LogManager.getLogger(PacientesHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(Pacientes instance) {
        log.debug(marker, "persisting Pacientes instance");
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
    public List<Pacientes> displayRecords() {
        log.debug(marker, "retrieving Pacientes list");
        List<Pacientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Pacientes PA where PA.deleted = false").list();
            for (Pacientes pacientes : list)
                Hibernate.initialize(pacientes.getPropietarios());
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
    public List<Pacientes> displayRecordsWithExams() {
        log.debug(marker, "retrieving Pacientes list with ExamenGeneral");
        List<Pacientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Pacientes PA where exists(" + "select 1 from model.ExamenGeneral EX "
                    + "where EX.pacientes = PA.id and EX.deleted = false and PA.deleted = false)").list();
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
    public List<Pacientes> displayDeletedRecords() {
        log.debug(marker, "retrieving Pacientes list");
        List<Pacientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Pacientes PA where PA.deleted = true").list();
            for (Pacientes pacientes : list)
                Hibernate.initialize(pacientes.getPropietarios());
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
    public List<Pacientes> displayRecordsWithClinicalRecords() {
        log.debug(marker, "retrieving CuentasCorrientes list with Pacientes");
        List<Pacientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Pacientes PA where exists(select 1 from model.FichasClinicas FC "
                    + "where FC.pacientes = PA.id and PA.deleted = false and  FC.deleted = false)").list();
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
    public Pacientes showById(Integer id) {
        log.debug(marker, "getting Pacientes instance with id: " + id);
        Pacientes instance;
        Session session = sessionFactory.openSession();
        Query<Pacientes> query = session
                .createQuery("from model.Pacientes PA where PA.id = :id and PA.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        return instance;
    }

    public void update(Pacientes instance) {
        log.debug(marker, "updating Pacientes instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Pacientes instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(Pacientes instance) {
        log.debug("attaching clean Pacientes instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Integer id) {
        log.debug("deleting Pacientes instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Pacientes instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(Pacientes.class, id);
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
                "UPDATE model.Pacientes pr " + "SET pr.deleted = false, pr.updatedAt = now() WHERE pr.id = " + id);
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

    public Integer pageCountResult() {
        Session session = sessionFactory.openSession();
        String query = "Select count (PA.id) from model.Pacientes PA where PA.deleted = false";
        @SuppressWarnings("rawtypes")
        Query count = session.createQuery(query);

        log.debug("Total records: " + totalRecords);
        // this is not optimal!!
        Integer result = Integer.valueOf(count.uniqueResult().toString());
        session.close();
        return result;
    }

    public Integer getTotalRecords() {
        return pageCountResult();
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }
}
