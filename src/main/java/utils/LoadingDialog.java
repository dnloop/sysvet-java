package utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class LoadingDialog {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label lblPendingTasks;

    protected static final Logger log = (Logger) LogManager.getLogger(LoadingDialog.class);

    private Stage stage;

    private int numTasks;

    private IntegerProperty pendingTasks = new SimpleIntegerProperty(0);

    private final ExecutorService exec = Executors.newFixedThreadPool(10, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    private List<Task<?>> taskList = new ArrayList<>();

    @FXML
    void initialize() {
        assert progressIndicator != null : "fx:id=\"progressIndicator\" was not injected: check your FXML file 'loading.fxml'.";
        assert lblPendingTasks != null : "fx:id=\"lblPendingTasks\" was not injected: check your FXML file 'loading.fxml'.";

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void showStage() {
        this.stage.show();
    }

    public void closeStage() {
        this.stage.close();
    }

    public void startTask() {
        showStage();

        numTasks = 0;

        setNumTasks(taskList.size());

        lblPendingTasks.textProperty().bind(pendingTasks.asString("%d de " + getNumTasks()));

        pendingTasks.set(getNumTasks());

        taskList.forEach(task -> task.stateProperty().addListener((obs, oldState, newState) -> {
            log.info("Task " + newState);
            // update lblPendingTasks if task moves out of running state:
            if (oldState == Worker.State.RUNNING)
                pendingTasks.set(pendingTasks.get() - 1);

            if (pendingTasks.get() == 0)
                closeStage();
        }));

        taskList.forEach(exec::execute);

        taskList.clear();
    }

    public List<Task<?>> getTask() {
        return taskList;
    }

    public void setTask(Task<?> task) {
        this.taskList.add(task);
    }

    public void stop() {
        exec.shutdown();
    }

    public int getNumTasks() {
        return numTasks;
    }

    public void setNumTasks(int numTasks) {
        this.numTasks = numTasks;
    }
}
