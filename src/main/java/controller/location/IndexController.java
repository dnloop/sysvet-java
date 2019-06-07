package controller.location;

import java.io.IOException;
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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
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

    private Integer id;

    final ObservableList<Localidades> localidades = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexLC != null : "fx:id=\"indexLC\" was not injected: check your FXML file 'index.fxml'.";
        assert pageSlider != null : "fx:id=\"pageSlider\" was not injected: check your FXML file 'index.fxml'.";

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
        localidades.setAll(dao.displayRecords(0));
        dao.pageCountResult();
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
                id = loc.getId();
                log.info("Item selected.");
            }
        });

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
                    TreeItem<Localidades> selectedItem = indexLC.getSelectionModel().getSelectedItem();
                    indexLC.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    indexLC.refresh();
                    id = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
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
        localidades.setAll(dao.displayRecords(page));
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexLC, localidades, tablePagination, index, 20));
    }
}
