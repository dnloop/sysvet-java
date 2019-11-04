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
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Desparasitaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.TableUtil;
import utils.ViewSwitcher;

public class RecoverController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnRecover;

    @FXML
    private TableView<Desparasitaciones> indexD;

    @FXML
    private Pagination tablePagination;

    // Table columns
    @FXML
    private TableColumn<Desparasitaciones, String> tcTratamiento;

    @FXML
    private TableColumn<Desparasitaciones, String> tcTipo;

    @FXML
    private TableColumn<Desparasitaciones, Date> tcFecha;

    @FXML
    private TableColumn<Desparasitaciones, Date> tcFechaProxima;

    @FXML
    private TableColumn<Desparasitaciones, Pacientes> tcPaciente;

    private FilteredList<Desparasitaciones> filteredData;

    private final ObservableList<Desparasitaciones> despList = FXCollections.observableArrayList();

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private DesparasitacionesHome dao = new DesparasitacionesHome();

    private Desparasitaciones desparasitacion;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexD != null : "fx:id=\"indexD\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

        log.info("creating table");
        tcPaciente
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getPacientes()));

        tcTratamiento.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTratamiento()));

        tcTipo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTipo()));

        tcFecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        tcFechaProxima
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaProxima()));

        log.info("loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexD.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                desparasitacion = newValue;
                log.info("Item selected.");
            }
        });

        btnRecover.setOnAction((event) -> {
            if (desparasitacion != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(desparasitacion.getId());
                    Desparasitaciones selectedItem = indexD.getSelectionModel().getSelectedItem();
                    indexD.getItems().remove(selectedItem);
                    indexD.refresh();
                    desparasitacion = null;
                    DialogBox.displaySuccess();
                    log.info("Item recovered.");
                }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        filteredData = new FilteredList<>(despList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(paciente -> newValue == null || newValue.isEmpty()
                    || paciente.getPacientes().toString().toLowerCase().contains(newValue.toLowerCase()));
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

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, despList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Desparasitaciones> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexD.comparatorProperty());
        indexD.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Desparasitaciones>> task = dao.displayDeletedRecords();

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
