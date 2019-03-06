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

import model.Desparasitaciones;
import utils.HibernateUtil;

/**
 * Home object for domain model class Desparasitaciones.
 * 
 * @see dao.Desparasitaciones
 * @author Hibernate Tools
 */
public class DesparasitacionesHome {

    protected static final Logger log = (Logger) LogManager.getLogger(DesparasitacionesHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void add(Desparasitaciones instance) {
        log.debug(marker, "persisting Desparasitaciones instance");
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
    public List<Desparasitaciones> displayRecords() {
        log.debug(marker, "retrieving Desparasitaciones list");
        List<Desparasitaciones> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery("from model.Desparasitaciones D where deleted = false").list();
            tx.commit();
            for (Desparasitaciones cc : list)
                Hibernate.initialize(cc.getPacientes());
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
    public Desparasitaciones showById(long id) {
        log.debug(marker, "getting Desparasitaciones instance with id: " + id);
        Desparasitaciones instance;
        Session session = sessionFactory.openSession();
        Query<Desparasitaciones> query = session.createQuery("from model.Desparasitaciones D where D.id = :id");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        return instance;
    }

    public void update(Desparasitaciones instance) {
        log.debug(marker, "updating Desparasitaciones instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Desparasitaciones instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(Desparasitaciones instance) {
        log.debug("attaching clean Desparasitaciones instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Integer id) {
        log.debug("deleting Desparasitaciones instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Desparasitaciones instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(Desparasitaciones.class, id);
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
