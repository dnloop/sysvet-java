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
import javafx.event.Event;
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

    // Table columns
    @FXML
    private TableColumn<Propietarios, String> tcNombre;

    @FXML
    private TableColumn<Propietarios, String> tcApellido;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private CuentasCorrientesHome dao = new CuentasCorrientesHome();

    private Propietarios propietario;

    final ObservableList<Propietarios> propietariosList = FXCollections.observableArrayList();

    private FilteredList<Propietarios> filteredData;

    @FXML
    void initialize() {
        // this should be a helper class to load everything
        log.info("creating table");

        tcNombre.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getNombre()));

        tcApellido.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getApellido()));

        log.info("loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                propietario = newValue;
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnShow.setOnAction((event) -> {
            if (propietario != null)
                displayShow(event);
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
                    log.info("Item deleted.");
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

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayShow(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        ShowController sc = vs.loadNode(Route.CUENTACORRIENTE.showView());
        ViewSwitcher.loadingDialog.showStage();
        sc.setObject(propietario);
        sc.loadDao();
        String path[] = { "Cuenta Corriente", "Índice", propietario.getApellido() + ", " + propietario.getNombre() };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void displayNew(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        NewController nc = vs.loadModal(Route.CUENTACORRIENTE.newView(), "Nuevo elemento - Cuenta Corriente", event);
        vs.getStage().setOnHiding((stageEvent) -> {
            refreshTable();
        });
        nc.showModal(vs.getStage());
        ViewSwitcher.loadingDialog.startTask();
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
            ViewSwitcher.loadingDialog.getStage().close();
            log.info("Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
    }
}
