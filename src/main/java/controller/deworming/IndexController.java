package controller.deworming;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.DesparasitacionesHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import utils.DialogBox;
import utils.TableUtil;
import utils.routes.Route;
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
    private TableView<Pacientes> indexD;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<Pacientes, Pacientes> tcPaciente;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private DesparasitacionesHome dao = new DesparasitacionesHome();

    private Pacientes paciente;

    final ObservableList<Pacientes> pacienteList = FXCollections.observableArrayList();

    private FilteredList<Pacientes> filteredData;

    @FXML
    void initialize() {

        log.info(marker, "creating table");
        tcPaciente.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue()));

        log.info(marker, "loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexD.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                paciente = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew());

        btnShow.setOnAction((event) -> {
            if (paciente != null)
                displayShow();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (paciente != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.deleteAll(paciente.getId());
                    Pacientes selectedItem = indexD.getSelectionModel().getSelectedItem();
                    pacienteList.remove(selectedItem);
                    indexD.setItems(pacienteList);
                    refreshTable();
                    paciente = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item deleted.");
                }
            } else
                DialogBox.displayWarning();

        });

        // search filter
        filteredData = new FilteredList<>(pacienteList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(paciente -> newValue == null || newValue.isEmpty()
                    || paciente.getNombre().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /*
     * Class Methods
     */

    public static void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    /**
     * Displays deworming records by patient.
     */
    private void displayShow() {
        ViewSwitcher.loadView(Route.DESPARASITACION.showView());
        ShowController sc = ViewSwitcher.getController(Route.DESPARASITACION.showView());
        sc.setObject(paciente);
        sc.loadDao();
        String path[] = { "Desparasitación", "Índice", paciente.getNombre() };
        ViewSwitcher.setPath(path);
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void displayNew() {
        ViewSwitcher.loadModal(Route.DESPARASITACION.newView(), "Nuevo elemento - Desparasitación", true);
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshTable();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void refreshTable() {
        pacienteList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, pacienteList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Pacientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexD.comparatorProperty());
        indexD.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Pacientes>> task = dao.displayRecordsWithPatients();

        task.setOnSucceeded(event -> {
            pacienteList.setAll(task.getValue());
            indexD.setItems(pacienteList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexD, pacienteList, tablePagination, index, 20));
            ViewSwitcher.loadingDialog.getStage().close();
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
