package utils;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Utility class for controlling navigation between Views.
 *
 * All methods on the navigator are static to facilitate simple access from
 * anywhere in the application.
 *
 * Based on the code by: jewelsea - https://gist.github.com/jewelsea/6460130
 */
public class ViewSwitcher {
    protected static final Logger log = (Logger) LogManager.getLogger(ViewSwitcher.class);

    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
    public static final String MAIN = "/fxml/main.fxml";

    /** The main application layout controller. */
    private static MainController mainController;

    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public static void setMainController(MainController mainController) {
        ViewSwitcher.mainController = mainController;
    }

    /**
     * Loads the view specified by the fxml file into the StackPane of the main
     * application layout.
     *
     * Previously loaded view for the same fxml file are not cached. The fxml is
     * loaded anew and a new view node hierarchy generated every time this method is
     * invoked.
     *
     * A more sophisticated load function could potentially add some enhancements or
     * optimizations, for example: cache FXMLLoaders cache loaded view nodes, so
     * they can be recalled or reused allow a user to specify view node reuse or new
     * creation allow back and forward history like a browser
     *
     * @param fxml the fxml file to be loaded.
     */
    public static void loadView(String fxml) {
        try {
            mainController.setView(FXMLLoader.load(ViewSwitcher.class.getResource(fxml)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadNode(Node node) {
        mainController.setView(node);
    }

    public <T> T loadCustomAnchor(String path, AnchorPane aPane, T controller) {
        /*
         * Lets just say this is only for special cases... for now. =)
         */
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(path));
            Node node = fxmlLoader.load();
            controller = fxmlLoader.getController();
            aPane.getChildren().setAll(node);
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            log.debug("Load succesfull.");
        } catch (IOException e) {
            log.debug("Failed to load Pane: " + e.getCause());
            e.printStackTrace();
        }
        return controller;
    }

}