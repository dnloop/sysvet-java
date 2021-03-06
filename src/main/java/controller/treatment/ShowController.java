package controller.treatment;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
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
import model.FichasClinicas;
import model.Pacientes;
import model.Tratamientos;
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

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private static TratamientosHome dao = new TratamientosHome();

    private FichasClinicas ficha;

    private Tratamientos tratamiento;

    private final ObservableList<Tratamientos> pacientesList = FXCollections.observableArrayList();

    private FilteredList<Tratamientos> filteredData;

    @FXML
    void initialize() {

        pacientes.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getFichasClinicas().getPacientes()));

        fecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        hora.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getHora()));

        tcTratamiento.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTratamiento()));

        // Handle ListView selection changes.
        indexTR.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tratamiento = newValue;
                log.info(marker, "Item selected." + ficha.getId());
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController.setView(Route.TRATAMIENTO.indexView());
            String path[] = { "Tratamiento", "Índice" };
            ViewSwitcher.setPath(path);
        });

        btnEdit.setOnAction((event) -> {
            if (tratamiento != null)
                displayModal();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (tratamiento != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(tratamiento.getId());
                    Tratamientos selectedItem = indexTR.getSelectionModel().getSelectedItem();
                    indexTR.getItems().remove(selectedItem);
                    refreshTable();
                    tratamiento = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        filteredData = new FilteredList<>(pacientesList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> newValue == null || newValue.isEmpty()
                    || ficha.toString().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getTratamiento().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /*
     * Class Methods
     */

    public void setObject(FichasClinicas ficha) {
        this.ficha = ficha;
    }

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal() {
        ViewSwitcher.loadModal(Route.TRATAMIENTO.modalView(), "Editar - Tratamiento", true);
        ModalDialogController mc = ViewSwitcher.getController(Route.TRATAMIENTO.modalView());
        ViewSwitcher.modalStage.setOnHidden((stageEvent) -> {
            refreshTable();
        });
        mc.setObject(tratamiento);
        ViewSwitcher.modalStage.showAndWait();
    }

    private void refreshTable() {
        pacientesList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, pacientesList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Tratamientos> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexTR.comparatorProperty());
        indexTR.setItems(sortedData);
    }

    void loadDao() {
        log.info("Loading table items");
        Task<List<Tratamientos>> task = dao.showByFicha(ficha);

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            indexTR.setItems(pacientesList);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexTR, pacientesList, tablePagination, index, 20));
            ViewSwitcher.loadingDialog.getStage().close();
            log.info(marker, "Table loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
