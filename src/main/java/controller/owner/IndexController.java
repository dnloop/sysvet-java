package controller.owner;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Localidades;
import model.Propietarios;
import utils.DialogBox;
import utils.TableUtil;
import utils.ViewSwitcher;
import utils.routes.Route;

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

    // table columns
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

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private PropietariosHome dao = new PropietariosHome();

    private Propietarios propietario;

    final ObservableList<Propietarios> propList = FXCollections.observableArrayList();

    private FilteredList<Propietarios> filteredData;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexPO != null : "fx:id=\"indexPO\" was not injected: check your FXML file 'index.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'index.fxml'.";

        log.info("creating table");

        tcNombre.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getNombre()));

        tcApellido.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getApellido()));

        tcDomicilio.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getDomicilio()));

        tcTelCel.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTelCel()));

        tcTelFijo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTelFijo()));

        tcMail.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getMail()));

        tcLocalidad.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Localidades>(param.getValue().getLocalidades()));

        log.info("loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexPO.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                propietario = newValue;
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnEdit.setOnAction((event) -> {
            if (propietario != null)
                displayEdit(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (propietario != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(propietario.getId());
                    Propietarios selectedItem = indexPO.getSelectionModel().getSelectedItem();
                    indexPO.getItems().remove(selectedItem);
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
            filteredData.setPredicate(localidad -> newValue == null || newValue.isEmpty()
                    || localidad.getNombre().toLowerCase().contains(newValue.toLowerCase())
                    || localidad.getApellido().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /**
     *
     * Class Methods
     *
     */

    private void displayNew(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        NewController nc = vs.loadModal(Route.PROPIETARIO.newView(), "Nuevo elemento - Propietario", event);
        vs.getStage().setOnHiding((stageEvent) -> {
            refreshTable();
        });
        nc.showModal(vs.getStage());
    }

    private void displayEdit(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        ModalDialogController mc = vs.loadModal(Route.PROPIETARIO.modalView(), "Propietario", event);
        vs.getStage().setOnHidden((stageEvent) -> {
            refreshTable();
        });
        mc.setObject(propietario);
        mc.showModal(vs.getStage());
    }

    private void refreshTable() {
        propList.clear();
        loadDao();
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
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setProgress(task);
        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}
