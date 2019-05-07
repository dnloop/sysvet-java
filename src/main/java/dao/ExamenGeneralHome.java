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

import model.ExamenGeneral;
import model.FichasClinicas;
import utils.HibernateUtil;

/**
 * Home object for domain model class ExamenGeneral.
 *
 * @see dao.ExamenGeneral
 * @author Hibernate Tools
 */
public class ExamenGeneralHome {

    protected static final Logger log = (Logger) LogManager.getLogger(ExamenGeneralHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(ExamenGeneral instance) {
        log.debug(marker, "persisting ExamenGeneral instance");
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
    public List<ExamenGeneral> displayRecords() {
        log.debug(marker, "retrieving ExamenGeneral list");
        List<ExamenGeneral> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.ExamenGeneral EX where EX.deleted = false").list();
            for (ExamenGeneral examenGeneral : list)
                Hibernate.initialize(examenGeneral.getFichasClinicas());
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
    public ExamenGeneral showById(Integer id) {
        log.debug(marker, "getting ExamenGeneral instance with id: " + id);
        ExamenGeneral instance;
        Session session = sessionFactory.openSession();
        Query<ExamenGeneral> query = session
                .createQuery("from model.ExamenGeneral EX where EX.id = :id and EX.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        session.close();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<ExamenGeneral> showByFicha(FichasClinicas id) {
        log.debug(marker, "retrieving ExamenGeneral (by Ficha) list");
        List<ExamenGeneral> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query<ExamenGeneral> query = session
                    .createQuery("from model.ExamenGeneral EX where EX.fichasClinicas = :id and EX.deleted = false");
            query.setParameter("id", id);
            list = query.list();
            for (ExamenGeneral examenGeneral : list) {
                FichasClinicas fc = examenGeneral.getFichasClinicas();
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

    public void update(ExamenGeneral instance) {
        log.debug(marker, "updating ExamenGeneral instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "ExamenGeneral instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(ExamenGeneral instance) {
        log.debug("attaching clean ExamenGeneral instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Integer id) {
        log.debug("deleting ExamenGeneral instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        ExamenGeneral instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(ExamenGeneral.class, id);
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
