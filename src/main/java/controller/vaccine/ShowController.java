package controller.vaccine;

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

import dao.VacunasHome;
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
import model.Vacunas;
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
    private TableView<Vacunas> indexVC;

    @FXML
    private Pagination tablePagination;

    @FXML
    TableColumn<Vacunas, String> descripcion;

    @FXML
    TableColumn<Vacunas, Date> fecha;

    private static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private VacunasHome dao = new VacunasHome();

    private Vacunas vacuna;

    private Pacientes paciente;

    private final ObservableList<Vacunas> vaccineList = FXCollections.observableArrayList();

    private FilteredList<Vacunas> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {

        descripcion.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDescripcion())));

        fecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        indexVC.getColumns().setAll(fecha, descripcion);
        indexVC.setItems(vaccineList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexVC, vaccineList, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexVC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                vacuna = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController.setView(Route.VACUNA.indexView());
            String path[] = { "Vacuna", "Índice" };
            ViewSwitcher.setPath(path);
            ViewSwitcher.loadingDialog.startTask();
        });

        btnEdit.setOnAction((event) -> {
            if (vacuna != null)
                displayModal();
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (paciente != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(paciente.getId());
                    Vacunas selectedItem = indexVC.getSelectionModel().getSelectedItem();
                    vaccineList.remove(selectedItem);
                    indexVC.setItems(vaccineList);
                    refreshTable();
                    paciente = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(vaccineList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> newValue == null || newValue.isEmpty()
                    || ficha.getDescripcion().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /*
     * Class Methods
     */

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    } // Pacientes

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal() {
        ViewSwitcher.loadModal(Route.VACUNA.modalView(), "Vacuna", true);
        ModalDialogController mc = ViewSwitcher.getController(Route.VACUNA.modalView());
        ViewSwitcher.modalStage.setOnHidden((stageEvent) -> {
            indexVC.refresh();
        });
        mc.setObject(vacuna);
        ViewSwitcher.modalStage.showAndWait();
    }

    private void refreshTable() {
        vaccineList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, vaccineList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Vacunas> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexVC.comparatorProperty());
        indexVC.setItems(sortedData);
    }

    public void loadDao() {
        log.info(marker, "Loading table items.");
        Task<List<Vacunas>> task = dao.showByPatient(paciente);

        task.setOnSucceeded(event -> {
            vaccineList.setAll(task.getValue());
            indexVC.setItems(vaccineList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexVC, vaccineList, tablePagination, index, 20));
            log.info(marker, "Table loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
