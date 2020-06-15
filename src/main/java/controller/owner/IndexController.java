package controller.owner;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.PropietariosHome;
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
import model.Localidades;
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
    private JFXButton btnNew;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableView<Propietarios> indexPO;

    @FXML
    private Pagination tablePagination;

    @FXML
    TableColumn<Propietarios, String> tcNombre;

    @FXML
    TableColumn<Propietarios, String> tcApellido;

    @FXML
    TableColumn<Propietarios, String> tcDomicilio;

    @FXML
    TableColumn<Propietarios, String> tcTelCel;

    @FXML
    TableColumn<Propietarios, String> tcTelFijo;

    @FXML
    TableColumn<Propietarios, String> tcMail;

    @FXML
    TableColumn<Propietarios, Localidades> tcLocalidad;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private PropietariosHome dao = new PropietariosHome();

    private Propietarios propietario;

    final ObservableList<Propietarios> propList = FXCollections.observableArrayList();

    private FilteredList<Propietarios> filteredData;

    @FXML
    void initialize() {

        log.info(marker, "Creating table");

        tcNombre.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getNombre()));

        tcApellido.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getApellido()));

        tcDomicilio.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getDomicilio()));

        tcTelCel.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTelCel()));

        tcTelFijo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTelFijo()));

        tcMail.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getMail()));

        tcLocalidad.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Localidades>(param.getValue().getLocalidades()));

        log.info(marker, "loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexPO.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                propietario = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew());

        btnEdit.setOnAction((event) -> {
            if (propietario != null)
                displayEdit();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (propietario != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(propietario.getId());
                    Propietarios selectedItem = indexPO.getSelectionModel().getSelectedItem();
                    propList.remove(selectedItem);
                    indexPO.setItems(propList);
                    refreshTable();
                    propietario = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(propList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(owner -> newValue == null || newValue.isEmpty()
                    || owner.getNombre().toLowerCase().contains(newValue.toLowerCase())
                    || owner.getApellido().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /*
     * Class Methods
     */

    private void displayNew() {
        ViewSwitcher.loadModal(Route.PROPIETARIO.newView(), "Nuevo elemento - Propietario", true);
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshTable();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void displayEdit() {
        ViewSwitcher.loadModal(Route.PROPIETARIO.modalView(), "Propietario", true);
        ModalDialogController mc = ViewSwitcher.getController(Route.PROPIETARIO.modalView());
        ViewSwitcher.modalStage.setOnHidden((stageEvent) -> {
            refreshTable();
        });
        mc.setObject(propietario);
        mc.loadDao();
        ViewSwitcher.loadingDialog.startTask();

    }

    private void refreshTable() {
        propList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, propList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Propietarios> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexPO.comparatorProperty());
        indexPO.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Propietarios>> task = dao.displayRecords();

        task.setOnSucceeded(event -> {
            propList.setAll(task.getValue());
            indexPO.setItems(propList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexPO, propList, tablePagination, index, 20));
            ViewSwitcher.loadingDialog.getStage().close();
            log.info("Table loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
