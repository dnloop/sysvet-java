package dao;
// Generated Dec 3, 2019, 12:18:31 PM by Hibernate Tools 4.3.5.Final

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

import javafx.concurrent.Task;
import model.Entrega;
import model.Propietarios;
import utils.HibernateUtil;

/**
 * Home object for domain model class Entrega.
 *
 * @see dao.Entrega
 * @author Hibernate Tools
 */
public class EntregaHome implements Dao<Entrega> {

    protected static final Logger log = (Logger) LogManager.getLogger(EntregaHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public void add(Entrega instance) {
        log.debug(marker, "persisting Entrega instance");
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
    public Task<List<Entrega>> displayRecords() {
        log.debug(marker, "retrieving Entrega list");
        return new Task<List<Entrega>>() {
            @Override
            protected List<Entrega> call() throws Exception {
                updateMessage("Cargando listado completo de entregas.");
                List<Entrega> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("from model.Entrega E where E.deleted = false").list();
                    tx.commit();
                    for (Entrega cc : list)
                        Hibernate.initialize(cc.getPropietarios());
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
                log.debug("Canceled Query: [ Payment - list ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Payment - list ]");
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Task<List<Entrega>> displayDeletedRecords() {
        log.debug(marker, "retrieving Entrega list");
        return new Task<List<Entrega>>() {
            @Override
            protected List<Entrega> call() throws Exception {
                updateMessage("Cargando listado de entregas eliminadas.");
                List<Entrega> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("from model.Entrega E where E.deleted = true").list();
                    tx.commit();
                    for (Entrega cc : list)
                        Hibernate.initialize(cc.getPropietarios());
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
                log.debug("Canceled Query: [ Payment - deletedList ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Payment - deletedList ]");
            }
        };
    }

    @SuppressWarnings("unchecked")
    public Task<List<Propietarios>> displayRecordsWithOwners() {
        log.debug(marker, "retrieving Entrega list with Propietarios");
        return new Task<List<Propietarios>>() {
            @Override
            protected List<Propietarios> call() throws Exception {
                updateMessage("Cargando listado de propietarios con entregas.");
                List<Propietarios> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session
                            .createQuery("select E.propietarios from model.Entrega E" + " where exists("
                                    + "select 1 from model.Propietarios O "
                                    + "where E.id = O.id and E.deleted = false and E.propietarios.deleted = false)")
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

            @Override
            protected void cancelled() {
                updateMessage("Consulta Cancelada.");
                log.debug("Canceled Query: [ Payment - Owners ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Payment - Owners ]");
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Entrega showById(Integer id) {
        log.debug(marker, "getting Entrega instance with id: " + id);
        Entrega instance;
        Session session = sessionFactory.openSession();
        Query<Entrega> query = session.createQuery("from model.Entrega E where E.id = :id and E.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        session.close();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public Task<List<Entrega>> showByOwner(Propietarios id) {
        log.debug(marker, "retrieving Entrega (by Propietarios) list");
        return new Task<List<Entrega>>() {
            @Override
            protected List<Entrega> call() throws Exception {
                updateMessage("Cargando entregas del propietario.");
                List<Entrega> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    Query<Entrega> query = session
                            .createQuery("from model.Entrega E where E.propietarios = :id and E.deleted = false");
                    query.setParameter("id", id);
                    list = query.list();
                    for (Entrega entrega : list) {
                        Propietarios pa = entrega.getPropietarios();
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

            @Override
            protected void cancelled() {
                updateMessage("Consulta Cancelada.");
                log.debug("Canceled Query: [ Owner - Payment ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Owner - Payment ]");
            }
        };
    }

    @Override
    public void update(Entrega instance) {
        log.debug(marker, "updating Entrega instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Entrega instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(Entrega instance) {
        log.debug("attaching clean Entrega instance");
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
        log.debug("deleting Entrega instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Entrega instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(Entrega.class, id);
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
        log.debug("deleting Entrega by propietario ");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery(
                "UPDATE model.Entrega E SET E.deleted = true, E.deletedAt = now() WHERE E.propietarios = " + id);
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

    @Override
    public void recover(Integer id) {
        log.debug("recovering register");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session
                .createQuery("UPDATE model.Entrega E SET E.deleted = false, E.updatedAt = now() WHERE E.id = " + id);
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