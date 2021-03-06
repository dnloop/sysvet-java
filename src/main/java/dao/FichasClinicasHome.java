package dao;
// Generated Feb 11, 2019 1:57:02 PM by Hibernate Tools 5.3.6.Final

import java.sql.Date;
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
import model.FichasClinicas;
import model.Pacientes;
import utils.HibernateUtil;

/**
 * Home object for domain model class FichasClinicas.
 *
 * @see dao.FichasClinicas
 * @author Hibernate Tools
 */
public class FichasClinicasHome implements Dao<FichasClinicas> {

    protected static final Logger log = (Logger) LogManager.getLogger(FichasClinicasHome.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public void add(FichasClinicas instance) {
        log.debug(marker, "persisting FichasClinicas instance");
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
    public Task<List<FichasClinicas>> displayRecords() {
        log.debug(marker, "retrieving FichasClinicas list");
        return new Task<List<FichasClinicas>>() {
            @Override
            protected List<FichasClinicas> call() throws Exception {
                updateMessage("Cargando listado de Fichas Clínicas.");
                List<FichasClinicas> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("from model.FichasClinicas FC where FC.deleted = false").list();
                    for (FichasClinicas fichasClinicas : list) {
                        Hibernate.initialize(fichasClinicas.getPacientes());
                        Hibernate.initialize(fichasClinicas.getHistoriaClinicas());
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
                log.debug("Canceled Query: [ ClinicalFiles - list ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ ClinicalFiles - list ]");
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Task<List<FichasClinicas>> displayDeletedRecords() {
        log.debug(marker, "retrieving FichasClinicas list");
        return new Task<List<FichasClinicas>>() {
            @Override
            protected List<FichasClinicas> call() throws Exception {
                updateMessage("Cargando listado de fichas clínicas eliminadas.");
                List<FichasClinicas> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session.createQuery("from model.FichasClinicas FC where FC.deleted = true").list();
                    for (FichasClinicas fichasClinicas : list) {
                        Hibernate.initialize(fichasClinicas.getPacientes());
                        Hibernate.initialize(fichasClinicas.getHistoriaClinicas());
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
                log.debug("Canceled Query: [ ClinicalFiles - deletedList ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ ClinicalFiles - deletedList ]");
            }
        };
    }

    @SuppressWarnings("unchecked")
    public Task<List<Pacientes>> displayRecordsWithPatients() {
        log.debug(marker, "retrieving FichasClinicas list with Pacientes");
        return new Task<List<Pacientes>>() {
            @Override
            protected List<Pacientes> call() throws Exception {
                updateMessage("Cargando fichas clínicas del paciente.");
                List<Pacientes> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    list = session
                            .createQuery("select distinct FC.pacientes from model.FichasClinicas FC" + " where exists("
                                    + "select 1 from model.Pacientes PA "
                                    + "where FC.pacientes = PA.id and FC.deleted = false and PA.deleted = false)")
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
                log.debug("Canceled Query: [ ClinicalFile - Patient ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ ClinicalFile - Patient ]");
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public FichasClinicas showById(Integer id) {
        log.debug(marker, "getting FichasClinicas instance with id: " + id);
        FichasClinicas instance;
        Session session = sessionFactory.openSession();
        Query<FichasClinicas> query = session
                .createQuery("from model.FichasClinicas FC where FC.id = :id and FC.deleted = false");
        query.setParameter("id", id);
        instance = query.uniqueResult();
        session.close();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public Task<List<FichasClinicas>> showByPatient(Pacientes id) {
        log.debug(marker, "retrieving FichasClinicas (by Pacientes) list");
        return new Task<List<FichasClinicas>>() {
            @Override
            protected List<FichasClinicas> call() throws Exception {
                updateMessage("Cargando fichas clínicas del paciente.");
                List<FichasClinicas> list = new ArrayList<>();
                Transaction tx = null;
                Session session = sessionFactory.openSession();
                try {
                    tx = session.beginTransaction();
                    Query<FichasClinicas> query = session.createQuery(
                            "from model.FichasClinicas FC where FC.pacientes = :id and FC.deleted = false");
                    query.setParameter("id", id);
                    list = query.list();
                    for (FichasClinicas fichas : list) {
                        Pacientes pa = fichas.getPacientes();
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
                log.debug("Canceled Query: [ Patient - ClinicalFile ]");
            }

            @Override
            protected void failed() {
                updateMessage("Consulta fallida.");
                log.debug("Query Failed: [ Patient - ClinicalFile ]");
            }
        };
    }

    // TODO performance check && Task
    @SuppressWarnings("unchecked")
    public List<FichasClinicas> showByPatientBeetween(Pacientes id, Date start, Date end) {
        log.debug(marker, "retrieving FichasClinicas (by Pacientes) list");
        List<FichasClinicas> list = new ArrayList<>();
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query<FichasClinicas> query = session.createQuery(
                    "from model.FichasClinicas FC where FC.pacientes = :id and FC.fecha between :start and :end and FC.deleted = false");
            query.setParameter("id", id);
            query.setParameter("start", start);
            query.setParameter("end", end);
            list = query.list();
            for (FichasClinicas fichas : list) {
                Pacientes pa = fichas.getPacientes();
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
    public void update(FichasClinicas instance) {
        log.debug(marker, "updating FichasClinicas instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(instance);
            tx.commit();
            log.debug(marker, "FichasClinicas instance updated");
        } catch (RuntimeException re) {
            if (tx != null)
                tx.rollback();
            log.error("update failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachClean(FichasClinicas instance) {
        log.debug("attaching clean FichasClinicas instance");
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
        log.debug("deleting FichasClinicas instance");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        FichasClinicas instance;
        try {
            tx = session.beginTransaction();
            instance = session.load(FichasClinicas.class, id);
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
        log.debug("deleting FichasClinicas by Patient");
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery("UPDATE model.FichasClinicas fc "
                + "SET fc.deleted = true, fc.deletedAt = now() WHERE fc.pacientes = " + id);
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
                "UPDATE model.FichasClinicas fc " + "SET fc.deleted = false, fc.updatedAt = now() WHERE fc.id = " + id);
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
