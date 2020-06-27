package utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
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

	private static final Logger log = (Logger) LogManager.getLogger(LoadingDialog.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private Stage stage = new Stage();

	private int numTasks;

	private IntegerProperty pendingTasks = new SimpleIntegerProperty(0);

	private AppReadyCallback callback;

	private final ScheduledExecutorService exec = Executors.newScheduledThreadPool(10, r -> {
		Thread t = new Thread(r);
		t.setDaemon(true);
		return t;
	});

	private List<Task<?>> taskList = new ArrayList<>();

	public void startTask() {

		numTasks = taskList.size();

		lblPendingTasks.textProperty().bind(pendingTasks.asString("%d de " + numTasks));

		pendingTasks.set(numTasks);

		if (pendingTasks.get() > 0) {
			stage.show();
			taskList.forEach(task -> task.stateProperty().addListener((obs, oldState, newState) -> {
				log.debug(marker, "Task " + newState);
				// update lblPendingTasks if task moves out of running state:
				if (oldState == Worker.State.RUNNING)
					pendingTasks.set(pendingTasks.get() - 1);

				if (pendingTasks.get() == 0) {
					stage.hide();
					log.info(marker, "Job finished.");
					callback.appState(Boolean.TRUE);
				}
			}));

			taskList.forEach(task -> {
				exec.schedule(task, 1000, TimeUnit.MILLISECONDS);
			});
		}
		taskList.clear();
	}

	public List<Task<?>> getTask() {
		return taskList;
	}

	public void addTask(Task<?> task) {
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

	public void setCallback(AppReadyCallback callback) {
		this.callback = callback;
	}

	public List<Task<?>> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task<?>> taskList) {
		this.taskList = taskList;
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
		this.stage.hide();
	}

}
