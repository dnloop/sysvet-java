package controller.clinicHistory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.HistoriaClinicaHome;
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
import model.FichasClinicas;
import model.Pacientes;
import utils.DialogBox;
import utils.Route;
import utils.TableUtil;
import utils.ViewSwitcher;

public class IndexController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableView<FichasClinicas> indexCH;

    @FXML
    private Pagination tablePagination;

    // table columns
    @FXML
    private TableColumn<FichasClinicas, Pacientes> tcPaciente;

    @FXML
    private TableColumn<FichasClinicas, Integer> tcFicha;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private HistoriaClinicaHome daoHC = new HistoriaClinicaHome();

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    final ObservableList<FichasClinicas> pacientesList = FXCollections.observableArrayList();

    private FichasClinicas fichasList;

    private FilteredList<FichasClinicas> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'index.fxml'.";
        assert indexCH != null : "fx:id=\"indexCH\" was not injected: check your FXML file 'index.fxml'.";
        assert tcPaciente != null : "fx:id=\"tcPaciente\" was not injected: check your FXML file 'index.fxml'.";
        assert tcFicha != null : "fx:id=\"tcFicha\" was not injected: check your FXML file 'index.fxml'.";

        tcPaciente.setCellValueFactory(
                (TableColumn.CellDataFeatures<FichasClinicas, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue().getPacientes()));

        tcFicha.setCellValueFactory(
                (TableColumn.CellDataFeatures<FichasClinicas, Integer> param) -> new ReadOnlyObjectWrapper<Integer>(
                        param.getValue().getId()));

        log.info("loading table items");

        pacientesList.setAll(daoHC.displayRecordsWithClinicHistory());

        indexCH.getColumns().setAll(tcPaciente, tcFicha);

        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexCH, pacientesList, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexCH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fichasList = newValue;
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnShow.setOnAction((event) -> {
            if (fichasList != null)
                displayShow(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (fichasList != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    daoHC.deleteAll(fichasList.getId());
                    FichasClinicas selectedItem = indexCH.getSelectionModel().getSelectedItem();
                    indexCH.getItems().remove(selectedItem);
                    refreshTable();
                    fichasList = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        filteredData = new FilteredList<>(pacientesList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> newValue == null || newValue.isEmpty()
                    || ficha.getPacientes().getNombre().toLowerCase().contains(newValue.toLowerCase()));
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

    private void setView(Node node) {
        ViewSwitcher.loadNode(node);
    }

    private void displayShow(Event event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.HISTORIACLINICA.showView()));
        try {
            Node node = fxmlLoader.load();
            ShowController sc = fxmlLoader.getController();
            sc.setObject(fichasList);
            setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayNew(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.HISTORIACLINICA.newView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            NewController sc = fxmlLoader.getController();
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Nuevo elemento - Historia Clínica");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHiding((stageEvent) -> {
                refreshTable();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        pacientesList.clear();
        pacientesList.setAll(daoHC.displayRecordsWithClinicHistory());
        indexCH.setItems(pacientesList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexCH, pacientesList, tablePagination, index, 20));
    }

    private void changeTableView(int index, int limit) {

        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, pacientesList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<FichasClinicas> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCH.comparatorProperty());

        indexCH.setItems(sortedData);

    }
}
