package controller.location;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Localidades;
import model.Provincias;

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
    private Pagination tablePagination;

    @FXML
    private JFXSlider pageSlider;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    static LocalidadesHome dao = new LocalidadesHome();

    private Localidades loc;

    private Integer id;

    final ObservableList<Localidades> localidades = FXCollections.observableArrayList();

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexLC != null : "fx:id=\"indexLC\" was not injected: check your FXML file 'index.fxml'.";
        assert pageSlider != null : "fx:id=\"pageSlider\" was not injected: check your FXML file 'index.fxml'.";

        log.info("creating table");

        JFXTreeTableColumn<Localidades, String> nombre = new JFXTreeTableColumn<Localidades, String>("Nombre");
        nombre.setPrefWidth(200);
        nombre.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Localidades, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getNombre()));

        JFXTreeTableColumn<Localidades, Integer> codigoPostal = new JFXTreeTableColumn<Localidades, Integer>(
                "Código Postal");
        codigoPostal.setPrefWidth(150);
        codigoPostal.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Localidades, Integer> param) -> new ReadOnlyObjectWrapper<Integer>(
                        param.getValue().getValue().getCodPostal()));

        JFXTreeTableColumn<Localidades, Provincias> provincia = new JFXTreeTableColumn<Localidades, Provincias>(
                "Provincia");
        provincia.setPrefWidth(150);
        provincia.setCellValueFactory((
                TreeTableColumn.CellDataFeatures<Localidades, Provincias> param) -> new ReadOnlyObjectWrapper<Provincias>(
                        param.getValue().getValue().getProvincias()));

        log.info("loading table items");
        localidades.setAll(dao.displayRecords(0));
        dao.pageCountResult();
        Long size = dao.getTotalRecords();
        pageSlider.setMax(Math.ceil(size / 100));

        tablePagination.setPageFactory((index) -> createPage(indexLC, localidades, tablePagination, index));

        TreeItem<Localidades> root = new RecursiveTreeItem<Localidades>(localidades, RecursiveTreeObject::getChildren);
        indexLC.getColumns().setAll(nombre, codigoPostal, provincia);
        indexLC.setShowRoot(false);
        indexLC.setRoot(root);

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
                displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (id != null)
                confirmDialog();
            else
                displayWarning();
        });
        // TODO add search filter
    }

    /**
     *
     * Class Methods
     *
     */

    private void displayEdit(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/location/modalDialog.fxml"));
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
            e.printStackTrace();
        }

    }

    private void confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción.");
        alert.setContentText("¿Desea eliminar el registro?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.delete(id);
            indexLC.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexLC.refresh();
            log.info("Item deleted.");
        }
    }

    private void displayWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia.");
        alert.setHeaderText("Elemento vacío.");
        alert.setContentText("No se seleccionó ningún elemento de la lista. Elija un ítem e intente nuevamente.");
        alert.setResizable(true);

        alert.showAndWait();
    }

    private void loadRecords(Integer page) {
        localidades.setAll(dao.displayRecords(page));
        tablePagination.setPageFactory((index) -> createPage(indexLC, localidades, tablePagination, index));
    }

    @SuppressWarnings("hiding")
    private static <Localidades extends RecursiveTreeObject<Localidades>> Node createPage(
            JFXTreeTableView<Localidades> tableView, ObservableList<Localidades> data, Pagination pagination,
            int pageIndex) {
        int fromIndex = pageIndex * 20;
        int toIndex = Math.min(fromIndex + 20, data.size());
        tableView.setRoot(new RecursiveTreeItem<Localidades>(
                FXCollections.observableArrayList(data.subList(fromIndex, toIndex)), RecursiveTreeObject::getChildren));
        pagination.setPageCount((data.size() / 20 + 1));

        return new BorderPane();
    }

}
