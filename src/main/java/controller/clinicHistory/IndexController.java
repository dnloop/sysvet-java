package controller.clinicHistory;

import java.net.URL;
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
    private TableView<FichasClinicas> indexCH;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<FichasClinicas, Pacientes> tcPaciente;

    @FXML
    private TableColumn<FichasClinicas, Integer> tcFicha;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private HistoriaClinicaHome daoHC = new HistoriaClinicaHome();

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    final ObservableList<FichasClinicas> fichasList = FXCollections.observableArrayList();

    private FichasClinicas fichaClinica;

    private FilteredList<FichasClinicas> filteredData;

    @FXML
    void initialize() {

        tcPaciente
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getPacientes()));

        tcFicha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Integer>(param.getValue().getId()));

        log.info(marker, "loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexCH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fichaClinica = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew());

        btnShow.setOnAction((event) -> {
            if (fichaClinica != null)
                displayShow();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (fichaClinica != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    daoHC.deleteAll(fichaClinica.getId());
                    FichasClinicas selectedItem = indexCH.getSelectionModel().getSelectedItem();
                    fichasList.remove(selectedItem);
                    indexCH.setItems(fichasList);
                    refreshTable();
                    fichaClinica = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        filteredData = new FilteredList<>(fichasList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> newValue == null || newValue.isEmpty()
                    || ficha.getPacientes().getNombre().toLowerCase().contains(newValue.toLowerCase()));
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
     * Displays clinic history by a patient's clinical file.
     */
    private void displayShow() {
        ViewSwitcher.loadView(Route.HISTORIACLINICA.showView());
        ShowController sc = ViewSwitcher.getController(Route.HISTORIACLINICA.showView());
        sc.setObject(fichaClinica);
        sc.loadDao();
        String path[] = { "Historia Clínica", "Índice", fichaClinica.getPacientes().toString() };
        ViewSwitcher.setPath(path);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void displayNew() {
        ViewSwitcher.loadModal(Route.HISTORIACLINICA.newView(), "Nuevo elemento - Historia Clínica", true);
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshTable();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void refreshTable() {
        fichasList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, fichasList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<FichasClinicas> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCH.comparatorProperty());
        indexCH.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<FichasClinicas>> task = daoHC.displayRecordsWithClinicHistory();

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            indexCH.setItems(fichasList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCH, fichasList, tablePagination, index, 20));
            log.info(marker, "Table loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
