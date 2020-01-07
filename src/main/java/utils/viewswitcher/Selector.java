package utils.viewswitcher;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * The Selector class builds the fxml views to be used in the
 * {@link ViewSwitcher}.
 * 
 * @param <T> The controller used by the view.
 *
 */
public class Selector<T> {
    protected static final Logger log = (Logger) LogManager.getLogger(Selector.class);

    private FXMLLoader loader;

    private Node node;

    private Stage stage;

    private T controller;

    public Selector() {
        super();
        this.loader = new FXMLLoader();
    }

    public Selector(FXMLLoader loader, Node node, T controller) {
        super();
        this.loader = loader;
        this.node = node;
        this.controller = controller;
    }

    public Node build(String route) {
        loader = new FXMLLoader();

        try {
            loader.setLocation(getClass().getResource(route));
            node = loader.load();
            controller = loader.getController();
            return node;
        } catch (IOException e) {
            log.error("Node building failed: " + e.getCause());
            log.debug(e.getStackTrace());
            e.printStackTrace(); // TODO log error to file not stdout.
            return null;
        }

    }

    public T buildModal(String route) {
        T controller = null;
        try {
            loader.setLocation(getClass().getResource(route));
            Parent rootNode = (Parent) loader.load();
            controller = loader.getController();
            stage.setScene(new Scene(rootNode));
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            log.error("Cannot display view: " + e.getCause());
            log.debug(e.getStackTrace());
            e.printStackTrace(); // TODO log error to file not stdout.
        }
        return controller;

    } // new view

    public T loadModal(String route, String title, Event event) {
        T controller = null;
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            loader.setLocation(getClass().getResource(route));
            Parent rootNode = (Parent) loader.load();
            controller = loader.getController();
            stage.setScene(new Scene(rootNode));
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
        } catch (IOException e) {
            log.error("Cannot display view: " + e.getCause());
            log.debug(e.getStackTrace());
            e.printStackTrace(); // TODO log error to file not stdout.
        }
        return controller;
    } // edit view

    public T getController() {
        return controller;
    }

    public void setController(T controller) {
        this.controller = controller;
    }

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

}
