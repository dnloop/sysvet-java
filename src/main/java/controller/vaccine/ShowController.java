package controller.vaccine;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
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
import javafx.event.Event;
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

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private VacunasHome dao = new VacunasHome();

    private Vacunas vacuna;

    private Pacientes paciente;

    final ObservableList<Vacunas> vaccineList = FXCollections.observableArrayList();

    private FilteredList<Vacunas> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'show.fxml'.";
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'show.fxml'.";
        assert indexVC != null : "fx:id=\"indexVC\" was not injected: check your FXML file 'show.fxml'.";
        assert descripcion != null : "fx:id=\"descripcion\" was not injected: check your FXML file 'show.fxml'.";
        assert fecha != null : "fx:id=\"fecha\" was not injected: check your FXML file 'show.fxml'.";

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
                log.info("Item selected.");
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController ic = new IndexController();
            ic.setView(Route.VACUNA.indexView());
            String path[] = { "Vacuna", "Índice" };
            ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
            ViewSwitcher.getLoadingDialog().startTask();
        });

        btnEdit.setOnAction((event) -> {
            if (vacuna != null)
                displayModal(event);
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
                    log.info("Item deleted.");
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
        ModalDialogController mc = vs.loadModal(Route.VACUNA.modalView(), "Vacuna", event);
        vs.getStage().setOnHidden((stageEvent) -> {
            indexVC.refresh();
        });
        mc.setObject(vacuna);
        mc.showModal(vs.getStage());
    }

    private void refreshTable() {
        vaccineList.clear();
        loadDao();
        ViewSwitcher.getLoadingDialog().startTask();
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
        log.info("Loading table items.");
        Task<List<Vacunas>> task = dao.showByPatient(paciente);

        task.setOnSucceeded(event -> {
            vaccineList.setAll(task.getValue());
            indexVC.setItems(vaccineList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexVC, vaccineList, tablePagination, index, 20));
            log.info("Table loaded.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}
