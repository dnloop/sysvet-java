package controller.currentAccount;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.CuentasCorrientesHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import model.CuentasCorrientes;
import model.Propietarios;
import utils.DialogBox;
import utils.TableUtil;
import utils.ViewSwitcher;
import utils.routes.Route;

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
    private TableView<CuentasCorrientes> indexCA;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<CuentasCorrientes, Propietarios> tcPropietario;

    @FXML
    private TableColumn<CuentasCorrientes, String> tcDescripcion;

    @FXML
    private TableColumn<CuentasCorrientes, BigDecimal> tcMonto;

    @FXML
    private TableColumn<CuentasCorrientes, Date> tcFecha;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private CuentasCorrientesHome dao = new CuentasCorrientesHome();

    private CuentasCorrientes cuentaCorriente;

    private Propietarios propietario;

    final ObservableList<CuentasCorrientes> cuentasList = FXCollections.observableArrayList();

    private FilteredList<CuentasCorrientes> filteredData;

    @FXML
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'show.fxml'.";
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexCA != null : "fx:id=\"indexCA\" was not injected: check your FXML file 'show.fxml'.";
        assert tcPropietario != null : "fx:id=\"tcPropietario\" was not injected: check your FXML file 'show.fxml'.";
        assert tcDescripcion != null : "fx:id=\"tcDescripcion\" was not injected: check your FXML file 'show.fxml'.";
        assert tcMonto != null : "fx:id=\"tcMonto\" was not injected: check your FXML file 'show.fxml'.";
        assert tcFecha != null : "fx:id=\"tcFecha\" was not injected: check your FXML file 'show.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'show.fxml'.";

        log.info("creating table");
        tcPropietario.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Propietarios>(param.getValue().getPropietarios()));

        tcDescripcion.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getDescripcion()));

        tcMonto.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<BigDecimal>(param.getValue().getMonto()));

        tcFecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        // Handle ListView selection changes.
        indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->

        {
            if (newValue != null) {
                cuentaCorriente = newValue;
                log.info("Item selected.");
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController ic = new IndexController();
            ic.setView(Route.CUENTACORRIENTE.indexView());
            String path[] = { "Cuenta Corriente", "Índice" };
            ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
            ViewSwitcher.getLoadingDialog().startTask();
        });

        btnEdit.setOnAction((event) -> {
            if (cuentaCorriente != null)
                displayModal(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (cuentaCorriente != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?"))
                    try {
                        dao.delete(cuentaCorriente.getId());
                        CuentasCorrientes selectedItem = indexCA.getSelectionModel().getSelectedItem();
                        indexCA.getItems().remove(selectedItem);
                        indexCA.refresh();
                        DialogBox.displaySuccess();
                        cuentaCorriente = null;
                        log.info("Item deleted.");
                    } catch (RuntimeException e) {
                        DialogBox.displayError();
                    }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        filteredData = new FilteredList<>(cuentasList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cuenta -> newValue == null || newValue.isEmpty()
                    || cuenta.getPropietarios().toString().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });

    }

    /**
     *
     * Class Methods
     *
     */

    public void setObject(Propietarios propietario) {
        this.propietario = propietario;
    } // Propietarios

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void refreshTable() {
        cuentasList.clear();
        loadDao();
    }

    private void displayModal(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        ModalDialogController mc = vs.loadModal(Route.CUENTACORRIENTE.modalView(), "Editar - Cuenta Corriente", event);
        mc.setObject(cuentaCorriente);
        vs.getStage().setOnHiding((stageEvent) -> {
            refreshTable();
        });
        mc.showModal(vs.getStage());

    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, cuentasList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<CuentasCorrientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCA.comparatorProperty());
        indexCA.setItems(sortedData);
    }

    void loadDao() {
        log.info("loading table items");
        Task<List<CuentasCorrientes>> task = dao.showByOwner(propietario);

        task.setOnSucceeded(event -> {
            cuentasList.setAll(task.getValue());
            indexCA.setItems(cuentasList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCA, cuentasList, tablePagination, index, 20));
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}
