package controller.currentAccount;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.CuentasCorrientesHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CuentasCorrientes;
import model.Propietarios;
import utils.DialogBox;
import utils.TableUtil;
import utils.ViewSwitcher;

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

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private CuentasCorrientesHome dao = new CuentasCorrientesHome();

    private CuentasCorrientes cuentaCorriente;

    final ObservableList<CuentasCorrientes> cuentasList = FXCollections.observableArrayList();

    private FilteredList<CuentasCorrientes> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexCA != null : "fx:id=\"indexCA\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        Platform.runLater(() -> {
            log.info("creating table");
            tcPropietario.setCellValueFactory((
                    TableColumn.CellDataFeatures<CuentasCorrientes, Propietarios> param) -> new ReadOnlyObjectWrapper<Propietarios>(
                            param.getValue().getPropietarios()));

            tcDescripcion.setCellValueFactory(
                    (TableColumn.CellDataFeatures<CuentasCorrientes, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getDescripcion()));

            tcMonto.setCellValueFactory((
                    TableColumn.CellDataFeatures<CuentasCorrientes, BigDecimal> param) -> new ReadOnlyObjectWrapper<BigDecimal>(
                            param.getValue().getMonto()));

            tcFecha.setCellValueFactory(
                    (TableColumn.CellDataFeatures<CuentasCorrientes, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getFecha()));
            log.info("loading table items");

            cuentasList.setAll(dao.displayDeletedRecords());

            indexCA.getColumns().setAll(tcFecha, tcPropietario, tcDescripcion, tcMonto);
            indexCA.setItems(cuentasList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCA, cuentasList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    cuentaCorriente = newValue;
                    log.info("Item selected.");
                }
            });

            btnRecover.setOnAction((event) -> {
                if (cuentaCorriente != null) {
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        try {
                            dao.recover(cuentaCorriente.getId());
                            CuentasCorrientes selectedItem = indexCA.getSelectionModel().getSelectedItem();
                            indexCA.getItems().remove(selectedItem);
                            indexCA.refresh();
                            cuentaCorriente = null;
                            DialogBox.displaySuccess();
                        } catch (RuntimeException e) {
                            DialogBox.setHeader("Fallo en la recuperación del registro.");
                            DialogBox.setContent("Motivo: " + e.getMessage());
                            DialogBox.displayError();
                        } // seems overkill but who knows =)

                        log.info("Item recovered.");
                    }
                } else
                    DialogBox.displayWarning();
            });
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
}
