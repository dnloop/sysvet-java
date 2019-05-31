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

import model.CuentasCorrientes;
import model.Propietarios;
import utils.HibernateUtil;

/**
 * Home object for domain model class CuentasCorrientes.
 *
 * @see dao.CuentasCorrientes
 * @author Hibernate Tools
 */

public class CuentasCorrientesHome {

    protected static final Logger log = (Logger) LogManager.getLogger(CuentasCorrientesHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(CuentasCorrientes instance) {
        log.debug(marker, "persisting CuentasCorrientes instance");
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
    public List<CuentasCorrientes> displayRecords() {
        log.debug(marker, "retrieving CuentasCorrientes list");
        List<CuentasCorrientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.CuentasCorrientes CC where CC.deleted = false").list();
            tx.commit();
            log.debug(marker, "retrieve successful, result size: " + list.size());
            log.debug(marker, "Initializing lazy loaded");
            for (CuentasCorrientes cc : list)
                Hibernate.initialize(cc.getPropietarios());
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
    public List<CuentasCorrientes> displayDeletedRecords() {
        log.debug(marker, "retrieving CuentasCorrientes list");
        List<CuentasCorrientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.CuentasCorrientes CC where CC.deleted = true").list();
            tx.commit();
            log.debug(marker, "retrieve successful, result size: " + list.size());
            log.debug(marker, "Initializing lazy loaded");
            for (CuentasCorrientes cc : list)
                Hibernate.initialize(cc.getPropietarios());
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
    public List<Propietarios> displayRecordsWithOwners() {
        log.debug(marker, "retrieving CuentasCorrientes list with Propietarios");
        List<Propietarios> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session
                    .createQuery(
                            "from model.Propietarios PO where exists( " + "select 1 from model.CuentasCorrientes CA "
                                    + "where CA.propietarios = PO.id and CA.deleted = 0 and PO.deleted = false)")
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
    public CuentasCorrientes showById(Integer id) {
        log.debug(marker, "getting CuentasCorrientes instance with id: " + id);
        CuentasCorrientes instance;
        Session session = sessionFactory.openSession();
        Query<CuentasCorrientes> query = session
                .createQuery("from model.CuentasCorrientes CC where CC.id = :id and CC.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        session.close();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<CuentasCorrientes> showByOwner(Propietarios id) {
        log.debug(marker, "retrieving CuentasCorrientes (by Propietarios) list");
        List<CuentasCorrientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query<CuentasCorrientes> query = session
                    .createQuery("from model.CuentasCorrientes CA where CA.propietarios = :id and CA.deleted = false");
            query.setParameter("id", id);
            list = query.list();
            for (CuentasCorrientes cuentaCorriente : list) {
                Propietarios po = cuentaCorriente.getPropietarios();
                Hibernate.initialize(po);
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

    public void update(CuentasCorrientes instance) {
        log.debug(marker, "updating CuentasCorrientes instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "CuentasCorrientes instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(CuentasCorrientes instance) {
        log.debug("attaching clean CuentasCorrientes instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Integer id) {
        log.debug("deleting CuentasCorrientes instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        CuentasCorrientes instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(CuentasCorrientes.class, id);
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
        log.debug("deleting CuentasCorrientes by Owner ");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery("UPDATE model.CuentasCorrientes cc "
                + "SET cc.deleted = true, cc.deletedAt = now() WHERE cc.propietarios = " + id);
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
        Query query = session.createQuery("UPDATE model.CuentasCorrientes cc "
                + "SET cc.deleted = false, cc.modifiedAt = now() WHERE cc.id = " + id);
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
