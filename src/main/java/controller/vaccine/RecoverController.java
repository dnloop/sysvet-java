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
    private TableView<Vacunas> indexVC;

    @FXML
    private Pagination tablePagination;
    // table column
    @FXML
    TableColumn<Vacunas, Pacientes> pacientes;

    @FXML
    TableColumn<Vacunas, String> descripcion;

    @FXML
    TableColumn<Vacunas, Date> fecha;

    private static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private VacunasHome dao = new VacunasHome();

    private Vacunas vacuna;

    private final ObservableList<Vacunas> vaccineList = FXCollections.observableArrayList();

    private FilteredList<Vacunas> filteredData;

    @FXML
    void initialize() {

        pacientes.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getPacientes()));

        descripcion.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDescripcion())));

        fecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        log.info(marker, "loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexVC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                vacuna = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnRecover.setOnAction((event) -> {
            if (vacuna != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(vacuna.getId());
                    Vacunas selectedItem = indexVC.getSelectionModel().getSelectedItem();
                    vaccineList.remove(selectedItem);
                    indexVC.setItems(vaccineList);
                    indexVC.refresh();
                    vacuna = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item recovered.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(vaccineList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> newValue == null || newValue.isEmpty()
                    || ficha.toString().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getDescripcion().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });

    }

    /*
     * Class Methods
     */

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
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

    private void loadDao() {
        Task<List<Vacunas>> task = dao.displayDeletedRecords();

        task.setOnSucceeded(event -> {
            vaccineList.setAll(task.getValue());
            indexVC.setItems(vaccineList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexVC, vaccineList, tablePagination, index, 20));
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
    }
}
