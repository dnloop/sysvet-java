package controller.clinicalFile;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import dao.PacientesHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Pacientes;
import utils.ViewSwitcher;

public class IndexController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<Pacientes> indexCF;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static PacientesHome daoPA = new PacientesHome();

    private Pacientes paciente;

    private Integer id;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private TreeItem<Pacientes> root;

    // Table column

    private JFXTreeTableColumn<Pacientes, Pacientes> pacientes = new JFXTreeTableColumn<Pacientes, Pacientes>(
            "Pacientes - (ficha)");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexCF != null : "fx:id=\"indexCF\" was not injected: check your FXML file 'index.fxml'.";

        pacientes.setPrefWidth(200);
        pacientes.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue().getValue()));

        log.info("loading table items");

        pacientesList.setAll(daoPA.displayRecordsWithClinicalRecords());

        root = new RecursiveTreeItem<Pacientes>(pacientesList, RecursiveTreeObject::getChildren);

        indexCF.getColumns().setAll(pacientes);
        indexCF.setShowRoot(false);
        indexCF.setRoot(root);

        // Handle ListView selection changes.
        indexCF.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                id = newValue.getValue().getId();
                paciente = newValue.getValue();
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnShow.setOnAction((event) -> {
            if (id != null)
                displayShow(event);
            else
                displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (id != null)
                confirmDialog();
            else
                displayWarning();
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void setView(Node node) {
        ViewSwitcher.loadNode(node);
    } // replace current view

    private void confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción.");
        alert.setContentText("¿Desea eliminar el registro?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            daoPA.delete(id);
            indexCF.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexCF.refresh();
            log.info("Item deleted.");
        }
    }

    private void displayShow(Event event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/clinicalFile/show.fxml"));
        try {
            Node node = fxmlLoader.load();
            ShowController sc = fxmlLoader.getController();
            sc.setObject(paciente);
            setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia.");
        alert.setHeaderText("Elemento vacío.");
        alert.setContentText("No se seleccionó ningún elemento de la lista. Elija un ítem e intente nuevamente.");
        alert.setResizable(true);

        alert.showAndWait();
    }

    private void displayNew(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/clinicalFile/new.fxml"));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            NewController sc = fxmlLoader.getController();
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Nuevo elemento - Ficha Clínica");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHiding((stageEvent) -> {
                indexCF.refresh();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
