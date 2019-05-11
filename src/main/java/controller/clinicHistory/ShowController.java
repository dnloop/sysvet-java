package controller.clinicHistory;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.FichasClinicas;
import model.HistoriaClinica;
import model.Pacientes;
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

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private HistoriaClinicaHome dao = new HistoriaClinicaHome();

    private HistoriaClinica historiaClinica;

    private Integer id;

    private FichasClinicas fc;

    final ObservableList<HistoriaClinica> historiaList = FXCollections.observableArrayList();

    TreeItem<HistoriaClinica> root;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexCH != null : "fx:id=\"indexCH\" was not injected: check your FXML file 'show.fxml'.";
        Platform.runLater(() -> {
            JFXTreeTableColumn<HistoriaClinica, Pacientes> pacientes = new JFXTreeTableColumn<HistoriaClinica, Pacientes>(
                    "Pacientes - (ficha)");
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<HistoriaClinica, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getFichasClinicas().getPacientes()));

            JFXTreeTableColumn<HistoriaClinica, String> descripcionEvento = new JFXTreeTableColumn<HistoriaClinica, String>(
                    "Descripción de evento");
            descripcionEvento.setPrefWidth(200);
            descripcionEvento.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getDescripcionEvento())));

            JFXTreeTableColumn<HistoriaClinica, Date> fechaInicio = new JFXTreeTableColumn<HistoriaClinica, Date>(
                    "Fecha Inicio");
            fechaInicio.setPrefWidth(150);
            fechaInicio.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFechaInicio()));

            JFXTreeTableColumn<HistoriaClinica, Date> fechaResolucion = new JFXTreeTableColumn<HistoriaClinica, Date>(
                    "Fecha Resolución");
            fechaResolucion.setPrefWidth(150);
            fechaResolucion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFechaResolucion()));

            JFXTreeTableColumn<HistoriaClinica, String> resultado = new JFXTreeTableColumn<HistoriaClinica, String>(
                    "Resultado");
            resultado.setPrefWidth(200);
            resultado.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getResultado())));

            JFXTreeTableColumn<HistoriaClinica, String> secuelas = new JFXTreeTableColumn<HistoriaClinica, String>(
                    "Secuelas");
            secuelas.setPrefWidth(200);
            secuelas.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getSecuelas())));

            JFXTreeTableColumn<HistoriaClinica, String> consideraciones = new JFXTreeTableColumn<HistoriaClinica, String>(
                    "Considetaciones complementarias");
            consideraciones.setPrefWidth(200);
            consideraciones.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getConsideraciones())));

            JFXTreeTableColumn<HistoriaClinica, String> comentarios = new JFXTreeTableColumn<HistoriaClinica, String>(
                    "Comentarios");
            comentarios.setPrefWidth(200);
            comentarios.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getComentarios())));

            log.info("loading table items");

            historiaList.setAll(loadTable(fc));
            root = new RecursiveTreeItem<HistoriaClinica>(historiaList, RecursiveTreeObject::getChildren);

            indexCH.getColumns().setAll(pacientes, descripcionEvento, fechaInicio, fechaResolucion, resultado, secuelas,
                    consideraciones, comentarios);
            indexCH.setShowRoot(false);
            indexCH.setRoot(root);

            // Handle ListView selection changes.
            indexCH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    historiaClinica = newValue.getValue();
                    id = historiaClinica.getId();
                    log.info("Item selected.");
                }
            });

            btnEdit.setOnAction((event) -> {
                if (id != null)
                    displayModal(event);
                else
                    displayWarning();
            });

            btnDelete.setOnAction((event) -> {
                if (id != null)
                    confirmDialog();
                else
                    displayWarning();
            });
            // TODO add search filter
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setObject(FichasClinicas fc) {
        this.fc = fc;
    } // FichasClinicas

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private ObservableList<HistoriaClinica> loadTable(FichasClinicas id) {
        ObservableList<HistoriaClinica> historiaList = FXCollections.observableArrayList();
        List<HistoriaClinica> list = dao.showByFicha(id);
        historiaList.addAll(list);
        return historiaList;
    }

    private void confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción.");
        alert.setContentText("¿Desea eliminar el registro?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.delete(id);
            TreeItem<HistoriaClinica> selectedItem = indexCH.getSelectionModel().getSelectedItem();
            indexCH.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
            indexCH.refresh();
            log.info("Item deleted.");
        }
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/clinicHistory/modalDialog.fxml"));
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

    private void displayWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia.");
        alert.setHeaderText("Elemento vacío.");
        alert.setContentText("No se seleccionó ningún elemento de la lista. Elija un ítem e intente nuevamente.");
        alert.setResizable(true);

        alert.showAndWait();
    }

    private void refreshTable() {
        historiaList.clear();
        historiaList.setAll(loadTable(fc));
        root = new RecursiveTreeItem<HistoriaClinica>(historiaList, RecursiveTreeObject::getChildren);
        indexCH.setRoot(root);
    }
}
