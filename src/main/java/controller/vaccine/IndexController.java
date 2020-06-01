package controller.vaccine;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.VacunasHome;
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
    private TableView<Pacientes> indexVC;

    @FXML
    private Pagination tablePagination;

    @FXML
    TableColumn<Pacientes, Pacientes> pacientes;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private static VacunasHome dao = new VacunasHome();

    private final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private FilteredList<Pacientes> filteredData;

    private Pacientes paciente;

    @FXML
    void initialize() {

        pacientes.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue()));

        log.info(marker, "Loading table items.");

        loadDao();

        // Handle ListView selection changes.
        indexVC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
                    Pacientes selectedItem = indexVC.getSelectionModel().getSelectedItem();
                    pacientesList.remove(selectedItem);
                    indexVC.setItems(pacientesList);
                    refreshTable();
                    paciente = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(pacientesList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> newValue == null || newValue.isEmpty()
                    || ficha.toString().toLowerCase().contains(newValue.toLowerCase()));
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
     * Displays vaccination records by patients.
     */
    private void displayShow() {
        ViewSwitcher.loadView(Route.VACUNA.showView());
        ShowController sc = ViewSwitcher.getController(Route.VACUNA.showView());
        sc.setObject(paciente);
        sc.loadDao();
        String path[] = { "Vacunas", "Índice", paciente.getNombre() };
        ViewSwitcher.setPath(path);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void displayNew() {
        ViewSwitcher.loadModal(Route.VACUNA.newView(), "Nuevo elemento - Vacunación", true);
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            indexVC.refresh();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void refreshTable() {
        pacientesList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, pacientesList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Pacientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexVC.comparatorProperty());
        indexVC.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Pacientes>> task = dao.displayRecordsWithVaccines();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            indexVC.setItems(pacientesList);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexVC, pacientesList, tablePagination, index, 20));
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
