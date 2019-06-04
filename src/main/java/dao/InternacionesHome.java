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
import model.Internaciones;
import model.Pacientes;
import utils.HibernateUtil;

/**
 * Home object for domain model class Internaciones.
 *
 * @see dao.Internaciones
 * @author Hibernate Tools
 */
public class InternacionesHome {

    protected static final Logger log = (Logger) LogManager.getLogger(InternacionesHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(Internaciones instance) {
        log.debug(marker, "persisting Internaciones instance");
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
    public List<Internaciones> displayRecords() {
        log.debug(marker, "retrieving Internaciones list");
        List<Internaciones> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Internaciones I where I.deleted = false").list();
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
    public List<Internaciones> displayDeletedRecords() {
        log.debug(marker, "retrieving Internaciones list");
        List<Internaciones> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Internaciones I where I.deleted = true").list();
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
    public List<Pacientes> displayRecordsWithTreatments() {
        log.debug(marker, "retrieving Internaciones list with Tratamientos");
        List<Pacientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("select I.fichasClinicas.pacientes from model.Internaciones I"
                    + "where exists( select 1 from model.Tratamientos T"
                    + "    where I.id = T.id and I.deleted = false and T.deleted = false )").list();
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
    public Internaciones showById(Integer id) {
        log.debug(marker, "getting Internaciones instance with id: " + id);
        Internaciones instance;
        Session session = sessionFactory.openSession();
        Query<Internaciones> query = session
                .createQuery("from model.Internaciones I where I.id = :id and I.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<Internaciones> showByPatient(Pacientes id) {
        log.debug(marker, "retrieving Internaciones (by Ficha.pacientes) list");
        List<Internaciones> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query<Internaciones> query = session.createQuery(
                    "from model.Internaciones I where I.fichasClinicas.pacientes = :id and I.deleted = false");
            query.setParameter("id", id);
            list = query.list();
            for (Internaciones internacion : list) {
                FichasClinicas fc = internacion.getFichasClinicas();
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

    public void update(Internaciones instance) {
        log.debug(marker, "updating Internaciones instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Internaciones instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(Internaciones instance) {
        log.debug("attaching clean Internaciones instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void deleteAll(Integer id) {
        log.debug("deleting Internaciones by Patient");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery("UPDATE model.Internaciones i "
                + "SET i.deleted = true, i.deletedAt = now() WHERE i.fichasClinicas.pacientes = " + id);
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

    public void delete(Integer id) {
        log.debug("deleting Internaciones instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Internaciones instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(Internaciones.class, id);
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
                "UPDATE model.Internaciones i " + "SET i.deleted = false, i.modifiedAt = now() WHERE i.id = " + id);
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
