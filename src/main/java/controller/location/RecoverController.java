package controller.location;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;

import dao.LocalidadesHome;
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
import model.Localidades;
import model.Provincias;
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

    final ObservableList<Localidades> locList = FXCollections.observableArrayList();

    private FilteredList<Localidades> filteredData;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexLC != null : "fx:id=\"indexLC\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcNombre != null : "fx:id=\"tcNombre\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcCodPost != null : "fx:id=\"tcCodPost\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcProvincia != null : "fx:id=\"tcProvincia\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        assert pageSlider != null : "fx:id=\"pageSlider\" was not injected: check your FXML file 'recover.fxml'.";

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
        locList.setAll(dao.displayDeletedRecords(0));
        dao.pageCountDeletedResult();
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
                log.info("Item selected.");
            }
        });

        btnRecover.setOnAction((event) -> {
            if (loc != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(loc.getId());
                    Localidades selectedItem = indexLC.getSelectionModel().getSelectedItem();
                    indexLC.getItems().remove(selectedItem);
                    indexLC.refresh();
                    loc = null;
                    DialogBox.displaySuccess();
                    log.info("Item recovered.");
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

    private void loadRecords(Integer page) {
        locList.setAll(dao.displayDeletedRecords(page));
        tablePagination.setPageFactory((index) -> TableUtil.createPage(indexLC, locList, tablePagination, index, 20));
    }

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
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
