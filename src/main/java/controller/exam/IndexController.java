package controller.exam;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.ExamenGeneralHome;
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
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableView<Pacientes> indexE;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<Pacientes, Pacientes> tcPaciente;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private ExamenGeneralHome daoEG = new ExamenGeneralHome();

    private PacientesHome daoPA = new PacientesHome();

    final ObservableList<Pacientes> filesList = FXCollections.observableArrayList();

    private Pacientes paciente;

    private FilteredList<Pacientes> filteredData;

    @FXML
    void initialize() {

        tcPaciente.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue()));

        log.info("loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexE.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
                    daoEG.deleteAll(paciente.getId());
                    Pacientes selectedItem = indexE.getSelectionModel().getSelectedItem();
                    filesList.remove(selectedItem);
                    indexE.setItems(filesList);
                    refreshTable();
                    paciente = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(filesList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(paciente -> newValue == null || newValue.isEmpty()
                    || paciente.getNombre().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /*
     * Class Methods
     */

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    /**
     * Displays a list of physical exams by patient.
     */
    private void displayShow() {
        ViewSwitcher vs = new ViewSwitcher();
        ShowController sc = vs.loadNode(Route.EXAMEN.showView());
        sc.setObject(paciente);
        sc.loadDao();
        String path[] = { "Exámen", "Índice", paciente.getNombre() };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
        ViewSwitcher.loadView(vs.getNode());
    }

    private void displayNew(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        NewController nc = vs.loadModal(Route.EXAMEN.newView(), "Nuevo elemento - Exámen General", event);
        vs.getStage().setOnHiding((stageEvent) -> {
            refreshTable();
        });
        nc.showModal(vs.getStage());
    }

    private void refreshTable() {
        filesList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, filesList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Pacientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexE.comparatorProperty());
        indexE.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Pacientes>> task = daoPA.displayRecordsWithExams();

        task.setOnSucceeded(event -> {
            filesList.setAll(task.getValue());
            indexE.setItems(filesList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexE, filesList, tablePagination, index, 20));
            ViewSwitcher.loadingDialog.closeStage();
            log.info("Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
    }
}
