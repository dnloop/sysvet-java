package controller.deworming;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.DesparasitacionesHome;
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
import model.Desparasitaciones;
import model.Pacientes;
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
    private TableView<Desparasitaciones> indexD;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<Desparasitaciones, String> tcTratamiento;

    @FXML
    private TableColumn<Desparasitaciones, String> tcTipo;

    @FXML
    private TableColumn<Desparasitaciones, Date> tcFecha;

    @FXML
    private TableColumn<Desparasitaciones, Date> tcFechaProxima;

    private final ObservableList<Desparasitaciones> despList = FXCollections.observableArrayList();

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private DesparasitacionesHome dao = new DesparasitacionesHome();

    private Desparasitaciones desparasitacion;

    private Pacientes paciente;

    private FilteredList<Desparasitaciones> filteredData;

    @FXML
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'show.fxml'.";
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'show.fxml'.";
        assert indexD != null : "fx:id=\"indexD\" was not injected: check your FXML file 'show.fxml'.";
        assert tcTratamiento != null : "fx:id=\"tcTratamiento\" was not injected: check your FXML file 'show.fxml'.";
        assert tcTipo != null : "fx:id=\"tcTipo\" was not injected: check your FXML file 'show.fxml'.";
        assert tcFecha != null : "fx:id=\"tcFecha\" was not injected: check your FXML file 'show.fxml'.";
        assert tcFechaProxima != null : "fx:id=\"tcFechaProxima\" was not injected: check your FXML file 'show.fxml'.";

        log.info("creating table");
        tcTratamiento.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTratamiento()));

        tcTipo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTipo()));

        tcFecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        tcFechaProxima
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaProxima()));

        // Handle ListView selection changes.
        indexD.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                desparasitacion = newValue;
                log.info("Item selected.");
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController ic = new IndexController();
            ic.setView(Route.DESPARASITACION.indexView());
            String path[] = { "Desparasitación", "Índice" };
            ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
            ViewSwitcher.getLoadingDialog().startTask();
        });

        btnEdit.setOnAction((event) -> {
            if (paciente != null)
                displayModal(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (paciente != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(paciente.getId());
                    Desparasitaciones selectedItem = indexD.getSelectionModel().getSelectedItem();
                    indexD.getItems().remove(selectedItem);
                    refreshTable();
                    paciente = null;
                    DialogBox.displayWarning();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        filteredData = new FilteredList<>(despList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(desp -> newValue == null || newValue.isEmpty()
                    || desp.getPacientes().toString().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    } // Pacientes

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        ModalDialogController mc = vs.loadModal(Route.DESPARASITACION.modalView(), "Desparasitación", event);
        mc.setObject(desparasitacion);
        vs.getStage().setOnHiding((stageEvent) -> {
            refreshTable();
        });
        mc.showModal(vs.getStage());
    }

    private void refreshTable() {
        despList.clear();
        loadDao();
        ViewSwitcher.getLoadingDialog().startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, despList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Desparasitaciones> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexD.comparatorProperty());
        indexD.setItems(sortedData);
    }

    public void loadDao() {
        log.info("Loading table items");
        Task<List<Desparasitaciones>> task = dao.showByPatient(paciente);

        task.setOnSucceeded(event -> {
            despList.setAll(task.getValue());
            indexD.setItems(despList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexD, despList, tablePagination, index, 20));
            ViewSwitcher.getLoadingDialog().getStage().close();
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }

    public void loadFromPatient() {
        log.info("Loading table items from patient");
        Task<List<Desparasitaciones>> task = dao.showByPatient(paciente);

        task.setOnSucceeded(event -> {
            despList.setAll(task.getValue());
            indexD.setItems(despList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexD, despList, tablePagination, index, 20));
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}
