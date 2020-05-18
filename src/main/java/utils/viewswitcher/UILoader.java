package utils.viewswitcher;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 * The UILoader class builds the fxml views to be used in the
 * {@link ViewSwitcher}.
 *
 */
public class UILoader {
    protected static final Logger log = (Logger) LogManager.getLogger(UILoader.class);

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
    public <T> Task<Void> buildTask(String route) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                loader = new FXMLLoader();
                try {
                    loader.setLocation(getClass().getResource(route));
                    node = loader.load();
                    pair = new Pair<T, Node>(loader.getController(), node);
                    storedViews.put(route, pair);
                } catch (IOException e) {
                    log.error("Node building failed: " + e.getCause());
                    log.debug(e.getStackTrace());
                }
                return null;
            }

            @Override
            protected void cancelled() {
                updateMessage("Carga cancelada.");
                log.debug("Canceled View Loading: \n" + route);
            }

            @Override
            protected void failed() {
                updateMessage("Carga fallida.");
                storedViews.put(route, null);
                log.debug("Loading Failed:  \n" + route);
            }
        };
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
    public Stage buildStage(String title, Parent node, Stage stage) {
        stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(stage);
        scene = new Scene(node);
        stage.setScene(scene);
        return stage;

    } // used on edit/new view

    public Node getNode() {
        return node;
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

    public HashMap<String, Pair<?, Node>> getStoredViews() {
        return storedViews;
    }
}
