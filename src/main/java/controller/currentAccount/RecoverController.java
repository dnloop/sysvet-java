package controller.currentAccount;

import java.math.BigDecimal;
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

import dao.CuentasCorrientesHome;
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
import model.CuentasCorrientes;
import model.Propietarios;
import utils.DialogBox;
import utils.TableUtil;
import utils.viewswitcher.ViewSwitcher;

public class RecoverController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnRecover;

    @FXML
    private TableView<CuentasCorrientes> indexCA;

    @FXML
    private Pagination tablePagination;

    // Table columns
    @FXML
    private TableColumn<CuentasCorrientes, Propietarios> tcPropietario;

    @FXML
    private TableColumn<CuentasCorrientes, String> tcDescripcion;

    @FXML
    private TableColumn<CuentasCorrientes, BigDecimal> tcMonto;

    @FXML
    private TableColumn<CuentasCorrientes, Date> tcFecha;

    private static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private CuentasCorrientesHome dao = new CuentasCorrientesHome();

    private CuentasCorrientes cuentaCorriente;

    final ObservableList<CuentasCorrientes> cuentasList = FXCollections.observableArrayList();

    private FilteredList<CuentasCorrientes> filteredData;

    @FXML
    void initialize() {
        log.info(marker, "creating table");
        tcPropietario.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Propietarios>(param.getValue().getPropietarios()));

        tcDescripcion.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getDescripcion()));

        tcMonto.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<BigDecimal>(param.getValue().getMonto()));

        tcFecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        log.info(marker, "loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cuentaCorriente = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnRecover.setOnAction((event) -> {
            if (cuentaCorriente != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    try {
                        dao.recover(cuentaCorriente.getId());
                        CuentasCorrientes selectedItem = indexCA.getSelectionModel().getSelectedItem();
                        cuentasList.remove(selectedItem);
                        indexCA.setItems(cuentasList);
                        indexCA.refresh();
                        cuentaCorriente = null;
                        DialogBox.displaySuccess();
                    } catch (RuntimeException e) {
                        DialogBox.setHeader("Fallo en la recuperación del registro.");
                        DialogBox.setContent("Motivo: " + e.getMessage());
                        DialogBox.displayError();
                    } // seems overkill but who knows =)

                    log.info(marker, "Item recovered.");
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

    /*
     * Class Methods
     */

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
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

    private void loadDao() {
        Task<List<CuentasCorrientes>> task = dao.displayDeletedRecords();

        task.setOnSucceeded(event -> {
            cuentasList.setAll(task.getValue());
            indexCA.setItems(cuentasList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCA, cuentasList, tablePagination, index, 20));
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
