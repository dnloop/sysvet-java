package controller.internation;

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

import dao.InternacionesHome;
import javafx.application.Platform;
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
import model.Internaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.Route;
import utils.TableUtil;
import utils.ViewSwitcher;

public class ShowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<Internaciones> indexI;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private InternacionesHome dao = new InternacionesHome();

    private Internaciones internacion;

    private Pacientes paciente;

    final ObservableList<Internaciones> fichasList = FXCollections.observableArrayList();

    private TreeItem<Internaciones> root;

    // Table columns

    private JFXTreeTableColumn<Internaciones, Pacientes> pacientes = new JFXTreeTableColumn<Internaciones, Pacientes>(
            "Pacientes - (ficha)");

    private JFXTreeTableColumn<Internaciones, Date> fechaIngreso = new JFXTreeTableColumn<Internaciones, Date>(
            "Fecha Ingreso");

    private JFXTreeTableColumn<Internaciones, Date> fechaAlta = new JFXTreeTableColumn<Internaciones, Date>(
            "Fecha Alta");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexI != null : "fx:id=\"indexI\" was not injected: check your FXML file 'show.fxml'.";

        Platform.runLater(() -> {
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<Internaciones, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getFichasClinicas().getPacientes()));

            fechaIngreso.setPrefWidth(150);
            fechaIngreso.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Internaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFechaIngreso()));

            fechaAlta.setPrefWidth(150);
            fechaAlta.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Internaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFechaAlta()));

            log.info("loading table items");
            fichasList.setAll(dao.showByPatient(paciente));

            root = new RecursiveTreeItem<Internaciones>(fichasList, RecursiveTreeObject::getChildren);

            indexI.getColumns().setAll(pacientes, fechaIngreso, fechaAlta);
            indexI.setShowRoot(false);
            indexI.setRoot(root);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexI, fichasList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    internacion = newValue.getValue();
                    log.info("Item selected." + internacion.getId());
                }
            });

            btnEdit.setOnAction((event) -> {
                if (internacion != null)
                    displayModal(event);
                else
                    DialogBox.displayWarning();
            });

            btnDelete.setOnAction((event) -> {
                if (internacion != null)
                    if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                        dao.delete(internacion.getId());
                        TreeItem<Internaciones> selectedItem = indexI.getSelectionModel().getSelectedItem();
                        indexI.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        indexI.refresh();
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.INTERNACION.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(internacion);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Editar - Internación");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                refreshTable();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        fichasList.clear();
        fichasList.setAll(dao.showByPatient(paciente));
        root = new RecursiveTreeItem<Internaciones>(fichasList, RecursiveTreeObject::getChildren);
        indexI.setRoot(root);
        tablePagination.setPageFactory((index) -> TableUtil.createPage(indexI, fichasList, tablePagination, index, 20));
    }
}
