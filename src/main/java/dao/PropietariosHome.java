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

import model.Propietarios;
import utils.HibernateUtil;

/**
 * Home object for domain model class Propietarios.
 *
 * @see dao.Propietarios
 * @author Hibernate Tools
 */
public class PropietariosHome {

    private Long totalRecords;

    protected static final Logger log = (Logger) LogManager.getLogger(ProvinciasHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(Propietarios instance) {
        log.debug(marker, "persisting Propietarios instance");
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
    public List<Propietarios> displayRecords() {
        log.debug(marker, "retrieving Propietarios list");
        List<Propietarios> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Propietarios PR where PR.deleted = false").list();
            for (Propietarios propietarios : list) {
                Hibernate.initialize(propietarios.getLocalidades());
                Hibernate.initialize(propietarios.getLocalidades().getProvincias());
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

    @SuppressWarnings("unchecked")
    public List<Propietarios> displayDeletedRecords() {
        log.debug(marker, "retrieving Propietarios list");
        List<Propietarios> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Propietarios PR where PR.deleted = true").list();
            for (Propietarios propietarios : list) {
                Hibernate.initialize(propietarios.getLocalidades());
                Hibernate.initialize(propietarios.getLocalidades().getProvincias());
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

    @SuppressWarnings("unchecked")
    public Propietarios showById(Integer id) {
        log.debug(marker, "getting Propietarios instance with id: " + id);
        Propietarios instance;
        Session session = sessionFactory.openSession();
        Query<Propietarios> query = session
                .createQuery("from model.Propietarios PR where PR.id = :id and PR.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        session.close();
        return instance;
    }

    public void update(Propietarios instance) {
        log.debug(marker, "updating Propietarios instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Propietarios instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(Propietarios instance) {
        log.debug("attaching clean Propietarios instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Integer id) {
        log.debug("deleting Propietarios instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Propietarios instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(Propietarios.class, id);
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
                "UPDATE model.Propietarios pr " + "SET pr.deleted = false, pr.updatedAt = now() WHERE pr.id = " + id);
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

    // utility methods

    public Integer pageCountResult() {
        Session session = sessionFactory.openSession();
        String query = "Select count (PO.id) from model.Propietarios PO where PO.deleted = false";
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
