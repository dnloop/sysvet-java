package model;
// Generated Feb 11, 2019 1:52:34 PM by Hibernate Tools 5.3.6.Final

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

/**
 * CuentasCorrientes generated by hbm2java
 */

public class CuentasCorrientes extends RecursiveTreeObject<CuentasCorrientes> implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -215738452477748132L;
    private Integer id;
    @NotNull(message = "El campo 'propietarios' es obligatorio.")
    private Propietarios propietarios;
    @NotEmpty(message = "El campo 'descripción' es obligatorio.")
    @Size(min = 2, max = 191, message = "La descripción debe ser entre {min} y {max} caracteres.")
    private String descripcion;
    @NotNull(message = "El campo 'monto' es obligatorio")
    @DecimalMax(value = "1000000", message = "El monto ${formatter.format('%1$.2f', validatedValue)} es mayor "
            + "que {value}")
    private BigDecimal monto;
    @NotNull(message = "El campo 'fecha' es obligatorio.")
    private Date fecha;
    private boolean deleted;
    private Date deletedAt;
    private Date createdAt;
    private Date updatedAt;

    public CuentasCorrientes() {
    }

    public CuentasCorrientes(Propietarios propietarios, String descripcion, BigDecimal monto, Date fecha,
            boolean deleted) {
        this.propietarios = propietarios;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
        this.deleted = deleted;
    }

    public CuentasCorrientes(Propietarios propietarios, String descripcion, BigDecimal monto, Date fecha,
            boolean deleted, Date deletedAt, Date createdAt, Date updatedAt) {
        this.propietarios = propietarios;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Propietarios getPropietarios() {
        return this.propietarios;
    }

    public void setPropietarios(Propietarios propietarios) {
        this.propietarios = propietarios;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
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

}
