package controller.clinicalFile;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.FichasClinicasHome;
import dao.PacientesHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
    private TableView<Pacientes> indexCF;

    @FXML
    private TableColumn<Pacientes, Pacientes> tcPaciente;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private PacientesHome daoPA = new PacientesHome();

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private Pacientes paciente;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private FilteredList<Pacientes> filteredData;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'index.fxml'.";
        assert indexCF != null : "fx:id=\"indexCF\" was not injected: check your FXML file 'index.fxml'.";
        assert tcPaciente != null : "fx:id=\"tcPaciente\" was not injected: check your FXML file 'index.fxml'.";

        tcPaciente.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue()));

        log.info("loading table items");
        loadDao();
        // Handle ListView selection changes.
        indexCF.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                paciente = newValue;
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnShow.setOnAction((event) -> {
            if (paciente != null)
                displayShow();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (paciente != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    daoFC.deleteAll(paciente.getId());
                    Pacientes selectedItem = indexCF.getSelectionModel().getSelectedItem();
                    indexCF.getItems().remove(selectedItem);
                    paciente = null;
                    refreshTable();
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(pacientesList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(paciente -> newValue == null || newValue.isEmpty()
                    || paciente.getNombre().toLowerCase().contains(newValue.toLowerCase()));
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

    private void displayShow() {
        ViewSwitcher vs = new ViewSwitcher();
        ShowController sc = vs.loadNode(Route.FICHACLINICA.showView());
        sc.setObject(paciente);
        String path[] = { "Ficha Clínica", paciente.getNombre(), "Fichas" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadNode(vs.getNode());
        ViewSwitcher.getLoadingDialog().startTask();
    }

    private void displayNew(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        NewController nc = vs.loadModal(Route.FICHACLINICA.newView(), "Nuevo elemento - Ficha Clínica", event);
        vs.getStage().setOnHiding((stageEvent) -> {
            refreshTable();
        });
        nc.showModal(vs.getStage());
    }

    private void refreshTable() {
        pacientesList.clear();
        loadDao();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, pacientesList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Pacientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCF.comparatorProperty());
        indexCF.setItems(sortedData);
    }

    private void loadDao() {
        log.info("Loading table items.");
        Task<List<Pacientes>> task = daoPA.displayRecordsWithClinicalFiles();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            indexCF.setItems(pacientesList);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexCF, pacientesList, tablePagination, index, 20));
            log.info("Table Loaded.");
        });

        ViewSwitcher.getLoadingDialog().setProgress(task);
        ViewSwitcher.getLoadingDialog().setTask(task);
    }

}
