package controller.treatment;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.FichasClinicas;
import model.Pacientes;
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

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private TratamientosHome dao = new TratamientosHome();

    private FichasClinicas ficha;

    final ObservableList<FichasClinicas> pacientesList = FXCollections.observableArrayList();

    private FilteredList<FichasClinicas> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexTR != null : "fx:id=\"indexTR\" was not injected: check your FXML file 'index.fxml'.";
        log.info("creating table");
        pacientes.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getPacientes()));

        fichaID.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getId().toString()));

        motivo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getMotivoConsulta()));

        log.info("loading table items");
        pacientesList.setAll(dao.displayRecordsWithFichas());
        indexTR.getColumns().setAll(pacientes, fichaID, motivo);
        indexTR.setItems(pacientesList);
        // setup pagination
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexTR, pacientesList, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexTR.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ficha = newValue;
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnShow.setOnAction((event) -> {
            if (ficha != null)
                displayShow(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (ficha != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.deleteAll(ficha.getId());
                    FichasClinicas selectedItem = indexTR.getSelectionModel().getSelectedItem();
                    indexTR.getItems().remove(selectedItem);
                    refreshTable();
                    ficha = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
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

    /**
     *
     * Class Methods
     *
     */

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayShow(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        ShowController sc = vs.loadNode(Route.TRATAMIENTO.showView());
        sc.setObject(ficha);
        ViewSwitcher.loadNode(vs.getNode());
    }

    private void displayNew(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        NewController nc = vs.loadModal(Route.TRATAMIENTO.newView(), "Nuevo elemento - Tratamiento", event);
        vs.getStage().setOnHiding((stageEvent) -> {
            indexTR.refresh();
        });
        nc.showModal(vs.getStage());
    }

    private void refreshTable() {
        pacientesList.clear();
        pacientesList.setAll(dao.displayRecordsWithFichas());
        indexTR.setItems(pacientesList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexTR, pacientesList, tablePagination, index, 20));
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
}
