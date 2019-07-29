package controller.vaccine;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.PacientesHome;
import dao.VacunasHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private TableView<Pacientes> indexVC;

    @FXML
    private Pagination tablePagination;

    @FXML
    TableColumn<Pacientes, Pacientes> pacientes;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static VacunasHome dao = new VacunasHome();

    private static PacientesHome daoPA = new PacientesHome();

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private FilteredList<Pacientes> filteredData;

    private Pacientes paciente;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexVC != null : "fx:id=\"indexVC\" was not injected: check your FXML file 'index.fxml'.";

        pacientes.setCellValueFactory(
                (TableColumn.CellDataFeatures<Pacientes, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue()));

        log.info("loading table items");

        pacientesList.setAll(dao.displayRecordsWithVaccines());

        indexVC.getColumns().setAll(pacientes);
        indexVC.setItems(pacientesList);
        // setup pagination
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexVC, pacientesList, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexVC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                paciente = newValue;
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnShow.setOnAction((event) -> {
            if (paciente != null)
                displayShow(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (paciente != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.deleteAll(paciente.getId());
                    Pacientes selectedItem = indexVC.getSelectionModel().getSelectedItem();
                    indexVC.getItems().remove(selectedItem);
                    refreshTable();
                    paciente = null;
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
                    || ficha.toString().toLowerCase().contains(newValue.toLowerCase()));
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
        ShowController sc = vs.loadModal(Route.VACUNA.showView());
        sc.setObject(paciente);
        ViewSwitcher.loadNode(vs.getNode());
    }

    private void displayNew(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        NewController nc = vs.loadModal(Route.VACUNA.newView(), "Nuevo elemento - Vacunación", event);
        vs.getStage().setOnHiding((stageEvent) -> {
            indexVC.refresh();
        });
        nc.showModal(vs.getStage());
    }

    private void refreshTable() {
        pacientesList.clear();
        pacientesList.setAll(dao.displayRecordsWithVaccines());
        indexVC.setItems(pacientesList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexVC, pacientesList, tablePagination, index, 20));
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, pacientesList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Pacientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexVC.comparatorProperty());
        indexVC.setItems(sortedData);
    }
}
