package controller.clinicHistory;

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

import dao.HistoriaClinicaHome;
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
import model.HistoriaClinica;
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
    private JFXTreeTableView<HistoriaClinica> indexCH;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private HistoriaClinicaHome dao = new HistoriaClinicaHome();

    private HistoriaClinica historiaClinica;

    private Pacientes paciente;

    final ObservableList<HistoriaClinica> historiaList = FXCollections.observableArrayList();

    private TreeItem<HistoriaClinica> root;

    // Table Columns

    private JFXTreeTableColumn<HistoriaClinica, Pacientes> pacientes = new JFXTreeTableColumn<HistoriaClinica, Pacientes>(
            "Pacientes - (ficha)");

    private JFXTreeTableColumn<HistoriaClinica, String> descripcionEvento = new JFXTreeTableColumn<HistoriaClinica, String>(
            "Descripción de evento");

    private JFXTreeTableColumn<HistoriaClinica, Date> fechaInicio = new JFXTreeTableColumn<HistoriaClinica, Date>(
            "Fecha Inicio");

    private JFXTreeTableColumn<HistoriaClinica, Date> fechaResolucion = new JFXTreeTableColumn<HistoriaClinica, Date>(
            "Fecha Resolución");

    private JFXTreeTableColumn<HistoriaClinica, String> resultado = new JFXTreeTableColumn<HistoriaClinica, String>(
            "Resultado");

    private JFXTreeTableColumn<HistoriaClinica, String> secuelas = new JFXTreeTableColumn<HistoriaClinica, String>(
            "Secuelas");

    private JFXTreeTableColumn<HistoriaClinica, String> consideraciones = new JFXTreeTableColumn<HistoriaClinica, String>(
            "Considetaciones complementarias");

    private JFXTreeTableColumn<HistoriaClinica, String> comentarios = new JFXTreeTableColumn<HistoriaClinica, String>(
            "Comentarios");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexCH != null : "fx:id=\"indexCH\" was not injected: check your FXML file 'show.fxml'.";
        Platform.runLater(() -> {

            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<HistoriaClinica, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getFichasClinicas().getPacientes()));

            descripcionEvento.setPrefWidth(200);
            descripcionEvento.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getDescripcionEvento())));

            fechaInicio.setPrefWidth(150);
            fechaInicio.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFechaInicio()));

            fechaResolucion.setPrefWidth(150);
            fechaResolucion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFechaResolucion()));

            resultado.setPrefWidth(200);
            resultado.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getResultado())));

            secuelas.setPrefWidth(200);
            secuelas.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getSecuelas())));

            consideraciones.setPrefWidth(200);
            consideraciones.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getConsideraciones())));

            comentarios.setPrefWidth(200);
            comentarios.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getComentarios())));

            log.info("loading table items");

            historiaList.setAll(dao.showByPatient(paciente));
            root = new RecursiveTreeItem<HistoriaClinica>(historiaList, RecursiveTreeObject::getChildren);

            indexCH.getColumns().setAll(pacientes, descripcionEvento, fechaInicio, fechaResolucion, resultado, secuelas,
                    consideraciones, comentarios);
            indexCH.setShowRoot(false);
            indexCH.setRoot(root);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCH, historiaList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexCH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    historiaClinica = newValue.getValue();
                    log.info("Item selected.");
                }
            });

            btnEdit.setOnAction((event) -> {
                if (historiaClinica != null)
                    displayModal(event);
                else
                    DialogBox.displayWarning();
            });

            btnDelete.setOnAction((event) -> {
                if (historiaClinica != null)
                    if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                        dao.delete(historiaClinica.getId());
                        TreeItem<HistoriaClinica> selectedItem = indexCH.getSelectionModel().getSelectedItem();
                        indexCH.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        indexCH.refresh();
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
    } // FichasClinicas

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.HISTORIACLINICA.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(historiaClinica);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Historia Clinica");
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
        historiaList.clear();
        historiaList.setAll(dao.showByPatient(paciente));
        root = new RecursiveTreeItem<HistoriaClinica>(historiaList, RecursiveTreeObject::getChildren);
        indexCH.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexCH, historiaList, tablePagination, index, 20));
    }
}
