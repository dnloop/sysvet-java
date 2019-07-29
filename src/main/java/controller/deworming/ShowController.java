package controller.deworming;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.DesparasitacionesHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Desparasitaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.TableUtil;
import utils.ViewSwitcher;
import utils.routes.Route;

public class ShowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

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

    private final ObservableList<Desparasitaciones> despList = FXCollections.observableArrayList();

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private DesparasitacionesHome dao = new DesparasitacionesHome();

    private Desparasitaciones desparasitacion;

    private Pacientes paciente;

    private FilteredList<Desparasitaciones> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexD != null : "fx:id=\"indexD\" was not injected: check your FXML file 'show.fxml'.";
        Platform.runLater(() -> {
            log.info("creating table");

            tcTratamiento.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Desparasitaciones, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getTratamiento()));

            tcTipo.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Desparasitaciones, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getTipo()));

            tcFecha.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Desparasitaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getFecha()));

            tcFechaProxima.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Desparasitaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getFechaProxima()));

            log.info("loading table items");

            despList.setAll(dao.showByPatient(paciente));

            indexD.getColumns().setAll(tcFecha, tcTratamiento, tcTipo, tcFechaProxima);
            indexD.setItems(despList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexD, despList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexD.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    desparasitacion = newValue;
                    log.info("Item selected.");
                }
            });

            btnEdit.setOnAction((event) -> {
                if (paciente != null)
                    displayModal(event);
                else
                    DialogBox.displayWarning();
            });

            btnDelete.setOnAction((event) -> {
                if (paciente != null) {
                    if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                        dao.delete(paciente.getId());
                        Desparasitaciones selectedItem = indexD.getSelectionModel().getSelectedItem();
                        indexD.getItems().remove(selectedItem);
                        refreshTable();
                        paciente = null;
                        DialogBox.displayWarning();
                        log.info("Item deleted.");
                    }
                } else
                    DialogBox.displayWarning();
            });
        });
        // search filter
        filteredData = new FilteredList<>(despList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(desp -> newValue == null || newValue.isEmpty()
                    || desp.getPacientes().toString().toLowerCase().contains(newValue.toLowerCase()));
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
        ModalDialogController mc = vs.loadModal(Route.DESPARASITACION.modalView(), "Desparasitación", event);
        mc.setObject(desparasitacion);
        vs.getStage().setOnHiding((stageEvent) -> {
            refreshTable();
        });
        mc.showModal(vs.getStage());
    }

    private void refreshTable() {
        despList.clear();
        despList.setAll(dao.showByPatient(paciente));
        indexD.setItems(despList);
        tablePagination.setPageFactory((index) -> TableUtil.createPage(indexD, despList, tablePagination, index, 20));
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
}
