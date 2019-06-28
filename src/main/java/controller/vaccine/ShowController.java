package controller.vaccine;

import java.io.IOException;
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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Pacientes;
import model.Vacunas;
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
    private TableView<Vacunas> indexVC;

    @FXML
    private Pagination tablePagination;
    // table column
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
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexVC != null : "fx:id=\"indexVC\" was not injected: check your FXML file 'show.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'show.fxml'.";

        Platform.runLater(() -> {
            descripcion.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Vacunas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getDescripcion())));

            fecha.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Vacunas, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getFecha()));
            log.info("loading table items");

            vaccineList.setAll(dao.showByPatient(paciente));

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

            btnEdit.setOnAction((event) -> {
                if (vacuna != null)
                    displayModal(event);
                else
                    DialogBox.displayWarning();
            });

            btnDelete.setOnAction((event) -> {
                if (paciente != null) {
                    if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                        dao.deleteAll(paciente.getId());
                        Vacunas selectedItem = indexVC.getSelectionModel().getSelectedItem();
                        indexVC.getItems().remove(selectedItem);
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
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.VACUNA.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(vacuna);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Vacuna");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                indexVC.refresh();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        vaccineList.clear();
        vaccineList.setAll(dao.showByPatient(paciente));
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
