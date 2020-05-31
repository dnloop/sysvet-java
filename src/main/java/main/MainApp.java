package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.AppReadyCallback;
import utils.HibernateUtil;
import utils.routes.RouteExtra;
import utils.validator.HibernateValidator;
import utils.viewswitcher.TaskManager;
import utils.viewswitcher.UILoader;
import utils.viewswitcher.ViewSwitcher;

public class MainApp extends Application implements AppReadyCallback {

    private static final Logger log = (Logger) LogManager.getLogger(MainApp.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private BooleanProperty state = new SimpleBooleanProperty(false);

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        log.info("[ Starting Sysvet application ]");
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        log.info(marker, "[ Loading modules ]");
        log.info(marker, "[ Setting Loading Dialog ]");
        initLoadingDialog();
        log.info(marker, "[ Setting Main Stage]");
        ViewSwitcher.loadingDialog.startTask();
        log.info(marker, "[ Waiting application start ]");
        state.addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (Boolean.TRUE.equals(t1)) {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            Scene scene = initMainPane();
                            stage.setTitle(" -·=[ SysVet ]=·-");
                            stage.setScene(scene);
                            log.info(marker, "[ Application running ]");
                            stage.show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void stop() throws Exception {
        try {
            HibernateValidator.closeValid();
            ViewSwitcher.loadingDialog.stop();
            HibernateUtil.getSessionFactory().close();
        } catch (Exception e) {
            log.info("Database not started");
        } finally {
            log.info("[ Terminated Sysvet application ]");
        }

    }

    /**
     * First the LoadingDialog is created to act as a splash screen providing
     * feedback to the state of application initialization.
     */
    private void initLoadingDialog() {
        ViewSwitcher.uiLoader = new UILoader();
        ViewSwitcher.loadingDialog = ViewSwitcher.uiLoader.createLoadingDialog();
        Parent node = (Parent) ViewSwitcher.uiLoader.getNode(RouteExtra.LOADING.getPath());
        Task<Void> task = TaskManager.hibernateConfiguration;
        Stage loading = ViewSwitcher.uiLoader.buildStage("Diálogo de carga.", node);
        ViewSwitcher.loadingDialog.addTask(task);
        ViewSwitcher.loadingDialog.setStage(loading);
        ViewSwitcher.loadingDialog.setCallback(this);
    }

    /**
     * Loads the main fxml layout. Sets up ViewSwitcher. Loads the first view into
     * the FXML layout. Creates the LoadingDialog.
     *
     * @return the main application scene.
     */
    private Scene initMainPane() {
        ViewSwitcher.uiLoader = new UILoader();
        Scene scene = ViewSwitcher.uiLoader.createMainPane();
        MainController mainController = ViewSwitcher.getController(RouteExtra.MAIN.getPath());
        ViewSwitcher.setMainController(mainController);

        return scene;
    }

    @Override
    public void appState(Boolean ready) {
        if (ready)
            state.setValue(Boolean.TRUE);
    }
}
