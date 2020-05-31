package controller.clinicalFile;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.FichasClinicasHome;
import dao.PacientesHome;
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
    private TableView<Pacientes> indexCF;

    @FXML
    private TableColumn<Pacientes, Pacientes> tcPaciente;

    @FXML
    private Pagination tablePagination;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private PacientesHome daoPA = new PacientesHome();

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private Pacientes paciente;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private FilteredList<Pacientes> filteredData;

    @FXML
    void initialize() {

        tcPaciente.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue()));

        log.info(marker, "loading table items");
        loadDao();
        // Handle ListView selection changes.
        indexCF.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
                    daoFC.deleteAll(paciente.getId());
                    Pacientes selectedItem = indexCF.getSelectionModel().getSelectedItem();
                    pacientesList.remove(selectedItem);
                    indexCF.setItems(pacientesList);
                    paciente = null;
                    refreshTable();
                    DialogBox.displaySuccess();
                    log.info(marker, "Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(pacientesList, p -> true);
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
     * Displays the list of clinical files by patient.
     */
    private void displayShow() {
        ViewSwitcher.loadView(Route.FICHACLINICA.showView());
        ShowController sc = ViewSwitcher.getController(Route.FICHACLINICA.showView());
        sc.setObject(paciente);
        sc.loadDao();
        String path[] = { "Ficha Clínica", paciente.getNombre(), "Fichas" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void displayNew() {
        ViewSwitcher.loadModal(Route.FICHACLINICA.newView(), "Nuevo elemento - Ficha Clínica", true);
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshTable();
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
        sortedData.comparatorProperty().bind(indexCF.comparatorProperty());
        indexCF.setItems(sortedData);
    }

    private void loadDao() {
        log.info("Loading table items.");
        Task<List<Pacientes>> task = daoPA.displayRecordsWithClinicalFiles();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            indexCF.setItems(pacientesList);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexCF, pacientesList, tablePagination, index, 20));
            ViewSwitcher.loadingDialog.getStage().close();
            log.info(marker, "Table Loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }

}
