package controller.hospitalization;

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
import model.Internaciones;
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
    private TableView<Internaciones> indexI;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<Internaciones, Date> tcFechaIngreso;

    @FXML
    private TableColumn<Internaciones, Date> tcFechaAlta;

    private static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private InternacionesHome dao = new InternacionesHome();

    private Internaciones internacion;

    private Pacientes paciente;

    final ObservableList<Internaciones> fichasList = FXCollections.observableArrayList();

    private FilteredList<Internaciones> filteredData;

    @FXML
    void initialize() {

        tcFechaIngreso
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaIngreso()));

        tcFechaAlta.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaAlta()));

        // Handle ListView selection changes.
        indexI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                internacion = newValue;
                log.info(marker, "Item selected." + internacion.getId());
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController.setView(Route.INTERNACION.indexView());
            String path[] = { "Internación", "Índice" };
            ViewSwitcher.setPath(path);
        });

        btnEdit.setOnAction((event) -> {
            if (internacion != null)
                displayModal();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (internacion != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(internacion.getId());
                    Internaciones selectedItem = indexI.getSelectionModel().getSelectedItem();
                    fichasList.remove(selectedItem);
                    indexI.setItems(fichasList);
                    indexI.refresh();
                    internacion = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(fichasList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(paciente -> newValue == null || newValue.isEmpty()
                    || paciente.getFechaIngreso().toString().toLowerCase().contains(newValue.toLowerCase())
                    || paciente.getFechaIngreso().toString().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /*
     * Class Methods
     */

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    } // Pacientes

    public static void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal() {
        ViewSwitcher.loadModal(Route.INTERNACION.modalView(), "Editar - Internación", true);
        ModalDialogController mc = ViewSwitcher.getController(Route.INTERNACION.modalView());
        mc.setObject(internacion);
        ViewSwitcher.modalStage.setOnHidden((stageEvent) -> {
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
        SortedList<Internaciones> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexI.comparatorProperty());
        indexI.setItems(sortedData);
    }

    public void loadDao() {
        log.info(marker, "Loading table items");
        Task<List<Internaciones>> task = dao.showByPatient(paciente);

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            indexI.setItems(fichasList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexI, fichasList, tablePagination, index, 20));
            log.info(marker, "Table loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
