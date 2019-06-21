package controller.location;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;

import controller.internation.NewController;
import dao.LocalidadesHome;
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
import model.Localidades;
import model.Provincias;
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
    private TableView<Localidades> indexLC;

    @FXML
    private TableColumn<Localidades, String> tcNombre;

    @FXML
    private TableColumn<Localidades, Integer> tcCodPost;

    @FXML
    private TableColumn<Localidades, Provincias> tcProvincia;

    @FXML
    private Pagination tablePagination;

    @FXML
    private JFXSlider pageSlider;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private LocalidadesHome dao = new LocalidadesHome();

    private Localidades loc;

    private Integer id;

    final ObservableList<Localidades> locList = FXCollections.observableArrayList();

    private FilteredList<Localidades> filteredData;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexLC != null : "fx:id=\"indexLC\" was not injected: check your FXML file 'index.fxml'.";
        assert pageSlider != null : "fx:id=\"pageSlider\" was not injected: check your FXML file 'index.fxml'.";

        log.info("creating table");
        tcNombre.setCellValueFactory(
                (TableColumn.CellDataFeatures<Localidades, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getNombre()));
        tcCodPost.setCellValueFactory(
                (TableColumn.CellDataFeatures<Localidades, Integer> param) -> new ReadOnlyObjectWrapper<Integer>(
                        param.getValue().getCodPostal()));
        tcProvincia.setCellValueFactory(
                (TableColumn.CellDataFeatures<Localidades, Provincias> param) -> new ReadOnlyObjectWrapper<Provincias>(
                        param.getValue().getProvincias()));

        log.info("loading table items");
        locList.setAll(dao.displayRecords(0));
        dao.pageCountResult();
        Long size = dao.getTotalRecords();
        indexLC.setItems(locList);
        tablePagination.setPageFactory((index) -> TableUtil.createPage(indexLC, locList, tablePagination, index, 20));

        pageSlider.setMax(Math.ceil(size / 100));
        // Handle Slider selection changes.
        pageSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging)
                loadRecords((int) Math.round(pageSlider.getValue()));
        });

        // Handle ListView selection changes.
        indexLC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loc = newValue;
                id = loc.getId();
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnEdit.setOnAction((event) -> {
            if (id != null)
                displayEdit(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (id != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(id);
                    Localidades selectedItem = indexLC.getSelectionModel().getSelectedItem();
                    indexLC.getItems().remove(selectedItem);
                    indexLC.refresh();
                    id = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(locList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(localidad -> newValue == null || newValue.isEmpty()
                    || localidad.getNombre().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /**
     *
     * Class Methods
     *
     */

    private void displayNew(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.LOCALIDAD.newView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            NewController sc = fxmlLoader.getController();
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Nuevo elemento - Localidades");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHiding((stageEvent) -> {
                indexLC.refresh();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayEdit(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.LOCALIDAD.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController mdc = fxmlLoader.getController();
            mdc.setObject(this.loc);
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Localidad");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                indexLC.refresh();
            });
            mdc.showModal(stage);

        } catch (IOException e) {
            log.error("Cannot display Edit view: " + e.getCause());
            DialogBox.setHeader(e.getCause().toString());
            DialogBox.setContent(e.getMessage());
            DialogBox.displayError();
        }

    }

    private void loadRecords(Integer page) {
        locList.setAll(dao.displayRecords(page));
        tablePagination.setPageFactory((index) -> TableUtil.createPage(indexLC, locList, tablePagination, index, 20));
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, locList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Localidades> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexLC.comparatorProperty());
        indexLC.setItems(sortedData);
    }
}
