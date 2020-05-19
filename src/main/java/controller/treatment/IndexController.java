package controller.treatment;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.TratamientosHome;
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
    private TableView<FichasClinicas> indexTR;

    @FXML
    private Pagination tablePagination;
    // Table columns
    @FXML
    private TableColumn<FichasClinicas, Pacientes> pacientes;

    @FXML
    private TableColumn<FichasClinicas, String> fichaID;

    @FXML
    private TableColumn<FichasClinicas, String> motivo;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private TratamientosHome dao = new TratamientosHome();

    private FichasClinicas ficha;

    private final ObservableList<FichasClinicas> pacientesList = FXCollections.observableArrayList();

    private FilteredList<FichasClinicas> filteredData;

    @FXML
    void initialize() {
        log.info(marker, "creating table");
        pacientes.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getPacientes()));

        fichaID.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getId().toString()));

        motivo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getMotivoConsulta()));

        log.info(marker, "loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexTR.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ficha = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew());

        btnShow.setOnAction((event) -> {
            if (ficha != null)
                displayShow();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (ficha != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.deleteAll(ficha.getId());
                    FichasClinicas selectedItem = indexTR.getSelectionModel().getSelectedItem();
                    indexTR.getItems().remove(selectedItem);
                    indexTR.refresh();
                    ficha = null;
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
                    || ficha.toString().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getMotivoConsulta().toLowerCase().contains(newValue.toLowerCase()));
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
     * Displays treatments by patient's clinical file.
     */
    private void displayShow() {
        ViewSwitcher.loadView(Route.TRATAMIENTO.showView());
        ShowController sc = ViewSwitcher.getController(Route.TRATAMIENTO.showView());
        sc.setObject(ficha);
        sc.loadDao();
        String path[] = { "Tratamiento", "Índice", ficha.getPacientes().toString() };
        ViewSwitcher.setPath(path);
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();

    }

    private void displayNew() {
        ViewSwitcher.loadModal(Route.TRATAMIENTO.newView(), "Nuevo elemento - Tratamiento", true);
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
        SortedList<FichasClinicas> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexTR.comparatorProperty());
        indexTR.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<FichasClinicas>> task = dao.displayRecordsWithFichas();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            indexTR.setItems(pacientesList);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexTR, pacientesList, tablePagination, index, 20));
            ViewSwitcher.loadingDialog.getStage().close();
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
    }
}
