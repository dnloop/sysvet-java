package controller.internation;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.InternacionesHome;
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
import model.Internaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.TableUtil;
import utils.routes.Route;
import utils.viewswitcher.ViewSwitcher;

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
    private TableView<Internaciones> indexI;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<Internaciones, Date> tcFechaIngreso;

    @FXML
    private TableColumn<Internaciones, Date> tcFechaAlta;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private InternacionesHome dao = new InternacionesHome();

    private Internaciones internacion;

    private Pacientes paciente;

    final ObservableList<Internaciones> fichasList = FXCollections.observableArrayList();

    private FilteredList<Internaciones> filteredData;

    @FXML
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'show.fxml'.";
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'show.fxml'.";
        assert indexI != null : "fx:id=\"indexI\" was not injected: check your FXML file 'show.fxml'.";
        assert tcFechaIngreso != null : "fx:id=\"tcFechaIngreso\" was not injected: check your FXML file 'show.fxml'.";
        assert tcFechaAlta != null : "fx:id=\"tcFechaAlta\" was not injected: check your FXML file 'show.fxml'.";

        tcFechaIngreso
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaIngreso()));

        tcFechaAlta.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaAlta()));

        // Handle ListView selection changes.
        indexI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                internacion = newValue;
                log.info("Item selected." + internacion.getId());
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController ic = new IndexController();
            ic.setView(Route.INTERNACION.indexView());
            String path[] = { "Internación", "Índice" };
            ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
            ViewSwitcher.getLoadingDialog().startTask();
        });

        btnEdit.setOnAction((event) -> {
            if (internacion != null)
                displayModal(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (internacion != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(internacion.getId());
                    Internaciones selectedItem = indexI.getSelectionModel().getSelectedItem();
                    fichasList.remove(selectedItem);
                    indexI.setItems(fichasList);
                    indexI.refresh();
                    internacion = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(fichasList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(paciente -> newValue == null || newValue.isEmpty()
                    || paciente.getFechaIngreso().toString().toLowerCase().contains(newValue.toLowerCase())
                    || paciente.getFechaIngreso().toString().toLowerCase().contains(newValue.toLowerCase()));
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
        ModalDialogController mc = vs.loadModal(Route.INTERNACION.modalView(), "Editar - Internación", event);
        mc.setObject(internacion);
        vs.getStage().setOnHidden((stageEvent) -> {
            refreshTable();
        });
        mc.showModal(vs.getStage());
    }

    private void refreshTable() {
        fichasList.clear();
        loadDao();
        ViewSwitcher.getLoadingDialog().startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, fichasList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Internaciones> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexI.comparatorProperty());
        indexI.setItems(sortedData);
    }

    public void loadDao() {
        log.info("Loading table items");
        Task<List<Internaciones>> task = dao.showByPatient(paciente);

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            indexI.setItems(fichasList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexI, fichasList, tablePagination, index, 20));
            ViewSwitcher.getLoadingDialog().getStage().close();
            log.info("Table loaded.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }

    public void loadFromPatient() {
        log.info("Loading table items from patient");
        Task<List<Internaciones>> task = dao.showByPatient(paciente);

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            indexI.setItems(fichasList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexI, fichasList, tablePagination, index, 20));
            log.info("Table loaded.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}
