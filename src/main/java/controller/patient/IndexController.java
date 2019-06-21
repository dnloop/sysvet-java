package controller.patient;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
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
import utils.DialogBox;
import utils.Route;
import utils.TableUtil;

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
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableView<Pacientes> indexPA;

    @FXML
    private Pagination tablePagination;

    // table columns
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

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private PacientesHome dao = new PacientesHome();

    private Pacientes paciente;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private FilteredList<Pacientes> filteredData;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexPA != null : "fx:id=\"indexPA\" was not injected: check your FXML file 'index.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'index.fxml'.";

        log.info("creating table");
        nombre.setCellValueFactory((TableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                param.getValue().getNombre()));

        especie.setCellValueFactory(
                (TableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getEspecie()));

        raza.setCellValueFactory((TableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                param.getValue().getRaza()));

        sexo.setCellValueFactory((TableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                param.getValue().getSexo()));

        temp.setCellValueFactory((TableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                param.getValue().getTemperamento()));

        pelaje.setCellValueFactory((TableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                param.getValue().getPelaje()));

        fecha.setCellValueFactory(
                (TableColumn.CellDataFeatures<Pacientes, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                        param.getValue().getFechaNacimiento()));
        log.info("loading table items");

        pacientesList.setAll(dao.displayRecords());
        indexPA.getColumns().setAll(nombre, especie, raza, sexo, temp, pelaje, fecha);
        indexPA.setItems(pacientesList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexPA, pacientesList, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexPA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                paciente = newValue;
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnEdit.setOnAction((event) -> {
            if (paciente != null)
                displayEdit(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (paciente != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(paciente.getId());
                    Pacientes selectedItem = indexPA.getSelectionModel().getSelectedItem();
                    indexPA.getItems().remove(selectedItem);
                    refreshTable();
                    paciente = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
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

    /**
     *
     * Class Methods
     *
     */

    private void displayEdit(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.PACIENTE.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController mdc = fxmlLoader.getController();
            mdc.setObject(paciente);
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Editar - Paciente");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                refreshTable();
            });
            mdc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void displayNew(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.PACIENTE.newView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            NewController sc = fxmlLoader.getController();
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Nuevo elemento - Paciente");
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
        pacientesList.setAll(dao.displayRecords());
        indexPA.setItems(pacientesList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexPA, pacientesList, tablePagination, index, 20));
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, pacientesList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Pacientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexPA.comparatorProperty());
        indexPA.setItems(sortedData);
    }
}
