package controller.clinicHistory;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.HistoriaClinicaHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.FichasClinicas;
import model.HistoriaClinica;
import model.Pacientes;
import utils.DialogBox;
import utils.TableUtil;
import utils.ViewSwitcher;
import utils.routes.Route;

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
    private TableView<HistoriaClinica> indexCH;

    @FXML
    private Pagination tablePagination;

    // Table Columns
    @FXML
    private TableColumn<HistoriaClinica, Pacientes> tcPaciente;

    @FXML
    private TableColumn<HistoriaClinica, String> tcDescripcionEvento;

    @FXML
    private TableColumn<HistoriaClinica, Date> tcFechaInicio;

    @FXML
    private TableColumn<HistoriaClinica, Date> tcFechaResolucion;

    @FXML
    private TableColumn<HistoriaClinica, String> tcResultado;

    @FXML
    private TableColumn<HistoriaClinica, String> tcSecuelas;

    @FXML
    private TableColumn<HistoriaClinica, String> tcConsideraciones;

    @FXML
    private TableColumn<HistoriaClinica, String> tcComentarios;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private HistoriaClinicaHome dao = new HistoriaClinicaHome();

    private HistoriaClinica historiaClinica;

    private FichasClinicas fichaClinica;

    final ObservableList<HistoriaClinica> historiaList = FXCollections.observableArrayList();

    private FilteredList<HistoriaClinica> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexCH != null : "fx:id=\"indexCH\" was not injected: check your FXML file 'show.fxml'.";
        Platform.runLater(() -> {

            tcPaciente.setCellValueFactory((
                    TableColumn.CellDataFeatures<HistoriaClinica, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getFichasClinicas().getPacientes()));

            tcDescripcionEvento.setCellValueFactory(
                    (TableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getDescripcionEvento())));

            tcFechaResolucion.setCellValueFactory(
                    (TableColumn.CellDataFeatures<HistoriaClinica, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getFechaResolucion()));

            tcFechaInicio.setCellValueFactory(
                    (TableColumn.CellDataFeatures<HistoriaClinica, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getFechaInicio()));

            tcResultado.setCellValueFactory(
                    (TableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getResultado())));

            tcSecuelas.setCellValueFactory(
                    (TableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getSecuelas())));

            tcConsideraciones.setCellValueFactory(
                    (TableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getConsideraciones())));

            tcComentarios.setCellValueFactory(
                    (TableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getComentarios())));

            log.info("loading table items");

            historiaList.setAll(dao.showByPatient(fichaClinica));

            indexCH.getColumns().setAll(tcPaciente, tcDescripcionEvento, tcFechaInicio, tcFechaResolucion, tcResultado,
                    tcSecuelas, tcConsideraciones, tcComentarios);

            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCH, historiaList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexCH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    historiaClinica = newValue;
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
                if (historiaClinica != null) {
                    if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                        dao.delete(historiaClinica.getId());
                        HistoriaClinica selectedItem = indexCH.getSelectionModel().getSelectedItem();
                        indexCH.getItems().remove(selectedItem);
                        indexCH.refresh();
                        historiaClinica = null;
                        DialogBox.displaySuccess();
                        log.info("Item deleted.");
                    }
                } else
                    DialogBox.displayWarning();
            });
            // search filter
            filteredData = new FilteredList<>(historiaList, p -> true);
            txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(historia -> newValue == null || newValue.isEmpty() || historia
                        .getFichasClinicas().getPacientes().getNombre().toLowerCase().contains(newValue.toLowerCase()));
                changeTableView(tablePagination.getCurrentPageIndex(), 20);
            });
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setObject(FichasClinicas fichaClinica) {
        this.fichaClinica = fichaClinica;
    } // FichasClinicas

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        ModalDialogController mc = vs.loadModal(Route.HISTORIACLINICA.modalView(), "Historia Clinica", event);
        mc.setObject(historiaClinica);
        vs.getStage().setOnHidden((stageEvent) -> {
            refreshTable();
        });
        mc.showModal(vs.getStage());
    }

    private void refreshTable() {
        historiaList.clear();
        historiaList.setAll(dao.showByPatient(fichaClinica));
        indexCH.setItems(historiaList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexCH, historiaList, tablePagination, index, 20));
    }

    private void changeTableView(int index, int limit) {

        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, historiaList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<HistoriaClinica> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCH.comparatorProperty());

        indexCH.setItems(sortedData);

    }
}
