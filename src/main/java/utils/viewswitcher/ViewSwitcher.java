package utils.viewswitcher;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import controller.MainController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import utils.LoadingDialog;

/**
 * Utility class for controlling navigation between Views.
 *
 * All methods on the navigator are static to facilitate simple access from
 * anywhere in the application.
 *
 * Based on the code by: jewelsea - https://gist.github.com/jewelsea/6460130
 *
 */
public class ViewSwitcher {
    protected static final Logger log = (Logger) LogManager.getLogger(ViewSwitcher.class);

    private static HashMap<String, Pair<?, Node>> storedViews;

    private static UILoader uiLoader;

    /* The stage used by the main application layout. */
    public static Stage mainStage;

    /* The stage used by modals */
    public static Stage modalStage;

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
     * Loads a node into the Pane of the main application layout. The node is
     * previously defined by the FXMLoader.
     *
     * The path is the key value of a cached map object from UILoader.
     *
     * @param path - The path to the fxml layout.
     */
    public static void loadView(String path) {
        Node node = storedViews.get(path).getValue();
        mainController.setView(node);
    }

    /**
     * Loads a node into the Pane of the main application layout. The node is
     * previously defined by the FXMLoader.
     *
     * The path is the key value of a cached map object from UILoader.
     *
     * @param path - The path to the fxml layout.
     */
    public static void loadView(Node node) {
        mainController.setView(node);
    }

    public static void setPath(String[] path) {
        TreeItem<String> navi = mainController.setPath(path);
        mainController.setNavi(navi);
    }

    /**
     * This hack is necessary because when the node is loaded inside an FXML layout
     * the constraints are not set.
     * 
     * @param node  - The node inserted into a pane.
     * @param aPane - The anchor pane to be adjusted.
     */
    public static void adjustPane(Node node, AnchorPane aPane) {
        aPane.getChildren().setAll(node);
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    /**
     * Retrieves the node containing the modal dialog and loads it on a different
     * stage. The boolean parameter checks wheather the modal must be assigned an
     * owner or it is an independent window.
     * 
     * @param route - The path to the FXML layout.
     * @param title - The title used on the modal.
     * @param owner - Check if the modal has an owner.
     */
    public static void loadModal(String route, String title, Boolean owner) {
        Parent node = (Parent) storedViews.get(route).getValue();
        if (owner)
            modalStage = uiLoader.buildStage(title, node, mainStage);
        else
            modalStage = uiLoader.buildStage(title, node);

    }

    /**
     * @return The graphic node of the view.
     */
    public static Node getNode(String path) {
        return storedViews.get(path).getValue();
    }

    /**
     * @return The utility to load views.
     */
    public UILoader getUiLoader() {
        return uiLoader;
    }

    /**
     * @param uiLoader - The utility to load views.
     */
    public void setUiLoader(UILoader uiLoader) {
        ViewSwitcher.uiLoader = uiLoader;
    }

    /**
     * @param <T>  - The type of the controller for the FXML layout.
     * @param path - The path to the FXML layout.
     * @return The controller for the FXML layout.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getController(String path) {
        return (T) storedViews.get(path).getKey();
    }
}