package controller.location;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;

import dao.LocalidadesHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
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
    private JFXTreeTableView<Localidades> indexLC;

    @FXML
    private JFXTreeTableColumn<Localidades, String> nombre;

    @FXML
    private JFXTreeTableColumn<Localidades, Integer> codigoPostal;

    @FXML
    private JFXTreeTableColumn<Localidades, Provincias> provincia;

    @FXML
    private Pagination tablePagination;

    @FXML
    private JFXSlider pageSlider;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private LocalidadesHome dao = new LocalidadesHome();

    private Localidades loc;

    final ObservableList<Localidades> localidades = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexLC != null : "fx:id=\"indexLC\" was not injected: check your FXML file 'recover.fxml'.";
        assert nombre != null : "fx:id=\"nombre\" was not injected: check your FXML file 'recover.fxml'.";
        assert codigoPostal != null : "fx:id=\"codigoPostal\" was not injected: check your FXML file 'recover.fxml'.";
        assert provincia != null : "fx:id=\"provincia\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        assert pageSlider != null : "fx:id=\"pageSlider\" was not injected: check your FXML file 'recover.fxml'.";

        log.info("creating table");
        nombre.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Localidades, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getNombre()));
        codigoPostal.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Localidades, Integer> param) -> new ReadOnlyObjectWrapper<Integer>(
                        param.getValue().getValue().getCodPostal()));
        provincia.setCellValueFactory((
                TreeTableColumn.CellDataFeatures<Localidades, Provincias> param) -> new ReadOnlyObjectWrapper<Provincias>(
                        param.getValue().getValue().getProvincias()));

        log.info("loading table items");
        localidades.setAll(dao.displayDeletedRecords(0));
        dao.pageCountDeletedResult();
        Long size = dao.getTotalRecords();
        indexLC.setShowRoot(false);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexLC, localidades, tablePagination, index, 20));

        pageSlider.setMax(Math.ceil(size / 100));
        // Handle Slider selection changes.
        pageSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging)
                loadRecords((int) Math.round(pageSlider.getValue()));
        });

        // Handle ListView selection changes.
        indexLC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loc = newValue.getValue();
                log.info("Item selected.");
            }
        });

        btnRecover.setOnAction((event) -> {
            if (loc != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(loc.getId());
                    TreeItem<Localidades> selectedItem = indexLC.getSelectionModel().getSelectedItem();
                    indexLC.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    indexLC.refresh();
                    loc = null;
                    DialogBox.displaySuccess();
                    log.info("Item recovered.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            indexLC.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty())
                    return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getValue().getNombre().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (item.getValue().getCodPostal().toString().contains(lowerCaseFilter))
                    return true;
                return false;
            });
        });
    }

    /**
     *
     * Class Methods
     *
     */

    private void loadRecords(Integer page) {
        localidades.setAll(dao.displayDeletedRecords(page));
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexLC, localidades, tablePagination, index, 20));
    }

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }
}
