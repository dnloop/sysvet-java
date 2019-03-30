package utils;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PaneSwitcherTest {
    protected static final Logger log = (Logger) LogManager.getLogger(MainController.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    static String fxmlPath;
    static Parent root;
    static Stage stage = new Stage();
    static FXMLLoader loader = new FXMLLoader();

    public PaneSwitcherTest() {
    } // empty constructor

    public PaneSwitcherTest(String fxmlPath) {
        PaneSwitcherTest.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public void setFxmlPath(String fxmlPath) {
        PaneSwitcherTest.fxmlPath = fxmlPath;
    }

    /* Standard pane switcher */
    public void switcher(StackPane contentPane) {
        try {
            root = (Parent) FXMLLoader.load(PaneSwitcherTest.class.getClass().getResource(fxmlPath));
//            contentPane.setTop((Parent) FXMLLoader.load(PaneSwitcher.class.getClass().getResource(fxmlPath)));
            contentPane = (StackPane) root;
        } catch (IOException e) {
            log.error(marker, "IO Exception" + e.getMessage());
            e.printStackTrace();
        }
    }
}
