package utils;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import controller.MainController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import utils.routes.RouteExtra;

/**
 * Utility class for controlling navigation between Views.
 *
 * All methods on the navigator are static to facilitate simple access from
 * anywhere in the application.
 *
 * Based on the code by: jewelsea - https://gist.github.com/jewelsea/6460130
 *
 * NOTE: This class is a complete mess but totally salvageable =)
 */
public class ViewSwitcher {
    protected static final Logger log = (Logger) LogManager.getLogger(ViewSwitcher.class);

    private FXMLLoader fxmlLoader;

    private Stage stage;

    private Node node;

    public ViewSwitcher() {
        fxmlLoader = new FXMLLoader();
        stage = new Stage();
    }

    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
    public static final String MAIN = "/fxml/main.fxml";

    public static final String LOAD = RouteExtra.LOADING.getPath();

    /** The main application layout controller. */
    private static MainController mainController;

    /** Loading dialog for concurrent tasks */
    private static LoadingDialog loadingDialog;

    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public static void setMainController(MainController mainController) {
        ViewSwitcher.mainController = mainController;
    }

    /**
     * Stores the loading dialog controller for later use in concurrent tasks.
     *
     * @param loadingDialog the loading layout controller.
     */

    public static void setLoadingDialog(LoadingDialog loadingDialog) {
        ViewSwitcher.loadingDialog = loadingDialog;
    }

    public static LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }

    /**
     * Loads the view specified by the fxml file into the Pane of the main
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

    public static TreeItem<String> setPath(String[] path) {
        return mainController.setPath(path);
    }

    public static void setNavi(TreeItem<String> model) {
        mainController.setNavi(model);
    }

    /**
     * Loads a Node inside an existing Pane.
     */
    public static void loadNode(Node node) {
        mainController.setView(node);
    }

    public <T> T loadNode(String path) {
        T controller = null;
        try {
            fxmlLoader.setLocation(getClass().getResource(path));
            setNode(fxmlLoader.load());
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            log.debug("Failed to load Node: " + e.getCause());
            e.printStackTrace(); // TODO log error to file not stdout.
        }
        return controller;
    }

    // TODO fix this code.
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

    // TODO fix this mess.
    public <T> T loadModal(String path) {
        T controller = null;
        try {
            fxmlLoader.setLocation(getClass().getResource(path));
            Parent rootNode = (Parent) fxmlLoader.load();
            controller = fxmlLoader.getController();
            stage.setScene(new Scene(rootNode));
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            log.error("Cannot display view: " + e.getCause());
            log.debug("[ IOException ] " + e.getMessage()); // TODO this should be a marker.
            DialogBox.setHeader(e.getCause().toString());
            DialogBox.setContent(e.getMessage());
            DialogBox.displayError();
            e.printStackTrace(); // TODO log error to file not stdout.
        }
        return controller;
    } // new view
      // TODO not DRY.

    public <T> T loadModal(String path, String title, Event event) {
        T controller = null;
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            fxmlLoader.setLocation(getClass().getResource(path));
            Parent rootNode = (Parent) fxmlLoader.load();
            controller = fxmlLoader.getController();
            stage.setScene(new Scene(rootNode));
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
        } catch (IOException e) {
            log.error("Cannot display view: " + e.getCause());
            log.debug("[ IOException ] " + e.getMessage()); // TODO this should be a marker.
            DialogBox.setHeader(e.getCause().toString());
            DialogBox.setContent(e.getMessage());
            DialogBox.displayError();
            e.printStackTrace(); // TODO log error to file not stdout.
        }
        return controller;
    } // edit view

    public Stage getStage() {
        return stage;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

}