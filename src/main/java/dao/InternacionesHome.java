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
import model.Internaciones;
import model.Pacientes;
import utils.HibernateUtil;

/**
 * Home object for domain model class Internaciones.
 *
 * @see dao.Internaciones
 * @author Hibernate Tools
 */
public class InternacionesHome implements Dao<Internaciones> {

    protected static final Logger log = (Logger) LogManager.getLogger(InternacionesHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
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

    @Override
    @SuppressWarnings("unchecked")
    public Task<List<Internaciones>> displayRecords() {
        log.debug(marker, "retrieving Internaciones list");
        return new Task<List<Internaciones>>() {
            @Override
            protected List<Internaciones> call() throws Exception {
                updateMessage("Cargando listado completo de desparasitaciones.");
                Thread.sleep(1000);
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

            @Override
            protected void cancelled() {
                updateMessage("Consulta Cancelada.");
                log.debug("Canceled Query: [ Internations - list ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Internations- list ]");
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Task<List<Internaciones>> displayDeletedRecords() {
        log.debug(marker, "retrieving Internaciones list");
        return new Task<List<Internaciones>>() {
            @Override
            protected List<Internaciones> call() throws Exception {
                updateMessage("Cargando listado de internaciones eliminadas.");
                Thread.sleep(1000);
                List<Internaciones> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("from model.Internaciones I where I.deleted = true").list();
                    for (Internaciones internacion : list)
                        Hibernate.initialize(internacion.getPacientes());
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
                log.debug("Canceled Query: [ Internations - deletedList ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Internations - deletedList ]");
            }
        };
    }

    @SuppressWarnings("unchecked")
    public List<Pacientes> displayRecordsWithTreatments() {
        log.debug(marker, "retrieving Internaciones list with Tratamientos");
        List<Pacientes> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            list = session.createQuery(
                    "select I.pacientes from model.Internaciones I" + "where exists( select 1 from model.Tratamientos T"
                            + "    where I.id = T.id and I.deleted = false and T.deleted = false )")
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
    public Task<List<Pacientes>> displayRecordsWithPatients() {
        log.debug(marker, "retrieving Internaciones list with Pacientes");
        return new Task<List<Pacientes>>() {
            @Override
            protected List<Pacientes> call() throws Exception {
                updateMessage("Cargando listado completo de Internaciones.");
                Thread.sleep(1000);
                List<Pacientes> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("select I.pacientes from model.Internaciones I" + " where exists("
                            + "select 1 from model.Pacientes PA "
                            + "where I.id = PA.id and I.deleted = false and PA.deleted = false)").list();
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
                log.debug("Canceled Query: [ Internations - Patients ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Internations - Patients]");
            }
        };
    }

    @Override
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
    public Task<List<Internaciones>> showByPatient(Pacientes id) {
        log.debug(marker, "retrieving Internaciones (by Ficha.pacientes) list");
        return new Task<List<Internaciones>>() {
            @Override
            protected List<Internaciones> call() throws Exception {
                updateMessage("Cargando internaciones del paciente.");
                Thread.sleep(1000);
                List<Internaciones> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    Query<Internaciones> query = session
                            .createQuery("from model.Internaciones I where I.pacientes = :id and I.deleted = false");
                    query.setParameter("id", id);
                    list = query.list();
                    for (Internaciones internacion : list)
                        Hibernate.initialize(internacion.getPacientes());
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
                log.debug("Canceled Query: [ Patient - Internations ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Patient - Internations ]");
            }
        };
    }

    @Override
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
                + "SET i.deleted = true, i.deletedAt = now() WHERE i.pacientes = " + id);
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

    @Override
    public void recover(Integer id) {
        log.debug("recovering register");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery(
                "UPDATE model.Internaciones i " + "SET i.deleted = false, i.updatedAt = now() WHERE i.id = " + id);
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
