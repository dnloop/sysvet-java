package utils.viewswitcher;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import utils.routes.RouteExtra;

/**
 * The UILoader class builds the fxml views to be used in the
 * {@link ViewSwitcher}.
 *
 */
public class UILoader {
    private static final Logger log = (Logger) LogManager.getLogger(UILoader.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private FXMLLoader loader;

    private Node node;

    private Stage stage;

    private Scene scene;

    private Pair<?, Node> pair;

    private final HashMap<String, Pair<?, Node>> storedViews = new HashMap<>();

    public UILoader() {
        super();
    }

    /**
     * Constructs a pair of a controller and a node to be used in the stored map of
     * views.
     * 
     * @param <T>
     * 
     * @param route - The path to the FXML layout.
     * @return The task to load the views concurrently.
     */
    public <T> void buildNode(String route) {
        loader = new FXMLLoader(getClass().getResource(route));
        try {
            node = loader.load();
            pair = new Pair<T, Node>(loader.getController(), node);
            storedViews.put(route, pair);
        } catch (IOException e) {
            log.error(marker, "Node building failed: " + e.getCause());
            e.printStackTrace();
        }
    }

    /**
     * Builds an independent modal dialog, without an owner.
     */
    public Stage buildStage(String title, Parent node) {
        stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        scene = new Scene(node);
        stage.setScene(scene);
        return stage;
    } // used on application start

    /**
     * Builds a modal dialog with an initializer owner.
     * 
     * @param route - The path to the FXML layout.
     * @param title - The modal window title.
     * @param stage - The source stage that called the modal.
     * 
     */
    public Stage buildStage(String title, Parent node, Stage owner) {
        stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        scene = new Scene(node);
        stage.setScene(scene);
        return stage;

    } // used on edit/new view

    /**
     * Creates the main application scene.
     *
     * @return the created scene.
     */
    public <T> Scene createMainPane() {
        String route = RouteExtra.MAIN.getPath();
        loader = new FXMLLoader(getClass().getResource(route));
        try {
            Parent node = loader.load();
            scene = new Scene(node, 900, 500);
            pair = new Pair<T, Node>(loader.getController(), node);
            storedViews.put(route, pair);
        } catch (IOException e) {
            log.error(marker, "Main Application Node building failed: " + e.getCause());
//            log.debug(marker, e.getStackTrace());
            e.printStackTrace();
        }
        return scene;
    }

    /**
     * Creates the main application LoadingDialog.
     *
     * @return The created loading dialog.
     */
    public <T> T createLoadingDialog() {
        String route = RouteExtra.LOADING.getPath();
        loader = new FXMLLoader(getClass().getResource(route));
        T controller = null;
        try {
            Node node = loader.load();
            controller = loader.getController();
            pair = new Pair<T, Node>(controller, node);
            storedViews.put(route, pair);
        } catch (IOException e) {
            log.error(marker, "Loading Dialog Node building failed: " + e.getCause());
            log.debug(marker, e.getStackTrace());
        }
        return controller;
    }

    /**
     * @return The graphic node of the view.
     */
    public Node getNode(String path) {
        Pair<?, Node> pair = storedViews.get(path);
        if (pair == null)
            return null;
        else
            return storedViews.get(path).getValue();
    }

    /**
     * @param <T>  - The type of the controller for the FXML layout.
     * @param path - The path to the FXML layout.
     * @return The controller for the FXML layout.
     */
    @SuppressWarnings("unchecked")
    public <T> T getController(String path) {
        return (T) storedViews.get(path).getKey();
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * HashMap containing the in memory cache of the graphic nodes and controllers
     * to be retrieved when needed.
     */
    public HashMap<String, Pair<?, Node>> getStoredViews() {
        return storedViews;
    }
}
