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
import model.Pacientes;
import model.Retornos;
import utils.HibernateUtil;

/**
 * Home object for domain model class Retornos.
 *
 * @see dao.Retornos
 * @author Hibernate Tools
 */
public class RetornosHome {

    protected static final Logger log = (Logger) LogManager.getLogger(RetornosHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(Retornos instance) {
        log.debug(marker, "persisting Retornos instance");
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
    public List<Retornos> displayRecords() {
        log.debug(marker, "retrieving Retornos list");
        List<Retornos> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Retornos RT where RT.deleted = false").list();
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
    public List<Retornos> displayDeletedRecords() {
        log.debug(marker, "retrieving Retornos list");
        List<Retornos> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Retornos RT where RT.deleted = true").list();
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
    public Retornos showById(Integer id) {
        log.debug(marker, "getting Retornos instance with id: " + id);
        Retornos instance;
        Session session = sessionFactory.openSession();
        Query<Retornos> query = session.createQuery("from model.Retornos RT where RT.id = :id and RT.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<Retornos> showByPaciente(Pacientes id) {
        log.debug(marker, "retrieving Retornos (by Ficha.pacientes) list");
        List<Retornos> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query<Retornos> query = session.createQuery(
                    "from model.Retornos RT where RT.fichasClinicas.pacientes = :id and RT.deleted = false");
            query.setParameter("id", id);
            list = query.list();
            for (Retornos retorno : list) {
                FichasClinicas fc = retorno.getFichasClinicas();
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

    public void update(Retornos instance) {
        log.debug(marker, "updating Retornos instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Retornos instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(Retornos instance) {
        log.debug("attaching clean Retornos instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Integer id) {
        log.debug("deleting Retornos instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Retornos instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(Retornos.class, id);
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
                "UPDATE model.Retornos rt " + "SET rt.deleted = false, rt.modifiedAt = now() WHERE rt.id = " + id);
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
