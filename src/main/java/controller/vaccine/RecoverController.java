package controller.vaccine;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.VacunasHome;
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
import model.Vacunas;
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

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private VacunasHome dao = new VacunasHome();

    private Vacunas vacuna;

    final ObservableList<Vacunas> vaccineList = FXCollections.observableArrayList();

    private FilteredList<Vacunas> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexVC != null : "fx:id=\"indexVC\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

        Platform.runLater(() -> {
            pacientes.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Vacunas, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getPacientes()));

            descripcion.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Vacunas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getDescripcion())));

            fecha.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Vacunas, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getFecha()));

            log.info("loading table items");

            vaccineList.setAll(dao.displayDeletedRecords());

            indexVC.getColumns().setAll(pacientes, fecha, descripcion);
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

            btnRecover.setOnAction((event) -> {
                if (vacuna != null) {
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        dao.recover(vacuna.getId());
                        Vacunas selectedItem = indexVC.getSelectionModel().getSelectedItem();
                        indexVC.getItems().remove(selectedItem);
                        refreshTable();
                        vacuna = null;
                        DialogBox.displaySuccess();
                        log.info("Item recovered.");
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
        vaccineList.clear();
        vaccineList.setAll(dao.displayDeletedRecords());
        indexVC.setItems(vaccineList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexVC, vaccineList, tablePagination, index, 20));
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
}
