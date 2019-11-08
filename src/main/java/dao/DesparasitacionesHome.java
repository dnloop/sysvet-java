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
import model.Desparasitaciones;
import model.Pacientes;
import utils.HibernateUtil;

/**
 * Home object for domain model class Desparasitaciones.
 *
 * @see dao.Desparasitaciones
 * @author Hibernate Tools
 */
public class DesparasitacionesHome implements Dao<Desparasitaciones> {

    protected static final Logger log = (Logger) LogManager.getLogger(DesparasitacionesHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
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

    @Override
    @SuppressWarnings("unchecked")
    public Task<List<Desparasitaciones>> displayRecords() {
        log.debug(marker, "retrieving Desparasitaciones list");
        return new Task<List<Desparasitaciones>>() {
            @Override
            protected List<Desparasitaciones> call() throws Exception {
                updateMessage("Cargando listado completo de desparasitaciones.");
                List<Desparasitaciones> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("from model.Desparasitaciones D where D.deleted = false").list();
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

            @Override
            protected void cancelled() {
                updateMessage("Consulta Cancelada.");
                log.debug("Canceled Query: [ Deworming - list ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Deworming - list ]");
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Task<List<Desparasitaciones>> displayDeletedRecords() {
        log.debug(marker, "retrieving Desparasitaciones list");
        return new Task<List<Desparasitaciones>>() {
            @Override
            protected List<Desparasitaciones> call() throws Exception {
                updateMessage("Cargando listado de desparasitaciones eliminadas.");
                List<Desparasitaciones> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("from model.Desparasitaciones D where D.deleted = true").list();
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

            @Override
            protected void cancelled() {
                updateMessage("Consulta Cancelada.");
                log.debug("Canceled Query: [ Deworming - deletedList ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Deworming - deletedList ]");
            }
        };
    }

    @SuppressWarnings("unchecked")
    public Task<List<Pacientes>> displayRecordsWithPatients() {
        log.debug(marker, "retrieving Desparasitaciones list with Pacientes");
        return new Task<List<Pacientes>>() {
            @Override
            protected List<Pacientes> call() throws Exception {
                updateMessage("Cargando listado de pacientes con desparasitaciones.");
                List<Pacientes> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session
                            .createQuery("select D.pacientes from model.Desparasitaciones D" + " where exists("
                                    + "select 1 from model.Pacientes PA where D.id = PA.id and D.deleted = false)")
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
                log.debug("Canceled Query: [ Deworming - Patients ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Deworming - Patients ]");
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Desparasitaciones showById(Integer id) {
        log.debug(marker, "getting Desparasitaciones instance with id: " + id);
        Desparasitaciones instance;
        Session session = sessionFactory.openSession();
        Query<Desparasitaciones> query = session
                .createQuery("from model.Desparasitaciones D where D.id = :id and D.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        session.close();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public Task<List<Desparasitaciones>> showByPatient(Pacientes id) {
        log.debug(marker, "retrieving Desparasitaciones (by Pacientes) list");
        return new Task<List<Desparasitaciones>>() {
            @Override
            protected List<Desparasitaciones> call() throws Exception {
                updateMessage("Cargando desparasitaciones del paciente.");
                List<Desparasitaciones> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    Query<Desparasitaciones> query = session.createQuery(
                            "from model.Desparasitaciones D where D.pacientes = :id and D.deleted = false");
                    query.setParameter("id", id);
                    list = query.list();
                    for (Desparasitaciones desparasitacion : list) {
                        Pacientes pa = desparasitacion.getPacientes();
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
                log.debug("Canceled Query: [ Patient - Deworming ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Patient - Deworming ]");
            }
        };
    }

    @Override
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

    @Override
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

    public void deleteAll(Integer id) {
        log.debug("deleting Desparasitaciones by paciente ");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery("UPDATE model.Desparasitaciones d "
                + "SET d.deleted = true, d.deletedAt = now() WHERE d.pacientes = " + id);
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
        Query query = session.createQuery(
                "UPDATE model.Desparasitaciones d " + "SET d.deleted = false, d.updatedAt = now() WHERE d.id = " + id);
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
