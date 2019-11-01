package dao;

import java.util.List;

import model.BaseModel;

public interface Dao<M extends BaseModel> {

    List<M> displayRecords();

    List<M> displayDeletedRecords();

    void add(M model);

    M showById(Integer id);

    void update(M model);

    void delete(Integer id);

    void recover(Integer id);
}
