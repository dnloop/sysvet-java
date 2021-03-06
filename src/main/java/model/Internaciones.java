package model;
// Generated Feb 11, 2019 1:52:34 PM by Hibernate Tools 5.3.6.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * Internaciones generated by hbm2java
 */
public class Internaciones extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = -3932240188589889189L;
    private Integer id;
    @NotNull(message = "El campo 'paciente (Ficha)' es requerido.")
    private Pacientes pacientes;
    @NotNull(message = "El campo 'fecha de ingreso' es requerido.")
    private Date fechaIngreso;
    private Date fechaAlta;
    private boolean deleted;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Set<Tratamientos> tratamientoses = new HashSet<Tratamientos>(0);

    public Internaciones() {
    }

    public Internaciones(Pacientes pacientes, Date fechaIngreso, boolean deleted) {
        this.pacientes = pacientes;
        this.fechaIngreso = fechaIngreso;
        this.deleted = deleted;
    }

    public Internaciones(Pacientes pacientes, Date fechaIngreso, Date fechaAlta, boolean deleted, Date createdAt,
            Date updatedAt, Date deletedAt, Set<Tratamientos> tratamientoses) {
        super();
        this.pacientes = pacientes;
        this.fechaIngreso = fechaIngreso;
        this.fechaAlta = fechaAlta;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.tratamientoses = tratamientoses;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pacientes getPacientes() {
        return this.pacientes;
    }

    public void setPacientes(Pacientes pacientes) {
        this.pacientes = pacientes;
    }

    public Date getFechaIngreso() {
        return this.fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaAlta() {
        return this.fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Override
    public boolean isDeleted() {
        return this.deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Date getDeletedAt() {
        return this.deletedAt;
    }

    @Override
    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<Tratamientos> getTratamientoses() {
        return this.tratamientoses;
    }

    public void setTratamientoses(Set<Tratamientos> tratamientoses) {
        this.tratamientoses = tratamientoses;
    }
}
