package dao;

import java.util.List;

import javafx.concurrent.Task;
import model.BaseModel;

public interface Dao<M extends BaseModel> {

    Task<List<M>> displayRecords();

    Task<List<M>> displayDeletedRecords();

    void add(M model);

    M showById(Integer id);

    void update(M model);

    void delete(Integer id);

    void recover(Integer id);
}
