package controller.currentAccount;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.CuentasCorrientesHome;
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
import model.Propietarios;
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
    private TableView<Propietarios> indexCA;

    @FXML
    private Pagination tablePagination;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    // Table columns
    @FXML
    private TableColumn<Propietarios, String> tcNombre;

    @FXML
    private TableColumn<Propietarios, String> tcApellido;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private CuentasCorrientesHome dao = new CuentasCorrientesHome();

    private Propietarios propietario;

    final ObservableList<Propietarios> propietariosList = FXCollections.observableArrayList();

    private FilteredList<Propietarios> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexCA != null : "fx:id=\"indexCA\" was not injected: check your FXML file 'index.fxml'.";
        // this should be a helper class to load everything
        log.info("creating table");

        tcNombre.setCellValueFactory(
                (TableColumn.CellDataFeatures<Propietarios, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getNombre()));

        tcApellido.setCellValueFactory(
                (TableColumn.CellDataFeatures<Propietarios, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getApellido()));

        log.info("loading table items");
        propietariosList.setAll(dao.displayRecordsWithOwners());

        indexCA.getColumns().setAll(tcNombre, tcApellido);
        indexCA.setItems(propietariosList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexCA, propietariosList, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                propietario = newValue;
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnShow.setOnAction((event) -> {
            if (propietario != null)
                displayShow(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (propietario != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.deleteAll(propietario.getId());
                    Propietarios selectedItem = indexCA.getSelectionModel().getSelectedItem();
                    indexCA.getItems().remove(selectedItem);
                    refreshTable();
                    propietario = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(propietariosList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cuenta -> newValue == null || newValue.isEmpty()
                    || cuenta.getNombre().toLowerCase().contains(newValue.toLowerCase())
                    || cuenta.getApellido().toLowerCase().contains(newValue.toLowerCase()));
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.CUENTACORRIENTE.showView()));
        try {
            Node node = fxmlLoader.load();
            ShowController sc = fxmlLoader.getController();
            sc.setObject(propietario);
            setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayNew(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.CUENTACORRIENTE.newView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            NewController sc = fxmlLoader.getController();
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Nuevo elemento - Cuenta Corriente");
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
        propietariosList.clear();
        propietariosList.setAll(dao.displayRecordsWithOwners());
        indexCA.setItems(propietariosList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexCA, propietariosList, tablePagination, index, 20));
    }

    private void changeTableView(int index, int limit) {

        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, propietariosList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Propietarios> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCA.comparatorProperty());

        indexCA.setItems(sortedData);

    }
}
