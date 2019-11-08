package main;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.HibernateUtil;
import utils.LoadingDialog;
import utils.ViewSwitcher;
import utils.validator.HibernateValidator;

public class MainApp extends Application {

    protected static final Logger log = (Logger) LogManager.getLogger(MainApp.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        log.info("[ Starting Sysvet application ]");
        log.info("Loading Database");
        try {
            HibernateUtil.setUp();
        } catch (Exception e) {
            log.debug(marker, "Unable establish the session. " + e.getMessage());
        }
        log.info("[ Loading modules ]");
        HibernateValidator.buildValid();
        stage.setTitle(" -·=[ SysVet ]=·-");
        stage.setScene(createScene(loadMainPane()));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        HibernateValidator.closeValid();
        ViewSwitcher.getLoadingDialog().stop();
        try {
            HibernateUtil.getSessionFactory().close();
        } catch (Exception e) {
            log.info("Database not started");
        } finally {
            log.info("[ Terminated Sysvet application ]");
        }

    }

    /**
     * Loads the main fxml layout. Sets up the view switching ViewSwitcher. Loads
     * the first view into the fxml layout.
     *
     * @return the loaded pane.
     * @throws IOException if the pane could not be loaded.
     */
    private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane mainPane = (Pane) loader.load(getClass().getResourceAsStream(ViewSwitcher.MAIN));
        MainController mainController = loader.getController();
        ViewSwitcher.setMainController(mainController);
        ViewSwitcher vs = new ViewSwitcher();
        LoadingDialog loadingDialog = vs.loadModal(ViewSwitcher.LOAD);
        ViewSwitcher.setLoadingDialog(loadingDialog);
        loadingDialog.setStage(vs.getStage());

        return mainPane;
    }

    /**
     * Creates the main application scene.
     *
     * @param mainPane the main application layout.
     *
     * @return the created scene.
     */
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(mainPane, 900, 500);
        setUserAgentStylesheet(STYLESHEET_CASPIAN);

        return scene;
    }
}
