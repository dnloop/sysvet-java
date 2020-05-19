package controller.patient;

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

import dao.PacientesHome;
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
import model.Pacientes;
import model.Propietarios;
import utils.DialogBox;
import utils.TableUtil;
import utils.routes.Route;
import utils.routes.RouteExtra;
import utils.viewswitcher.ViewSwitcher;

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
    private TableView<Pacientes> indexPA;

    @FXML
    private Pagination tablePagination;

    @FXML
    TableColumn<Pacientes, String> nombre;

    @FXML
    TableColumn<Pacientes, String> especie;

    @FXML
    TableColumn<Pacientes, String> raza;

    @FXML
    TableColumn<Pacientes, String> sexo;

    @FXML
    TableColumn<Pacientes, String> temp;

    @FXML
    TableColumn<Pacientes, String> pelaje;

    @FXML
    TableColumn<Pacientes, Date> fecha;

    @FXML
    private TableColumn<Pacientes, Propietarios> propietario;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private PacientesHome dao = new PacientesHome();

    private Pacientes patient;

    private final ObservableList<Pacientes> patientsList = FXCollections.observableArrayList();

    private FilteredList<Pacientes> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {

        log.info(marker, "creating table");
        nombre.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getNombre()));

        especie.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getEspecie()));

        raza.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getRaza()));

        sexo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getSexo()));

        temp.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTemperamento()));

        pelaje.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPelaje()));

        fecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaNacimiento()));

        propietario.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Propietarios>(param.getValue().getPropietarios()));
        log.info(marker, "loading table items");

        loadDao();
        indexPA.getColumns().setAll(nombre, especie, raza, sexo, temp, pelaje, fecha, propietario);

        // Handle ListView selection changes.
        indexPA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                patient = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew());

        btnShow.setOnAction((event) -> {
            if (patient != null)
                displayShow();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (patient != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(patient.getId());
                    Pacientes selectedItem = indexPA.getSelectionModel().getSelectedItem();
                    patientsList.remove(selectedItem);
                    indexPA.setItems(patientsList);
                    refreshTable();
                    patient = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        filteredData = new FilteredList<>(patientsList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(paciente -> newValue == null || newValue.isEmpty()
                    || paciente.getNombre().toLowerCase().contains(newValue.toLowerCase())
                    || paciente.getPropietarios().toString().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /*
     * Class Methods
     */

    public static void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayShow() {
        ViewSwitcher.loadView(RouteExtra.PACIENTEMAIN.getPath());
        MainController mc = ViewSwitcher.getController(RouteExtra.PACIENTEMAIN.getPath());
        mc.setObject(patient);
        mc.loadPanes();
        String path[] = { "Paciente", "Índice", patient.getNombre() };
        ViewSwitcher.setPath(path);
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void displayNew() {
        ViewSwitcher.loadModal(Route.PACIENTE.newView(), "Nuevo elemento - Paciente", true);
        ViewSwitcher.modalStage.setOnHiding(stageEvent -> {
            refreshTable();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void refreshTable() {
        patientsList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, patientsList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Pacientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexPA.comparatorProperty());
        indexPA.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Pacientes>> task = dao.displayRecords();

        task.setOnSucceeded(event -> {
            patientsList.setAll(task.getValue());
            indexPA.setItems(patientsList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexPA, patientsList, tablePagination, index, 20));
            ViewSwitcher.loadingDialog.getStage().close();
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
    }
}
