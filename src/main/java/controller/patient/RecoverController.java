package controller.patient;

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

import dao.PacientesHome;
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
    private TableView<Pacientes> indexPA;

    @FXML
    private Pagination tablePagination;

    @FXML
    TableColumn<Pacientes, String> nombre;

    @FXML
    TableColumn<Pacientes, String> especie;

    @FXML
    TableColumn<Pacientes, String> raza;

    @FXML
    TableColumn<Pacientes, String> sexo;

    @FXML
    TableColumn<Pacientes, String> temp;

    @FXML
    TableColumn<Pacientes, String> pelaje;

    @FXML
    TableColumn<Pacientes, Date> fecha;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private PacientesHome dao = new PacientesHome();

    private Pacientes paciente;

    private final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private FilteredList<Pacientes> filteredData;

    @FXML
    void initialize() {

        log.info(marker, "creating table");
        nombre.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getNombre()));

        especie.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getEspecie()));

        raza.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getRaza()));

        sexo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getSexo()));

        temp.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTemperamento()));

        pelaje.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPelaje()));

        fecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaNacimiento()));
        log.info(marker, "loading table items");

        loadDao();
        // Handle ListView selection changes.
        indexPA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                paciente = newValue;
                log.info(marker, "Item selected.");
            }
        });

        btnRecover.setOnAction((event) -> {
            if (paciente != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(paciente.getId());
                    Pacientes selectedItem = indexPA.getSelectionModel().getSelectedItem();
                    pacientesList.remove(selectedItem);
                    indexPA.setItems(pacientesList);
                    indexPA.refresh();
                    paciente = null;
                    DialogBox.displaySuccess();
                    log.info(marker, "Item recovered.");
                }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        filteredData = new FilteredList<>(pacientesList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(localidad -> newValue == null || newValue.isEmpty()
                    || localidad.getNombre().toLowerCase().contains(newValue.toLowerCase())
                    || localidad.getPropietarios().toString().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /*
     * Class methods
     */

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, pacientesList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Pacientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexPA.comparatorProperty());
        indexPA.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Pacientes>> task = dao.displayDeletedRecords();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            indexPA.setItems(pacientesList);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexPA, pacientesList, tablePagination, index, 20));
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
    }
}
