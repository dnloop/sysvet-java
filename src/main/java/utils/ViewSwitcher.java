package utils;

import java.io.IOException;

import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * Utility class for controlling navigation between vistas.
 *
 * All methods on the navigator are static to facilitate simple access from
 * anywhere in the application.
 *
 * jewelsea - https://gist.github.com/jewelsea/6460130
 */
public class ViewSwitcher {

    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
    public static final String MAIN = "/fxml/main.fxml";
    public static final String BASE = "/fxml/base.fxml";

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
     * Loads the vista specified by the fxml file into the StackPane of the main
     * application layout.
     *
     * Previously loaded vista for the same fxml file are not cached. The fxml is
     * loaded anew and a new vista node hierarchy generated every time this method
     * is invoked.
     *
     * A more sophisticated load function could potentially add some enhancements or
     * optimizations, for example: cache FXMLLoaders cache loaded vista nodes, so
     * they can be recalled or reused allow a user to specify vista node reuse or
     * new creation allow back and forward history like a browser
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

}