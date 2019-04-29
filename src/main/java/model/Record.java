package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class PacienteFicha extends RecursiveTreeObject<PacienteFicha> implements java.io.Serializable {

    /**
     * Esta clase sirve de soporte a FichasClinicas
     */
    private static final long serialVersionUID = -2357760413746833349L;
    private Integer id;
    private Pacientes paciente;

    public PacienteFicha() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pacientes getPaciente() {
        return paciente;
    }

    public void setPaciente(Pacientes paciente) {
        this.paciente = paciente;
    }
}
