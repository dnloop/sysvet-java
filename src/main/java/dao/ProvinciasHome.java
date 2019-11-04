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

import javafx.concurrent.Task;
import model.Provincias;
import utils.HibernateUtil;

/**
 * Home object for domain model class Provincias.
 *
 * @see dao.Provincias
 * @author Hibernate Tools
 */
public class ProvinciasHome implements Dao<Provincias> {

    protected static final Logger log = (Logger) LogManager.getLogger(ProvinciasHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public void add(Provincias instance) {
        log.debug(marker, "persisting Provincias instance");
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

    @Override
    @SuppressWarnings("unchecked")
    public Task<List<Provincias>> displayRecords() {
        log.debug(marker, "retrieving Provincias list");
        return new Task<List<Provincias>>() {
            @Override
            protected List<Provincias> call() throws Exception {
                updateMessage("Cargando listado completo de provincias.");
                Thread.sleep(1000);
                List<Provincias> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("from model.Provincias P where P.deleted = false").list();
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

            @Override
            protected void cancelled() {
                updateMessage("Consulta Cancelada.");
                log.debug("Canceled Query: [ Province - list ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Province- list ]");
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Provincias> displayDeletedRecords() {
        log.debug(marker, "retrieving Provincias list");
        List<Provincias> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Provincias P where P.deleted = true").list();
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

    @Override
    @SuppressWarnings("unchecked")
    public Provincias showById(Integer id) {
        log.debug(marker, "getting Provincias instance with id: " + id);
        Provincias instance;
        Session session = sessionFactory.openSession();
        Query<Provincias> query = session.createQuery("from model.Provincias P where P.id = :id and P.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        return instance;
    }

    @Override
    public void update(Provincias instance) {
        log.debug(marker, "updating Provincias instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Provincias instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(Provincias instance) {
        log.debug("attaching clean Provincias instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    @Override
    public void delete(Integer id) {
        log.debug("deleting Provincias instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Provincias instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(Provincias.class, id);
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

    @Override
    public void recover(Integer id) {
        log.debug("recovering register");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery(
                "UPDATE model.Provincias pr " + "SET pr.deleted = false, pr.updatedAt = now() WHERE pr.id = " + id);
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
