package model;
// Generated Feb 11, 2019 1:52:34 PM by Hibernate Tools 5.3.6.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

/**
 * FichasClinicas generated by hbm2java
 */
public class FichasClinicas extends RecursiveTreeObject<FichasClinicas> implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2357760413746833349L;
    private Integer id;
    private Pacientes pacientes;
    private String motivoConsulta;
    private String anamnesis;
    private String medicacionActual;
    private String medicacionAnterior;
    private String estadoNutricion;
    private String estadoSanitario;
    private String aspectoGeneral;
    private String deterDiagComp;
    private String derivaciones;
    private String pronostico;
    private String diagnostico;
    private String exploracion;
    private String evolucion;
    private boolean deleted;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Set<Retornos> retornoses = new HashSet<Retornos>(0);
    private Set<Internaciones> internacioneses = new HashSet<Internaciones>(0);
    private Set<ExamenGeneral> examenGenerals = new HashSet<ExamenGeneral>(0);
    private Set<HistoriaClinica> historiaClinicas = new HashSet<HistoriaClinica>(0);

    public FichasClinicas() {
    }

    public FichasClinicas(Pacientes pacientes, String motivoConsulta, boolean deleted) {
        this.pacientes = pacientes;
        this.motivoConsulta = motivoConsulta;
        this.deleted = deleted;
    }

    public FichasClinicas(Pacientes pacientes, String motivoConsulta, String anamnesis, String medicacionActual,
            String medicacionAnterior, String estadoNutricion, String estadoSanitario, String aspectoGeneral,
            String deterDiagComp, String derivaciones, String pronostico, String diagnostico, String exploracion,
            String evolucion, Date createdAt, Date updatedAt, Date deletedAt, Set<Retornos> retornoses,
            Set<Internaciones> internacioneses, boolean deleted, Set<ExamenGeneral> examenGenerals,
            Set<HistoriaClinica> historiaClinicas) {
        this.pacientes = pacientes;
        this.motivoConsulta = motivoConsulta;
        this.anamnesis = anamnesis;
        this.medicacionActual = medicacionActual;
        this.medicacionAnterior = medicacionAnterior;
        this.estadoNutricion = estadoNutricion;
        this.estadoSanitario = estadoSanitario;
        this.aspectoGeneral = aspectoGeneral;
        this.deterDiagComp = deterDiagComp;
        this.derivaciones = derivaciones;
        this.pronostico = pronostico;
        this.diagnostico = diagnostico;
        this.exploracion = exploracion;
        this.evolucion = evolucion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.retornoses = retornoses;
        this.internacioneses = internacioneses;
        this.examenGenerals = examenGenerals;
        this.historiaClinicas = historiaClinicas;
        this.deleted = deleted;
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

    public String getMotivoConsulta() {
        return this.motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getAnamnesis() {
        return this.anamnesis;
    }

    public void setAnamnesis(String anamnesis) {
        this.anamnesis = anamnesis;
    }

    public String getMedicacionActual() {
        return this.medicacionActual;
    }

    public void setMedicacionActual(String medicacionActual) {
        this.medicacionActual = medicacionActual;
    }

    public String getMedicacionAnterior() {
        return this.medicacionAnterior;
    }

    public void setMedicacionAnterior(String medicacionAnterior) {
        this.medicacionAnterior = medicacionAnterior;
    }

    public String getEstadoNutricion() {
        return this.estadoNutricion;
    }

    public void setEstadoNutricion(String estadoNutricion) {
        this.estadoNutricion = estadoNutricion;
    }

    public String getEstadoSanitario() {
        return this.estadoSanitario;
    }

    public void setEstadoSanitario(String estadoSanitario) {
        this.estadoSanitario = estadoSanitario;
    }

    public String getAspectoGeneral() {
        return this.aspectoGeneral;
    }

    public void setAspectoGeneral(String aspectoGeneral) {
        this.aspectoGeneral = aspectoGeneral;
    }

    public String getDeterDiagComp() {
        return this.deterDiagComp;
    }

    public void setDeterDiagComp(String deterDiagComp) {
        this.deterDiagComp = deterDiagComp;
    }

    public String getDerivaciones() {
        return this.derivaciones;
    }

    public void setDerivaciones(String derivaciones) {
        this.derivaciones = derivaciones;
    }

    public String getPronostico() {
        return this.pronostico;
    }

    public void setPronostico(String pronostico) {
        this.pronostico = pronostico;
    }

    public String getDiagnostico() {
        return this.diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getExploracion() {
        return this.exploracion;
    }

    public void setExploracion(String exploracion) {
        this.exploracion = exploracion;
    }

    public String getEvolucion() {
        return this.evolucion;
    }

    public void setEvolucion(String evolucion) {
        this.evolucion = evolucion;
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

    public Set<Retornos> getRetornoses() {
        return this.retornoses;
    }

    public void setRetornoses(Set<Retornos> retornoses) {
        this.retornoses = retornoses;
    }

    public Set<Internaciones> getInternacioneses() {
        return this.internacioneses;
    }

    public void setInternacioneses(Set<Internaciones> internacioneses) {
        this.internacioneses = internacioneses;
    }

    public Set<ExamenGeneral> getExamenGenerals() {
        return this.examenGenerals;
    }

    public void setExamenGenerals(Set<ExamenGeneral> examenGenerals) {
        this.examenGenerals = examenGenerals;
    }

    public Set<HistoriaClinica> getHistoriaClinicas() {
        return this.historiaClinicas;
    }

    public void setHistoriaClinicas(Set<HistoriaClinica> historiaClinicas) {
        this.historiaClinicas = historiaClinicas;
    }

    @Override
    public String toString() {
        return pacientes.getNombre();
    }

}
