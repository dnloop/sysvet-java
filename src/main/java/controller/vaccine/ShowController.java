package controller.vaccine;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import dao.VacunasHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import model.Vacunas;
import utils.DialogBox;
import utils.ViewSwitcher;

public class ShowController {

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
    private JFXTreeTableView<Vacunas> indexVC;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private VacunasHome dao = new VacunasHome();

    private Vacunas vacuna;

    private Pacientes paciente;

    final ObservableList<Vacunas> vaccineList = FXCollections.observableArrayList();

    private TreeItem<Vacunas> root;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexVC != null : "fx:id=\"indexVC\" was not injected: check your FXML file 'show.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'show.fxml'.";

        Platform.runLater(() -> {
            JFXTreeTableColumn<Vacunas, Pacientes> pacientes = new JFXTreeTableColumn<Vacunas, Pacientes>("Pacientes");
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<Vacunas, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getPacientes()));

            JFXTreeTableColumn<Vacunas, String> descripcion = new JFXTreeTableColumn<Vacunas, String>("Descripción");
            descripcion.setPrefWidth(200);
            descripcion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Vacunas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getDescripcion())));

            JFXTreeTableColumn<Vacunas, Date> fecha = new JFXTreeTableColumn<Vacunas, Date>("Fecha Inicio");
            fecha.setPrefWidth(150);
            fecha.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Vacunas, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFecha()));

            log.info("loading table items");

            vaccineList.setAll(dao.showByPatient(paciente));
            root = new RecursiveTreeItem<Vacunas>(vaccineList, RecursiveTreeObject::getChildren);

            indexVC.getColumns().setAll(pacientes, fecha, descripcion, fecha);
            indexVC.setShowRoot(false);
            indexVC.setRoot(root);

            // Handle ListView selection changes.
            indexVC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    vacuna = newValue.getValue();
                    log.info("Item selected.");
                }
            });

            btnShow.setOnAction((event) -> {
                if (paciente != null)
                    displayModal(event);
                else
                    DialogBox.displayWarning();
            });

            btnDelete.setOnAction((event) -> {
                if (paciente != null)
                    if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                        dao.delete(paciente.getId());
                        TreeItem<Vacunas> selectedItem = indexVC.getSelectionModel().getSelectedItem();
                        indexVC.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        refreshTable();
                        paciente = null;
                        log.info("Item deleted.");
                    }
            });
            // TODO add search filter
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    } // Pacientes

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/vaccine/modalDialog.fxml"));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        vacuna = dao.showById(paciente.getId());
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(vacuna);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Vacuna");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                indexVC.refresh();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        vaccineList.clear();
        vaccineList.setAll(dao.showByPatient(paciente));
        root = new RecursiveTreeItem<Vacunas>(vaccineList, RecursiveTreeObject::getChildren);
        indexVC.setRoot(root);
    }
}
