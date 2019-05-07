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

import model.Pacientes;
import utils.HibernateUtil;

/**
 * Home object for domain model class Pacientes.
 *
 * @see dao.Pacientes
 * @author Hibernate Tools
 */
public class PacientesHome {

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
            list = session.createQuery("from model.Pacientes D").list();
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
                    + "where FC.pacientes = PA.id and PA.deleted = 0 )").list();
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
        Query<Pacientes> query = session.createQuery("from model.Pacientes D where D.id = :id");
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
}
