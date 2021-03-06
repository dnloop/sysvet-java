package controller.clinicHistory;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.HistoriaClinicaHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.FichasClinicas;
import model.HistoriaClinica;
import model.Pacientes;
import utils.DialogBox;
import utils.TableUtil;
import utils.routes.Route;
import utils.viewswitcher.ViewSwitcher;

public class ShowController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnBack;

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

    private static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private HistoriaClinicaHome dao = new HistoriaClinicaHome();

    private HistoriaClinica historiaClinica;

    private FichasClinicas fichaClinica;

    private final ObservableList<HistoriaClinica> historiaList = FXCollections.observableArrayList();

    private FilteredList<HistoriaClinica> filteredData;

    @FXML
    void initialize() {

        tcPaciente.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getFichasClinicas().getPacientes()));

        tcDescripcionEvento.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDescripcionEvento())));

        tcFechaResolucion
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaResolucion()));

        tcFechaInicio
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaInicio()));

        tcResultado.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getResultado())));

        tcSecuelas.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getSecuelas())));

        tcConsideraciones.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getConsideraciones())));

        tcComentarios.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getComentarios())));

        // Handle ListView selection changes.
        indexCH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                historiaClinica = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController.setView(Route.HISTORIACLINICA.indexView());
            String path[] = { "Historia Clínica", "Índice" };
            ViewSwitcher.setPath(path);
        });

        btnEdit.setOnAction((event) -> {
            if (historiaClinica != null)
                displayModal();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (historiaClinica != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(historiaClinica.getId());
                    HistoriaClinica selectedItem = indexCH.getSelectionModel().getSelectedItem();
                    historiaList.remove(selectedItem);
                    indexCH.setItems(historiaList);
                    refreshTable();
                    historiaClinica = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(historiaList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(historia -> newValue == null || newValue.isEmpty() || historia.getFichasClinicas()
                    .getPacientes().getNombre().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /*
     * Class Methods
     */

    public void setObject(FichasClinicas fichaClinica) {
        this.fichaClinica = fichaClinica;
    } // FichasClinicas

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal() {
        ViewSwitcher.loadModal(Route.HISTORIACLINICA.modalView(), "Historia Clínica", true);
        ModalDialogController mc = ViewSwitcher.getController(Route.HISTORIACLINICA.modalView());
        ViewSwitcher.modalStage.setOnHidden((stageEvent) -> {
            indexCH.refresh();
        });
        mc.setObject(historiaClinica);
        ViewSwitcher.modalStage.showAndWait();
    }

    private void refreshTable() {
        historiaList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
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

    void loadDao() {
        log.info(marker, "Loading table items");
        Task<List<HistoriaClinica>> task = dao.showByPatient(fichaClinica);

        task.setOnSucceeded(event -> {
            historiaList.setAll(task.getValue());
            indexCH.setItems(historiaList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCH, historiaList, tablePagination, index, 20));
            ViewSwitcher.loadingDialog.getStage().close();
            log.info(marker, "Table loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
