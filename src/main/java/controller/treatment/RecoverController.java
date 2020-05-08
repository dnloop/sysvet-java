package controller.treatment;

import java.net.URL;
import java.util.Date;
import java.util.List;
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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Pacientes;
import model.Tratamientos;
import utils.DialogBox;
import utils.TableUtil;
import utils.viewswitcher.ViewSwitcher;

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
    private TableView<Tratamientos> indexTR;

    @FXML
    private Pagination tablePagination;
    // Table columns
    @FXML
    private TableColumn<Tratamientos, Pacientes> pacientes;

    @FXML
    private TableColumn<Tratamientos, Date> fecha;

    @FXML
    private TableColumn<Tratamientos, Date> hora;

    @FXML
    private TableColumn<Tratamientos, String> tcTratamiento;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private static TratamientosHome dao = new TratamientosHome();

    private Tratamientos tratamiento;

    final ObservableList<Tratamientos> tratamientosList = FXCollections.observableArrayList();

    private FilteredList<Tratamientos> filteredData;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexTR != null : "fx:id=\"indexTR\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

        pacientes.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getFichasClinicas().getPacientes()));

        fecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        hora.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getHora()));

        tcTratamiento.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTratamiento()));

        log.info("loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexTR.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tratamiento = newValue;
                log.info("Item selected." + tratamiento.getId());
            }
        });

        btnRecover.setOnAction((event) -> {
            if (tratamiento != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(tratamiento.getId());
                    Tratamientos selectedItem = indexTR.getSelectionModel().getSelectedItem();
                    tratamientosList.remove(selectedItem);
                    indexTR.setItems(tratamientosList);
                    indexTR.refresh();
                    tratamiento = null;
                    DialogBox.displaySuccess();
                    log.info("Item recovered.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(tratamientosList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> newValue == null || newValue.isEmpty()
                    || ficha.toString().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getTratamiento().toLowerCase().contains(newValue.toLowerCase()));
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
        int toIndex = Math.min(fromIndex + limit, tratamientosList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Tratamientos> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexTR.comparatorProperty());
        indexTR.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Tratamientos>> task = dao.displayDeletedRecords();

        task.setOnSucceeded(event -> {
            tratamientosList.setAll(task.getValue());
            indexTR.setItems(tratamientosList);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexTR, tratamientosList, tablePagination, index, 20));
            log.info("Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
    }
}
