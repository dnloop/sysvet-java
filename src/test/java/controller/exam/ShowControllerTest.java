package controller.exam;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.ExamenGeneral;
import utils.ViewSwitcher;

public class ShowControllerTest {

    protected static final Logger log = (Logger) LogManager.getLogger(ShowControllerTest.class);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<ExamenGeneral> indexE;

    private Integer id;

    private Stage stage;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexE != null : "fx:id=\"indexE\" was not injected: check your FXML file 'index.fxml'.";
        Platform.runLater(() -> {
            log.info("aft - Controller ID: " + this.id);
            btnShow.setOnAction((event) -> {
                Parent rootNode;
                Stage stage = new Stage();
                Window node = ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/exam/modalDialog.fxml"));
                try {
                    rootNode = (Parent) fxmlLoader.load();
                    ModalDialogControllerTest controller = fxmlLoader.getController();
                    if (this.id != null) {
                        controller.setObject(this.id);
                        System.out.println("Loaded");
                    }
                    stage.setScene(new Scene(rootNode));
                    stage.setTitle("Cuenta Corriente");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(node);
                    controller.showModal(stage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

//        btnDelete.setOnAction((event) -> {
//            log.info("Item deleted.");
//        });
            // TODO add search filter
        });
    }

    /* Class Methods */
    public void setNumero(Integer id) {
        this.id = id;
    }

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

}
