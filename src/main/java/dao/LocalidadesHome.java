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

import javafx.concurrent.Task;
import model.Localidades;
import model.Provincias;
import utils.HibernateUtil;

/**
 * Home object for domain model class Localidades.
 *
 * @see dao.Localidades
 * @author Hibernate Tools
 */
public class LocalidadesHome implements Dao<Localidades> {

    private Long totalRecords;

    protected static final Logger log = (Logger) LogManager.getLogger(LocalidadesHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public void add(Localidades instance) {
        log.debug(marker, "persisting Localidades instance");
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

    public void pageCountResult() {
        Session session = sessionFactory.openSession();
        String query = "Select count (L.id) from model.Localidades L where L.deleted = false";
        @SuppressWarnings("rawtypes")
        Query count = session.createQuery(query);
        setTotalRecords((Long) count.uniqueResult());
        session.close();
        log.debug("Total records: " + totalRecords);
    }

    @SuppressWarnings("unchecked")
    public Task<List<Localidades>> displayRecords(Integer page) {
        log.debug(marker, "retrieving Localidades list");
        return new Task<List<Localidades>>() {
            @Override
            protected List<Localidades> call() throws Exception {
                updateMessage("Cargando localidades. Página: " + Integer.toString(page == 0 ? 1 : page));
                Thread.sleep(1000);
                List<Localidades> list = new ArrayList<Localidades>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                Query<Localidades> selectQuery = session
                        .createQuery("from model.Localidades L where L.deleted = false");
                try {
                    tx = session.beginTransaction();

                    selectQuery.setFirstResult(page * 100);
                    selectQuery.setMaxResults(180);
                    list.addAll(selectQuery.list());

                    for (Localidades localidades : list)
                        Hibernate.initialize(localidades.getProvincias());
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
                log.debug("Canceled Query: [ Location - list ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Location - list ]");
            }
        };
    }

    public void pageCountDeletedResult() {
        Session session = sessionFactory.openSession();
        String query = "Select count (L.id) from model.Localidades L where L.deleted = true";
        @SuppressWarnings("rawtypes")
        Query count = session.createQuery(query);
        setTotalRecords((Long) count.uniqueResult());
        session.close();
        log.debug("Total records: " + totalRecords);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Localidades showById(Integer id) {
        log.debug(marker, "getting Localidades instance with id: " + id);
        Localidades instance;
        Session session = sessionFactory.openSession();
        Query<Localidades> query = session
                .createQuery("from model.Localidades L where L.id = :id and L.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        session.close();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public Task<List<Localidades>> showByProvincia(Provincias id) {
        log.debug(marker, "retrieving Localidades (by Provincias) list");
        return new Task<List<Localidades>>() {
            @Override
            protected List<Localidades> call() throws Exception {
                updateMessage("Cargando cuenta corriente del propietario.");
                Thread.sleep(1000);
                List<Localidades> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    Query<Localidades> query = session
                            .createQuery("from model.Localidades LC where LC.provincias = :id and LC.deleted = false");
                    query.setParameter("id", id);
                    list = query.list();
                    for (Localidades localidad : list) {
                        Provincias lc = localidad.getProvincias();
                        Hibernate.initialize(lc);
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
                log.debug("Canceled Query: [ Province - Location ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Province - Location ]");
            }
        };
    }

    @Override
    public void update(Localidades instance) {
        log.debug(marker, "updating Localidades instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "Localidades instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(Localidades instance) {
        log.debug("attaching clean Localidades instance");
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
        log.debug("deleting Localidades instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        Localidades instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(Localidades.class, id);
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
                "UPDATE model.Localidades lc " + "SET lc.deleted = false, lc.updatedAt = now() WHERE lc.id = " + id);
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

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Task<List<Localidades>> displayRecords() {
        log.debug(marker, "retrieving Localidades list");
        return new Task<List<Localidades>>() {
            @Override
            protected List<Localidades> call() throws Exception {
                updateMessage("Cargando listado completo de Localidades.");
                Thread.sleep(1000);
                List<Localidades> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("from model.Localidades L where L.deleted = false").list();
                    for (Localidades localidades : list)
                        Hibernate.initialize(localidades.getProvincias());
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
                log.debug("Canceled Query: [ Location - list ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Location - list ]");
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public Task<List<Localidades>> displayDeletedRecords() {
        log.debug(marker, "retrieving Localidades list");
        return new Task<List<Localidades>>() {
            @Override
            protected List<Localidades> call() throws Exception {
                updateMessage("Cargando Localidades eliminadas.");
                Thread.sleep(1000);
                List<Localidades> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("from model.Localidades L where L.deleted = true").list();
                    for (Localidades localidades : list)
                        Hibernate.initialize(localidades.getProvincias());
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
                log.debug("Canceled Query: [ Location - list ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Location - list ]");
            }
        };
    }

    @SuppressWarnings("unchecked")
    public Task<List<Localidades>> displayDeletedRecords(Integer page) {
        log.debug(marker, "retrieving Localidades list");
        return new Task<List<Localidades>>() {
            @Override
            protected List<Localidades> call() throws Exception {
                updateMessage("Cargando localidades eliminadas. Página: " + Integer.toString(page == 0 ? 1 : page));
                Thread.sleep(1000);
                List<Localidades> list = new ArrayList<Localidades>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                Query<Localidades> selectQuery = session.createQuery("from model.Localidades L where L.deleted = true");
                try {
                    tx = session.beginTransaction();

                    selectQuery.setFirstResult(page * 100);
                    selectQuery.setMaxResults(180);
                    list.addAll(selectQuery.list());

                    for (Localidades localidades : list)
                        Hibernate.initialize(localidades.getProvincias());
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
                log.debug("Canceled Query: [ Location - list ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Location - list ]");
            }
        };
    }
}
