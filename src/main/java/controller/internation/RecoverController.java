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
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Internaciones;
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
    private TableView<Internaciones> indexI;

    @FXML
    private Pagination tablePagination;

    // // Table column
    @FXML
    private TableColumn<Internaciones, Pacientes> tcPaciente;

    @FXML
    private TableColumn<Internaciones, Date> tcFechaIngreso;

    @FXML
    private TableColumn<Internaciones, Date> tcFechaAlta;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private InternacionesHome dao = new InternacionesHome();

    private Internaciones internacion;

    final ObservableList<Internaciones> fichasList = FXCollections.observableArrayList();

    private FilteredList<Internaciones> filteredData;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexI != null : "fx:id=\"indexI\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

        tcPaciente
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getPacientes()));

        tcFechaIngreso
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaIngreso()));

        tcFechaAlta.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaAlta()));

        log.info("loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                internacion = newValue;
                log.info("Item selected." + internacion.getId());
            }
        });

        btnRecover.setOnAction((event) -> {
            if (internacion != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(internacion.getId());
                    Internaciones selectedItem = indexI.getSelectionModel().getSelectedItem();
                    fichasList.remove(selectedItem);
                    indexI.setItems(fichasList);
                    indexI.refresh();
                    internacion = null;
                    DialogBox.displaySuccess();
                    log.info("Item recovered.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(fichasList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> newValue == null || newValue.isEmpty()
                    || ficha.getPacientes().toString().toLowerCase().contains(newValue.toLowerCase()));
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
        int toIndex = Math.min(fromIndex + limit, fichasList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Internaciones> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexI.comparatorProperty());
        indexI.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<Internaciones>> task = dao.displayDeletedRecords();

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            indexI.setItems(fichasList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexI, fichasList, tablePagination, index, 20));
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}
