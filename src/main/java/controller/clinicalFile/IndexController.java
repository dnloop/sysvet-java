package controller.clinicalFile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import dao.FichasClinicasHome;
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
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Pacientes;
import utils.DialogBox;
import utils.Route;
import utils.TableUtil;
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

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private PacientesHome daoPA = new PacientesHome();

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private Pacientes paciente;

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
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexCF, pacientesList, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexCF.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                paciente = newValue.getValue();
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnShow.setOnAction((event) -> {
            if (paciente != null)
                displayShow(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (paciente != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    daoFC.deleteAll(paciente.getId());
                    TreeItem<Pacientes> selectedItem = indexCF.getSelectionModel().getSelectedItem();
                    indexCF.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    paciente = null;
                    refreshTable();
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            indexCF.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty())
                    return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getValue().getNombre().toLowerCase().contains(lowerCaseFilter))
                    return true;
                return false;
            });
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

    private void displayShow(Event event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.FICHACLINICA.indexView()));
        try {
            Node node = fxmlLoader.load();
            ShowController sc = fxmlLoader.getController();
            sc.setObject(paciente);
            setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayNew(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.FICHACLINICA.newView()));
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
                refreshTable();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        pacientesList.clear();
        pacientesList.setAll(daoPA.displayRecordsWithClinicalRecords());
        root = new RecursiveTreeItem<Pacientes>(pacientesList, RecursiveTreeObject::getChildren);
        indexCF.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexCF, pacientesList, tablePagination, index, 20));
    }
}
