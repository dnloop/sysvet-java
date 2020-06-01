package controller.hospitalization;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.InternacionesHome;
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

/**
 * @author dnloop
 *
 */
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
    private TableView<Pacientes> indexI;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<Pacientes, Pacientes> tcPacientes;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private InternacionesHome dao = new InternacionesHome();

    final ObservableList<Pacientes> hospitalList = FXCollections.observableArrayList();

    private FilteredList<Pacientes> filteredData;

    private Pacientes paciente;

    @FXML
    void initialize() {

        tcPacientes.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue()));

        log.info(marker, "Loading table items.");
        loadDao();

        // Handle ListView selection changes.
        indexI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
                    Pacientes selectedItem = indexI.getSelectionModel().getSelectedItem();
                    hospitalList.remove(selectedItem);
                    indexI.setItems(hospitalList);
                    refreshTable();
                    paciente = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        filteredData = new FilteredList<>(hospitalList, p -> true);
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
     * Displays the list of hospitalizations by patient.
     */
    private void displayShow() {
        ViewSwitcher.loadView(Route.INTERNACION.showView());
        ShowController sc = ViewSwitcher.getController(Route.INTERNACION.showView());
        sc.setObject(paciente);
        sc.loadDao();
        String path[] = { "Internación", "Índice", paciente.getNombre() };
        ViewSwitcher.setPath(path);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void displayNew() {
        ViewSwitcher.loadModal(Route.INTERNACION.newView(), "Nuevo elemento - Internaciones", true);
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshTable();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void refreshTable() {
        hospitalList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, hospitalList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Pacientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexI.comparatorProperty());
        indexI.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Pacientes>> task = dao.displayRecordsWithPatients();

        task.setOnSucceeded(event -> {
            hospitalList.setAll(task.getValue());
            indexI.setItems(hospitalList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexI, hospitalList, tablePagination, index, 20));
            log.info(marker, "Table loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
