package controller.owner;

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
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Localidades;
import model.Propietarios;
import utils.DialogBox;
import utils.TableUtil;

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
    private JFXTreeTableView<Propietarios> indexPO;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private PropietariosHome dao = new PropietariosHome();

    private Propietarios propietario;

    final ObservableList<Propietarios> propietarios = FXCollections.observableArrayList();

    TreeItem<Propietarios> root;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexPO != null : "fx:id=\"indexPO\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

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

        btnRecover.setOnAction((event) -> {
            if (propietario != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(propietario.getId());
                    TreeItem<Propietarios> selectedItem = indexPO.getSelectionModel().getSelectedItem();
                    indexPO.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    indexPO.refresh();
                    log.info("Item recovered.");
                    propietario = null;
                }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            indexPO.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty())
                    return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getValue().getNombre().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (item.getValue().getApellido().toLowerCase().contains(lowerCaseFilter))
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

    private void refreshTable() {
        propietarios.clear();
        propietarios.setAll(dao.displayRecords());
        root = new RecursiveTreeItem<Propietarios>(propietarios, RecursiveTreeObject::getChildren);
        indexPO.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexPO, propietarios, tablePagination, index, 20));
    }
}
