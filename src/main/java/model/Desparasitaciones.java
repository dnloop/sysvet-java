package model;
// Generated Feb 11, 2019 1:52:34 PM by Hibernate Tools 5.3.6.Final

import java.util.Date;

/**
 * Desparasitaciones generated by hbm2java
 */

public class Desparasitaciones implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Pacientes pacientes;
    private Date fecha;
    private String tratamiento;
    private Date fechaProxima;
    private String tipo;
    private boolean deleted;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    public Desparasitaciones() {
    }

    public Desparasitaciones(Date fecha, Date fechaProxima, String tipo, boolean deleted) {
        this.fecha = fecha;
        this.fechaProxima = fechaProxima;
        this.tipo = tipo;
        this.deleted = deleted;
    }

    public Desparasitaciones(Pacientes pacientes, Date fecha, String tratamiento, Date fechaProxima, String tipo,
            boolean deleted, Date createdAt, Date updatedAt, Date deletedAt) {
        this.pacientes = pacientes;
        this.fecha = fecha;
        this.tratamiento = tratamiento;
        this.fechaProxima = fechaProxima;
        this.tipo = tipo;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

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

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTratamiento() {
        return this.tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Date getFechaProxima() {
        return this.fechaProxima;
    }

    public void setFechaProxima(Date fechaProxima) {
        this.fechaProxima = fechaProxima;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

}
