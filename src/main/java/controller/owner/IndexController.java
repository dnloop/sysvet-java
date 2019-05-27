package controller.owner;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import dao.PropietariosHome;
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
import model.Propietarios;
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
    private JFXTreeTableView<Propietarios> indexPO;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private static PropietariosHome dao = new PropietariosHome();

    private Propietarios propietario;

    final ObservableList<Propietarios> propietarios = FXCollections.observableArrayList();

    TreeItem<Propietarios> root;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexPO != null : "fx:id=\"indexPO\" was not injected: check your FXML file 'index.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'index.fxml'.";

        log.info("creating table");

        JFXTreeTableColumn<Propietarios, String> nombre = new JFXTreeTableColumn<Propietarios, String>("Nombre");
        nombre.setPrefWidth(200);
        nombre.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Propietarios, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getNombre()));

        JFXTreeTableColumn<Propietarios, String> apellido = new JFXTreeTableColumn<Propietarios, String>("Apellido");
        apellido.setPrefWidth(200);
        apellido.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Propietarios, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getApellido()));

        JFXTreeTableColumn<Propietarios, String> domicilio = new JFXTreeTableColumn<Propietarios, String>("Domicilio");
        domicilio.setPrefWidth(200);
        domicilio.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Propietarios, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getDomicilio()));

        JFXTreeTableColumn<Propietarios, String> telCel = new JFXTreeTableColumn<Propietarios, String>(
                "Teléfono Celular");
        telCel.setPrefWidth(200);
        telCel.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Propietarios, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getTelCel()));

        JFXTreeTableColumn<Propietarios, String> telFijo = new JFXTreeTableColumn<Propietarios, String>(
                "Teléfono Fijo");
        telFijo.setPrefWidth(200);
        telFijo.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Propietarios, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getTelFijo()));

        JFXTreeTableColumn<Propietarios, String> mail = new JFXTreeTableColumn<Propietarios, String>("Mail");
        mail.setPrefWidth(200);
        mail.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Propietarios, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getMail()));

        JFXTreeTableColumn<Propietarios, Localidades> localidad = new JFXTreeTableColumn<Propietarios, Localidades>(
                "Localidades");
        localidad.setPrefWidth(200);
        localidad.setCellValueFactory((
                TreeTableColumn.CellDataFeatures<Propietarios, Localidades> param) -> new ReadOnlyObjectWrapper<Localidades>(
                        param.getValue().getValue().getLocalidades()));

        log.info("loading table items");
        propietarios.setAll(dao.displayRecords());

        root = new RecursiveTreeItem<Propietarios>(propietarios, RecursiveTreeObject::getChildren);
        indexPO.getColumns().setAll(nombre, apellido, domicilio, telCel, telFijo, mail, localidad);
        indexPO.setShowRoot(false);
        indexPO.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexPO, propietarios, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexPO.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                propietario = newValue.getValue();
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnEdit.setOnAction((event) -> {
            if (propietario != null)
                displayEdit(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (propietario != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(propietario.getId());
                    TreeItem<Propietarios> selectedItem = indexPO.getSelectionModel().getSelectedItem();
                    indexPO.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    refreshTable();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.PROPIETARIO.newView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            NewController sc = fxmlLoader.getController();
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Nuevo elemento - Propietario");
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

    private void displayEdit(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.PROPIETARIO.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController mdc = fxmlLoader.getController();
            mdc.setObject(propietario);
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Propietario");
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

    private void refreshTable() {
        propietarios.clear();
        propietarios.setAll(dao.displayRecords());
        root = new RecursiveTreeItem<Propietarios>(propietarios, RecursiveTreeObject::getChildren);
        indexPO.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexPO, propietarios, tablePagination, index, 20));
    }
}
