package controller.clinicHistory;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.HistoriaClinicaHome;
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
import model.HistoriaClinica;
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
    private TableView<HistoriaClinica> indexCH;

    @FXML
    private Pagination tablePagination;

    // Table Columns
    @FXML
    private TableColumn<HistoriaClinica, Pacientes> tcPaciente;

    @FXML
    private TableColumn<HistoriaClinica, Integer> tcFicha;

    @FXML
    private TableColumn<HistoriaClinica, String> tcDescripcionEvento;

    @FXML
    private TableColumn<HistoriaClinica, Date> tcFechaResolucion;

    @FXML
    private TableColumn<HistoriaClinica, String> tcResultado;

    @FXML
    private TableColumn<HistoriaClinica, String> tcSecuelas;

    @FXML
    private TableColumn<HistoriaClinica, String> tcConsideraciones;

    @FXML
    private TableColumn<HistoriaClinica, String> tcComentarios;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private HistoriaClinicaHome dao = new HistoriaClinicaHome();

    private HistoriaClinica historiaClinica;

    final ObservableList<HistoriaClinica> historiaList = FXCollections.observableArrayList();

    private FilteredList<HistoriaClinica> filteredData;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexCH != null : "fx:id=\"indexCH\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcPaciente != null : "fx:id=\"tcPaciente\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcFicha != null : "fx:id=\"tcFicha\" was not injected: check your FXML file 'recover.fxml'.";

        tcPaciente.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getFichasClinicas().getPacientes()));

        tcFicha.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Integer>(param.getValue().getFichasClinicas().getId()));

        tcDescripcionEvento.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDescripcionEvento())));

        tcFechaResolucion
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaResolucion()));

        tcResultado.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getResultado())));

        tcSecuelas.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getSecuelas())));

        tcConsideraciones.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getConsideraciones())));

        tcComentarios.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getComentarios())));

        log.info("loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexCH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                historiaClinica = newValue;
                log.info("Item selected.");
            }
        });

        btnRecover.setOnAction((event) -> {
            if (historiaClinica != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(historiaClinica.getId());
                    HistoriaClinica selectedItem = indexCH.getSelectionModel().getSelectedItem();
                    historiaList.remove(selectedItem);
                    indexCH.setItems(historiaList);
                    indexCH.refresh();
                    historiaClinica = null;
                    DialogBox.displaySuccess();
                    log.info("Item recovered.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(historiaList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(historia -> newValue == null || newValue.isEmpty() || historia.getFichasClinicas()
                    .getPacientes().getNombre().toLowerCase().contains(newValue.toLowerCase()));
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
        int toIndex = Math.min(fromIndex + limit, historiaList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<HistoriaClinica> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCH.comparatorProperty());
        indexCH.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<HistoriaClinica>> task = dao.displayDeletedRecords();

        task.setOnSucceeded(event -> {
            historiaList.setAll(task.getValue());
            indexCH.setItems(historiaList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCH, historiaList, tablePagination, index, 20));
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}
