package controller.internation;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.InternacionesHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import model.Internaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.Route;
import utils.TableUtil;
import utils.ViewSwitcher;

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
    private TableView<Internaciones> indexI;

    @FXML
    private Pagination tablePagination;

    // Table columns
    @FXML
    private TableColumn<Internaciones, Date> tcFechaIngreso;

    @FXML
    private TableColumn<Internaciones, Date> tcFechaAlta;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private InternacionesHome dao = new InternacionesHome();

    private Internaciones internacion;

    private Pacientes paciente;

    final ObservableList<Internaciones> fichasList = FXCollections.observableArrayList();

    private FilteredList<Internaciones> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexI != null : "fx:id=\"indexI\" was not injected: check your FXML file 'show.fxml'.";

        Platform.runLater(() -> {
            tcFechaIngreso.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Internaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getFechaIngreso()));

            tcFechaAlta.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Internaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getFechaAlta()));

            log.info("loading table items");
            fichasList.setAll(dao.showByPatient(paciente));

            indexI.getColumns().setAll(tcFechaIngreso, tcFechaAlta);
            indexI.setItems(fichasList);

            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexI, fichasList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    internacion = newValue;
                    log.info("Item selected." + internacion.getId());
                }
            });

            btnEdit.setOnAction((event) -> {
                if (internacion != null)
                    displayModal(event);
                else
                    DialogBox.displayWarning();
            });

            btnDelete.setOnAction((event) -> {
                if (internacion != null) {
                    if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                        dao.delete(internacion.getId());
                        Internaciones selectedItem = indexI.getSelectionModel().getSelectedItem();
                        indexI.getItems().remove(selectedItem);
                        indexI.refresh();
                        internacion = null;
                        DialogBox.displaySuccess();
                        log.info("Item deleted.");
                    }
                } else
                    DialogBox.displayWarning();
            });
            // search filter
            filteredData = new FilteredList<>(fichasList, p -> true);
            txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(paciente -> newValue == null || newValue.isEmpty()
                        || paciente.getFechaIngreso().toString().toLowerCase().contains(newValue.toLowerCase())
                        || paciente.getFechaIngreso().toString().toLowerCase().contains(newValue.toLowerCase()));
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.INTERNACION.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(internacion);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Editar - Internación");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                refreshTable();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        fichasList.clear();
        fichasList.setAll(dao.showByPatient(paciente));
        indexI.setItems(fichasList);
        tablePagination.setPageFactory((index) -> TableUtil.createPage(indexI, fichasList, tablePagination, index, 20));
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
}
