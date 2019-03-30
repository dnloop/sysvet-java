package main;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import controller.MainControllerTest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.ViewSwitcher;
import utils.ViewSwitcherTest;

public class MainAppTest extends Application {

    protected static final Logger log = (Logger) LogManager.getLogger(MainAppTest.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        log.info("[ Starting Sysvet (test) application ]");
        stage.setTitle(" -·=[ SysVet - TEST ]=·-");
        stage.setScene(createScene(loadMainPane()));
        stage.show();
    }

    /**
     * Loads the main fxml layout. Sets up the vista switching VistaNavigator. Loads
     * the first vista into the fxml layout.
     *
     * @return the loaded pane.
     * @throws IOException if the pane could not be loaded.
     */
    private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane mainPane = (Pane) loader.load(getClass().getResourceAsStream(ViewSwitcher.MAIN));
        MainControllerTest baseController = loader.getController();
        ViewSwitcherTest.setMainController(baseController);
//        ViewSwitcher.loadView(ViewSwitcher.MAIN);

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
        Scene scene = new Scene(mainPane, 800, 420);
        setUserAgentStylesheet(STYLESHEET_CASPIAN);

        return scene;
    }
}
