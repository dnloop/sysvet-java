package utils;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class PaneSwitcher {
    protected static final Logger log = (Logger) LogManager.getLogger(MainController.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    
    static String fxmlPath;

    public PaneSwitcher() {}

    public String getFxmlPath() {
        return fxmlPath;
    }

    public void setFxmlPath(String fxmlPath) {
        PaneSwitcher.fxmlPath = fxmlPath;
    }

    public void switcher(BorderPane contentPane) {
        try {
          contentPane.setCenter(
                  FXMLLoader.load(
                          PaneSwitcher.class.getClass().getResource(fxmlPath)
          ));
        } catch (IOException e) {
            log.error(marker, "wrong fxml path" + e);
        }
    }

}
