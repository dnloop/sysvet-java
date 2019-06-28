package controller.currentAccount;

import java.io.IOException;
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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
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
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

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

    private Propietarios propietario;

    final ObservableList<CuentasCorrientes> cuentasList = FXCollections.observableArrayList();

    private FilteredList<CuentasCorrientes> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexCA != null : "fx:id=\"indexCA\" was not injected: check your FXML file 'show.fxml'.";
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

            cuentasList.setAll(dao.showByOwner(propietario));

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
                            log.info("Item deleted.");
                        } catch (RuntimeException e) {
                            DialogBox.displayError();
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

    public void setObject(Propietarios propietario) {
        this.propietario = propietario;
    } // Propietarios

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void refreshTable() {
        cuentasList.clear();
        cuentasList.setAll(dao.showByOwner(propietario));
        indexCA.setItems(cuentasList);
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.CUENTACORRIENTE.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(cuentaCorriente);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Editar - Cuenta Corriente");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHiding((stageEvent) -> {
                refreshTable();
            });
            sc.showModal(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
