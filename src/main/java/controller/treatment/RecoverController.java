package controller.treatment;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.TratamientosHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Pacientes;
import model.Tratamientos;
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

    final ObservableList<Tratamientos> pacientesList = FXCollections.observableArrayList();

    private FilteredList<Tratamientos> filteredData;

    @SuppressWarnings("unchecked")

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexTR != null : "fx:id=\"indexTR\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        Platform.runLater(() -> {
            pacientes.setCellValueFactory((
                    TableColumn.CellDataFeatures<Tratamientos, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getFichasClinicas().getPacientes()));

            fecha.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Tratamientos, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getFecha()));

            hora.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Tratamientos, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getHora()));

            tcTratamiento.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Tratamientos, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getTratamiento()));

            log.info("loading table items");
            pacientesList.setAll(dao.displayDeletedRecords());

            indexTR.getColumns().setAll(pacientes, fecha, hora, tcTratamiento);
            indexTR.setItems(pacientesList);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexTR, pacientesList, tablePagination, index, 20));

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
                        indexTR.getItems().remove(selectedItem);
                        refreshTable();
                        tratamiento = null;
                        DialogBox.displaySuccess();
                        log.info("Item recovered.");
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

    private void refreshTable() {
        pacientesList.clear();
        pacientesList.setAll(dao.displayDeletedRecords());
        indexTR.setItems(pacientesList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexTR, pacientesList, tablePagination, index, 20));
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
}
