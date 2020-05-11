package utils.viewswitcher;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import controller.MainController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.LoadingDialog;
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
/**
 * @author dnloop
 *
 */
/**
 * @author dnloop
 *
 */
/**
 * @author dnloop
 *
 */
/**
 * @author dnloop
 *
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
    public static LoadingDialog loadingDialog;

    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public static void setMainController(MainController mainController) {
        ViewSwitcher.mainController = mainController;
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

    /**
     * Loads a node into the Pane of the main application layout. The node is
     * previously defined by the FXMLoader.
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
     * @param node - The node to be loaded.
     */
    public static void loadView(Node node) {
        mainController.setView(node);
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
    public static void loadContent(Node node) {
        mainController.setView(node);
    }

    /**
     * Loads an fxml file given a path and returns a controller for further
     * operations.
     * 
     * @param path - The path to the fxml layout.
     * @return The controller used on the layout.
     */
    public <T> T loadNode(String path) {
        T controller = null;
        fxmlLoader = new FXMLLoader();
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

    /**
     * Loads an fxml file given a path and returns a controller for further
     * operations. The overloaded parameter is used to adjust an anchor pane to fit
     * the entire container.
     * 
     * @param path  - The path to the fxml layout.
     * @param aPane - The anchor pane to be adjusted.
     * @return The controller used on the layout.
     */
    public <T> T loadNode(String path, AnchorPane aPane) {
        T controller = null;
        fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.setLocation(getClass().getResource(path));
            setNode(fxmlLoader.load());
            controller = fxmlLoader.getController();
            aPane.getChildren().setAll(node);
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
        } catch (IOException e) {
            log.debug("Failed to load Node: " + e.getCause());
            e.printStackTrace(); // TODO log error to file not stdout.
        }
        return controller;
    }

    /**
     * This method is used to load the {@link LoadingDialog} as an independent modal
     * dialog (no initializer owner).
     * 
     * @param <T>   The Concurrency controller used on initialize.
     * @param route The Concurrency controller's route.
     * @return The Concurrency controller.
     */
    public <T> T init(String route) {
        Selector<T> selector = new Selector<>();
        selector.buildModal(route);
        this.stage = selector.getStage();
        return selector.getController();
    }

    /**
     * This method is used to load a modal dialog. It requires the route to the FXML
     * file, the title to be set on the modal window and the event used to extract
     * the parent window required to define the initialize owner.
     * 
     * @param <T>
     * @param route - The path to the FXML layout.
     * @param title - The modal window title.
     * @param event - The source event that called the method.
     * @return
     * 
     * @See Route
     * @See RouteExtra
     */
    public <T> T loadModal(String route, String title, Event event) {
        Selector<T> selector = new Selector<>();
        selector.buildModal(route, title, event);
        this.stage = selector.getStage();
        return selector.getController();
    }

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