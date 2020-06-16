package controller.currentAccount;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.CuentasCorrientesHome;
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
import model.Propietarios;
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
    private TableView<Propietarios> indexCA;

    @FXML
    private Pagination tablePagination;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableColumn<Propietarios, String> tcNombre;

    @FXML
    private TableColumn<Propietarios, String> tcApellido;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private CuentasCorrientesHome dao = new CuentasCorrientesHome();

    private Propietarios propietario;

    final ObservableList<Propietarios> propietariosList = FXCollections.observableArrayList();

    private FilteredList<Propietarios> filteredData;

    @FXML
    void initialize() {
        // this should be a helper class to load everything
        log.info(marker, "creating table");

        tcNombre.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getNombre()));

        tcApellido.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getApellido()));

        log.info(marker, "loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                propietario = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew());

        btnShow.setOnAction((event) -> {
            if (propietario != null)
                displayShow();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (propietario != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.deleteAll(propietario.getId());
                    Propietarios selectedItem = indexCA.getSelectionModel().getSelectedItem();
                    propietariosList.remove(selectedItem);
                    indexCA.setItems(propietariosList);
                    refreshTable();
                    propietario = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(propietariosList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cuenta -> newValue == null || newValue.isEmpty()
                    || cuenta.getNombre().toLowerCase().contains(newValue.toLowerCase())
                    || cuenta.getApellido().toLowerCase().contains(newValue.toLowerCase()));
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
     * Displays current accounts by mascot's owner.
     */
    private void displayShow() {
//        ViewSwitcher.loadView(Route.CUENTACORRIENTE.showView());
        ShowController sc = ViewSwitcher.getController(Route.CUENTACORRIENTE.showView());
//        ViewSwitcher.loadingDialog.showStage();
        sc.setObject(propietario);
        sc.loadDao();
        String path[] = { "Cuenta Corriente", "Índice", propietario.getApellido() + ", " + propietario.getNombre() };
        ViewSwitcher.setPath(path);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void displayNew() {
        ViewSwitcher.loadModal(Route.CUENTACORRIENTE.newView(), "Nuevo elemento - Cuenta Corriente", true);
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshTable();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void refreshTable() {
        propietariosList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, propietariosList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Propietarios> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCA.comparatorProperty());
        indexCA.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Propietarios>> task = dao.displayRecordsWithOwners();

        task.setOnSucceeded(event -> {
            propietariosList.setAll(task.getValue());
            indexCA.setItems(propietariosList);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexCA, propietariosList, tablePagination, index, 20));
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
