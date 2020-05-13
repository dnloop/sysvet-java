package model;
// Generated Feb 11, 2019 1:52:34 PM by Hibernate Tools 5.3.6.Final

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * HistoriaClinica generated by hbm2java
 */
public class HistoriaClinica extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = -2495196227762558509L;
    private Integer id;
    @NotNull(message = "El campo 'paciente (Ficha)' es requerido.")
    private FichasClinicas fichasClinicas;
    @NotEmpty(message = "El campo 'Descripción' es requerido.")
    @Size(max = 65535, message = "El campo 'Descripción' no debe ser mayor a {max} caracteres.")
    private String descripcionEvento;
    @NotNull(message = "El campo 'Fecha Inicio' es requerido.")
    private Date fechaInicio;
    @NotNull(message = "El campo 'Fecha Resolución' es requerido.")
    private Date fechaResolucion;
    @Size(max = 500, message = "El campo 'resultado' no debe ser mayor a {max} caracteres.")
    private String resultado;
    @Size(max = 500, message = "El campo 'secuela' no debe ser mayor a {max} caracteres.")
    private String secuelas;
    @Size(max = 500, message = "El campo 'consideraciones' no debe ser mayor a {max} caracteres.")
    private String consideraciones;
    @Size(max = 65535, message = "El campo 'comentarios' no debe ser mayor a {max} caracteres.")
    private String comentarios;
    private boolean deleted;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    public HistoriaClinica() {
    }

    public HistoriaClinica(FichasClinicas fichasClinicas, String descripcionEvento, Date fechaInicio, boolean deleted) {
        this.fichasClinicas = fichasClinicas;
        this.descripcionEvento = descripcionEvento;
        this.deleted = deleted;
    }

    public HistoriaClinica(
            FichasClinicas fichasClinicas, String descripcionEvento, Date fechaInicio, Date fechaResolucion,
            String resultado, String secuelas, String consideraciones, String comentarios, boolean deleted,
            Date createdAt, Date updatedAt, Date deletedAt
    ) {
        super();
        this.fichasClinicas = fichasClinicas;
        this.descripcionEvento = descripcionEvento;
        this.fechaInicio = fechaInicio;
        this.fechaResolucion = fechaResolucion;
        this.resultado = resultado;
        this.secuelas = secuelas;
        this.consideraciones = consideraciones;
        this.comentarios = comentarios;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    @Override
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

    public String getDescripcionEvento() {
        return this.descripcionEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        this.descripcionEvento = descripcionEvento;
    }

    public Date getFechaResolucion() {
        return this.fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getResultado() {
        return this.resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getSecuelas() {
        return this.secuelas;
    }

    public void setSecuelas(String secuelas) {
        this.secuelas = secuelas;
    }

    public String getConsideraciones() {
        return this.consideraciones;
    }

    public void setConsideraciones(String consideraciones) {
        this.consideraciones = consideraciones;
    }

    public String getComentarios() {
        return this.comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

}
