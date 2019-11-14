package controller.clinicHistory;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
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

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private HistoriaClinicaHome dao = new HistoriaClinicaHome();

    private HistoriaClinica historiaClinica;

    private FichasClinicas fichaClinica;

    final ObservableList<HistoriaClinica> historiaList = FXCollections.observableArrayList();

    private FilteredList<HistoriaClinica> filteredData;

    @FXML
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'show.fxml'.";
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'show.fxml'.";
        assert indexCH != null : "fx:id=\"indexCH\" was not injected: check your FXML file 'show.fxml'.";
        assert tcPaciente != null : "fx:id=\"tcPaciente\" was not injected: check your FXML file 'show.fxml'.";
        assert tcDescripcionEvento != null : "fx:id=\"tcDescripcionEvento\" was not injected: check your FXML file 'show.fxml'.";
        assert tcFechaInicio != null : "fx:id=\"tcFechaInicio\" was not injected: check your FXML file 'show.fxml'.";
        assert tcFechaResolucion != null : "fx:id=\"tcFechaResolucion\" was not injected: check your FXML file 'show.fxml'.";
        assert tcResultado != null : "fx:id=\"tcResultado\" was not injected: check your FXML file 'show.fxml'.";
        assert tcSecuelas != null : "fx:id=\"tcSecuelas\" was not injected: check your FXML file 'show.fxml'.";
        assert tcConsideraciones != null : "fx:id=\"tcConsideraciones\" was not injected: check your FXML file 'show.fxml'.";
        assert tcComentarios != null : "fx:id=\"tcComentarios\" was not injected: check your FXML file 'show.fxml'.";

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
                log.info("Item selected.");
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController ic = new IndexController();
            ic.setView(Route.HISTORIACLINICA.indexView());
            String path[] = { "Historia Clínica", "Índice" };
            ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
            ViewSwitcher.getLoadingDialog().startTask();
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
                    historiaList.remove(selectedItem);
                    indexCH.setItems(historiaList);
                    refreshTable();
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
            filteredData.setPredicate(historia -> newValue == null || newValue.isEmpty() || historia.getFichasClinicas()
                    .getPacientes().getNombre().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
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
        ModalDialogController mc = vs.loadModal(Route.HISTORIACLINICA.modalView(), "Historia Clínica", event);
        vs.getStage().setOnHidden((stageEvent) -> {
            indexCH.refresh();
        });
        mc.setObject(historiaClinica);
        mc.showModal(vs.getStage());
    }

    private void refreshTable() {
        historiaList.clear();
        loadDao();
        ViewSwitcher.getLoadingDialog().startTask();
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
        log.info("Loading table items");
        Task<List<HistoriaClinica>> task = dao.showByPatient(fichaClinica);

        task.setOnSucceeded(event -> {
            historiaList.setAll(task.getValue());
            indexCH.setItems(historiaList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCH, historiaList, tablePagination, index, 20));
            ViewSwitcher.getLoadingDialog().getStage().close();
            log.info("Table loaded.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}
