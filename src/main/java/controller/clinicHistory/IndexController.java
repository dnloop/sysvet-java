package controller.clinicHistory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.HistoriaClinicaHome;
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
    private TableView<FichasClinicas> indexCH;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<FichasClinicas, Pacientes> tcPaciente;

    @FXML
    private TableColumn<FichasClinicas, Integer> tcFicha;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private HistoriaClinicaHome daoHC = new HistoriaClinicaHome();

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    final ObservableList<FichasClinicas> pacientesList = FXCollections.observableArrayList();

    private FichasClinicas fichaClinica;

    private FilteredList<FichasClinicas> filteredData;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'index.fxml'.";
        assert indexCH != null : "fx:id=\"indexCH\" was not injected: check your FXML file 'index.fxml'.";
        assert tcPaciente != null : "fx:id=\"tcPaciente\" was not injected: check your FXML file 'index.fxml'.";
        assert tcFicha != null : "fx:id=\"tcFicha\" was not injected: check your FXML file 'index.fxml'.";

        tcPaciente
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getPacientes()));

        tcFicha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Integer>(param.getValue().getId()));

        log.info("loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexCH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fichaClinica = newValue;
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnShow.setOnAction((event) -> {
            if (fichaClinica != null)
                displayShow(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (fichaClinica != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    daoHC.deleteAll(fichaClinica.getId());
                    FichasClinicas selectedItem = indexCH.getSelectionModel().getSelectedItem();
                    indexCH.getItems().remove(selectedItem);
                    refreshTable();
                    fichaClinica = null;
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
                    || ficha.getPacientes().getNombre().toLowerCase().contains(newValue.toLowerCase()));
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
        ShowController sc = vs.loadNode(Route.HISTORIACLINICA.showView());
        sc.setObject(fichaClinica);
        String path[] = { "Historia Clínica", "Índice", fichaClinica.getPacientes().toString() };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadNode(vs.getNode());
        ViewSwitcher.getLoadingDialog().startTask();
    }

    private void displayNew(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        NewController nc = vs.loadModal(Route.HISTORIACLINICA.newView());
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
        SortedList<FichasClinicas> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCH.comparatorProperty());
        indexCH.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<FichasClinicas>> task = daoHC.displayRecordsWithClinicHistory();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            indexCH.setItems(pacientesList);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexCH, pacientesList, tablePagination, index, 20));
            log.info("Table loaded.");
        });

        ViewSwitcher.getLoadingDialog().setProgress(task);
        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}
