package utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoadingDialog {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Text message;

    private Stage stage;

    private List<Task<?>> taskList = new ArrayList<>();

    @FXML
    void initialize() {
        assert progressIndicator != null : "fx:id=\"progressIndicator\" was not injected: check your FXML file 'loading.fxml'.";
        assert message != null : "fx:id=\"message\" was not injected: check your FXML file 'loading.fxml'.";
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

    private void setProgress(Task<?> task) {
        message.textProperty().bind(task.messageProperty());
    }

    public void startTask() {
        for (Task<?> task : taskList) {
            setProgress(task);
            new Thread(task).start();
        }
        this.stage.close();
    }

    public List<Task<?>> getTask() {
        return taskList;
    }

    public void setTask(Task<?> task) {
        this.taskList.add(task);
    }
}
