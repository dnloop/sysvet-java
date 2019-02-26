package model;
// Generated Feb 11, 2019 1:52:34 PM by Hibernate Tools 5.3.6.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Internaciones generated by hbm2java
 */
public class Internaciones implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3932240188589889189L;
    private Integer id;
    private FichasClinicas fichasClinicas;
    private Date fechaIngreso;
    private Date fechaAlta;
    private boolean deleted;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Set<Tratamientos> tratamientoses = new HashSet<Tratamientos>(0);

    public Internaciones() {
    }

    public Internaciones(FichasClinicas fichasClinicas, Date fechaIngreso, boolean deleted) {
        this.fichasClinicas = fichasClinicas;
        this.fechaIngreso = fechaIngreso;
        this.deleted = deleted;
    }

    public Internaciones(FichasClinicas fichasClinicas, Date fechaIngreso, Date fechaAlta, Date createdAt,
            boolean deleted, Date updatedAt, Date deletedAt, Set<Tratamientos> tratamientoses) {
        this.fichasClinicas = fichasClinicas;
        this.fechaIngreso = fechaIngreso;
        this.fechaAlta = fechaAlta;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.tratamientoses = tratamientoses;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FichasClinicas getFichasClinicas() {
        return this.fichasClinicas;
    }

    public void setFichasClinicas(FichasClinicas fichasClinicas) {
        this.fichasClinicas = fichasClinicas;
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

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return this.deletedAt;
    }

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
