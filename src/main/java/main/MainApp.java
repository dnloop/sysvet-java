package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application{

    protected static final Logger log = (Logger) LogManager.getLogger(MainApp.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        log.info("[ Starting Sysvet application ]");

        String fxmlFile = "/fxml/main.fxml";
        log.debug(marker, "Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

        log.debug(marker, "Showing JFX scene");
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        Scene scene = new Scene(rootNode, 600, 380);
        stage.setTitle(" -·=[ SysVet ]=·-");
        stage.setScene(scene);
        //        stage.setResizable(false);
        stage.show();
    }

}
