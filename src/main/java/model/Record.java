package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class Record<T> extends RecursiveTreeObject<Record<T>> implements java.io.Serializable {

    /**
     * Esta clase sirve de soporte a: FichasClinicas, Historia Clínica, . Debería
     * ser genérica ya que no se utiliza dos veces en un mismo lugar a la vez.
     */
    private static final long serialVersionUID = -2357760413746833349L;
    private Integer id;
    private T record;

    public Record() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }
}
