package utils;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PaneSwitcher {
    protected static final Logger log = (Logger) LogManager.getLogger(MainController.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    static String fxmlPath;
    static Parent root;
    static Stage stage = new Stage();

    public PaneSwitcher() {} // empty constructor

    public PaneSwitcher(String fxmlPath) {
        PaneSwitcher.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public void setFxmlPath(String fxmlPath) {
        PaneSwitcher.fxmlPath = fxmlPath;
    }
    /* Standard pane switcher */
    public void switcher(BorderPane contentPane) {
        try {
            contentPane.setTop(
                    (Parent) FXMLLoader.load(
                            PaneSwitcher.class.getClass().getResource(fxmlPath)
                            ));
        } catch (IOException e) {
            log.error(marker, "IO Exception" + e.getMessage());
            e.printStackTrace();
        }
    }
}
