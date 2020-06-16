package model;
// Generated Feb 11, 2019 1:52:34 PM by Hibernate Tools 5.3.6.Final

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * ExamenGeneral generated by hbm2java
 */
public class ExamenGeneral extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = -1219245266662826224L;
    private Integer id;
    @NotNull(message = "El campo 'Paciente' es obligatorio.")
    private Pacientes pacientes;
    @NotNull(message = "El campo 'fecha' es obligatorio.")
    private Date fecha;
    @NotNull(message = "El campo 'peso corporal' es obligatorio.")
    @Min(message = "El valor del peso '${validatedValue}' debe ser como mínimo {value}", value = 1)
    @Max(message = "El valor del peso '${validatedValue}' no debe ser mayor que {value}", value = 1000)
    @Digits(integer = 10, fraction = 0, message = "El peso debe ser un número entero.")
    private int pesoCorporal;
    @NotNull(message = "El campo 'temperatura corporal' es obligatorio.")
    @Min(message = "El valor de la temperatura corporal '${validatedValue}' debe ser como mínimo {value}", value = 1)
    @Max(message = "El valor de la temperatura corporal '${validatedValue}' no debe ser mayor que {value}", value = 200)
    @Digits(integer = 10, fraction = 0, message = "La temperatura corporal debe ser un número entero.")
    private int tempCorporal;
    @NotNull(message = "El campo 'deshidratación' es obligatorio.")
    @Min(message = "El valor de la deshidratación'${validatedValue}' debe ser como mínimo {value}", value = 1)
    @Max(message = "El valor de la deshidratación '${validatedValue}' no debe ser mayor que {value}", value = 100)
    @Digits(integer = 10, fraction = 0, message = "El porcentaje de deshidratación debe ser un número entero.")
    private int deshidratacion;
    @NotNull(message = "El campo 'frecuencia respiratoria' es obligatorio.")
    @Min(message = "El valor de la frecuencia respiratoria '${validatedValue}' debe ser como mínimo {value}", value = 1)
    @Max(
            message = "El valor de la frecuencia respiratoria '${validatedValue}' no debe ser mayor que {value}", value = 100
    )
    @Digits(integer = 10, fraction = 0, message = "La frecuencia respiratoria debe ser un número entero.")
    private int frecResp;
    @NotEmpty(message = "El campo 'amplitud' es obligatorio.")
    @Size(min = 2, max = 200, message = "La amplitud debe ser entre {min} y {max} caracteres.")
    private String amplitud;
    @NotEmpty(message = "El campo 'tipo' es obligatorio.")
    @Size(min = 2, max = 200, message = "El tipo debe ser entre {min} y {max} caracteres.")
    private String tipo;
    @NotEmpty(message = "El campo 'ritmo' es obligatorio.")
    @Size(min = 2, max = 200, message = "El ritmo debe ser entre {min} y {max} caracteres.")
    private String ritmo;
    @NotNull(message = "El campo 'frecuencia cardíaca' es obligatorio.")
    @Min(message = "El valor de la frecuencia cardíaca '${validatedValue}' debe ser como mínimo {value}", value = 1)
    @Max(message = "El valor de la frecuencia cardíaca '${validatedValue}' no debe ser mayor que {value}", value = 300)
    @Digits(integer = 10, fraction = 0, message = "La frecuencia cardíaca debe ser un número entero.")
    private int frecCardio;
    @NotNull(message = "El campo 'pulso' es obligatorio.")
    @Min(message = "El valor del pulso '${validatedValue}' debe ser como mínimo {value}", value = 1)
    @Max(message = "El valor del pulso '${validatedValue}' no debe ser mayor que {value}", value = 200)
    @Digits(integer = 10, fraction = 0, message = "El pulso debe ser un número entero.")
    private int pulso;
    @NotNull(message = "El campo 'T.L.L.C.' es obligatorio.")
    @Min(message = "El valor del T.L.L.C. '${validatedValue}' debe ser como mínimo {value}", value = 1)
    @Max(message = "El valor del T.L.L.C. '${validatedValue}' no debe ser mayor que {value}", value = 100)
    @Digits(integer = 10, fraction = 0, message = "El T.L.L.C. debe ser un número entero.")
    private int tllc;
    @NotNull(message = "El campo 'bucal' es obligatorio.")
    @Size(max = 200, message = "El campo 'bucal' no debe ser mayor a {max} caracteres.")
    private String bucal;
    @NotNull(message = "El campo 'escleral' es obligatorio.")
    @Size(max = 200, message = "El campo 'escleral' no debe ser mayor a {max} caracteres.")
    private String escleral;
    @NotNull(message = "El campo 'palperal' es obligatorio.")
    @Size(max = 200, message = "El campo 'palperal' no debe ser mayor a {max} caracteres.")
    private String palperal;
    @NotNull(message = "El campo 'Sexual' es obligatorio.")
    @Size(max = 200, message = "El campo 'sexual' no debe ser mayor a {max} caracteres.")
    private String sexual;
    @NotNull(message = "El campo 'submandibular' es obligatorio.")
    @Size(max = 200, message = "El campo 'submandibular' no debe ser mayor a {max} caracteres.")
    private String submandibular;
    @NotNull(message = "El campo 'preescapular' es obligatorio.")
    @Size(max = 200, message = "El campo 'preescapular' no debe ser mayor a {max} caracteres.")
    private String preescapular;
    @NotNull(message = "El campo 'precrural' es obligatorio.")
    @Size(max = 200, message = "El campo 'precrural' no debe ser mayor a {max} caracteres.")
    private String precrural;
    @NotNull(message = "El campo 'inguinal' es obligatorio.")
    @Size(max = 200, message = "El campo 'inguinal' no debe ser mayor a {max} caracteres.")
    private String inguinal;
    @NotNull(message = "El campo 'popliteo' es obligatorio.")
    @Size(max = 200, message = "El campo 'popliteo' no debe ser mayor a {max} caracteres.")
    private String popliteo;
    @Size(max = 65535, message = "El campo 'otros' no debe ser mayor a {max} caracteres.")
    private String otros;
    private boolean deleted;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    public ExamenGeneral() {
    }

    public ExamenGeneral(
            Pacientes pacientes, Date fecha, int pesoCorporal, int tempCorporal, int deshidratacion, int frecResp,
            String amplitud, String tipo, String ritmo, int frecCardio, int pulso, int tllc, String bucal,
            String escleral, String palperal, String submandibular, String preescapular, boolean deleted,
            String precrural, String inguinal, String popliteo
    ) {
        this.pacientes = pacientes;
        this.fecha = fecha;
        this.pesoCorporal = pesoCorporal;
        this.tempCorporal = tempCorporal;
        this.deshidratacion = deshidratacion;
        this.frecResp = frecResp;
        this.amplitud = amplitud;
        this.tipo = tipo;
        this.ritmo = ritmo;
        this.frecCardio = frecCardio;
        this.pulso = pulso;
        this.tllc = tllc;
        this.bucal = bucal;
        this.escleral = escleral;
        this.palperal = palperal;
        this.submandibular = submandibular;
        this.preescapular = preescapular;
        this.precrural = precrural;
        this.inguinal = inguinal;
        this.popliteo = popliteo;
        this.deleted = deleted;
    }

    public ExamenGeneral(
            Pacientes pacientes, Date fecha, int pesoCorporal, int tempCorporal, int deshidratacion, int frecResp,
            String amplitud, String tipo, String ritmo, int frecCardio, int pulso, int tllc, String bucal,
            String escleral, String palperal, String sexual, String submandibular, String preescapular,
            String precrural, String inguinal, String popliteo, String otros, boolean deleted, Date createdAt,
            Date updatedAt, Date deletedAt
    ) {
        this.pacientes = pacientes;
        this.fecha = fecha;
        this.pesoCorporal = pesoCorporal;
        this.tempCorporal = tempCorporal;
        this.deshidratacion = deshidratacion;
        this.frecResp = frecResp;
        this.amplitud = amplitud;
        this.tipo = tipo;
        this.ritmo = ritmo;
        this.frecCardio = frecCardio;
        this.pulso = pulso;
        this.tllc = tllc;
        this.bucal = bucal;
        this.escleral = escleral;
        this.palperal = palperal;
        this.sexual = sexual;
        this.submandibular = submandibular;
        this.preescapular = preescapular;
        this.precrural = precrural;
        this.inguinal = inguinal;
        this.popliteo = popliteo;
        this.otros = otros;
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

    public int getPesoCorporal() {
        return this.pesoCorporal;
    }

    public void setPesoCorporal(int pesoCorporal) {
        this.pesoCorporal = pesoCorporal;
    }

    public int getTempCorporal() {
        return this.tempCorporal;
    }

    public void setTempCorporal(int tempCorporal) {
        this.tempCorporal = tempCorporal;
    }

    public int getDeshidratacion() {
        return this.deshidratacion;
    }

    public void setDeshidratacion(int deshidratacion) {
        this.deshidratacion = deshidratacion;
    }

    public int getFrecResp() {
        return this.frecResp;
    }

    public void setFrecResp(int frecResp) {
        this.frecResp = frecResp;
    }

    public String getAmplitud() {
        return this.amplitud;
    }

    public void setAmplitud(String amplitud) {
        this.amplitud = amplitud;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRitmo() {
        return this.ritmo;
    }

    public void setRitmo(String ritmo) {
        this.ritmo = ritmo;
    }

    public int getFrecCardio() {
        return this.frecCardio;
    }

    public void setFrecCardio(int frecCardio) {
        this.frecCardio = frecCardio;
    }

    public int getPulso() {
        return this.pulso;
    }

    public void setPulso(int pulso) {
        this.pulso = pulso;
    }

    public int getTllc() {
        return this.tllc;
    }

    public void setTllc(int tllc) {
        this.tllc = tllc;
    }

    public String getBucal() {
        return this.bucal;
    }

    public void setBucal(String bucal) {
        this.bucal = bucal;
    }

    public String getEscleral() {
        return this.escleral;
    }

    public void setEscleral(String escleral) {
        this.escleral = escleral;
    }

    public String getPalperal() {
        return this.palperal;
    }

    public void setPalperal(String palperal) {
        this.palperal = palperal;
    }

    public String getSexual() {
        return this.sexual;
    }

    public void setSexual(String sexual) {
        this.sexual = sexual;
    }

    public String getSubmandibular() {
        return this.submandibular;
    }

    public void setSubmandibular(String submandibular) {
        this.submandibular = submandibular;
    }

    public String getPreescapular() {
        return this.preescapular;
    }

    public void setPreescapular(String preescapular) {
        this.preescapular = preescapular;
    }

    public String getPrecrural() {
        return this.precrural;
    }

    public void setPrecrural(String precrural) {
        this.precrural = precrural;
    }

    public String getInguinal() {
        return this.inguinal;
    }

    public void setInguinal(String inguinal) {
        this.inguinal = inguinal;
    }

    public String getPopliteo() {
        return this.popliteo;
    }

    public void setPopliteo(String popliteo) {
        this.popliteo = popliteo;
    }

    public String getOtros() {
        return this.otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
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

    @Override
    public String toString() {
        return pacientes.getNombre();
    }

}
