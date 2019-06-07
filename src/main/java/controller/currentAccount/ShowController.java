package controller.currentAccount;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import dao.CuentasCorrientesHome;
import javafx.application.Platform;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.CuentasCorrientes;
import model.Propietarios;
import utils.DialogBox;
import utils.Route;
import utils.ViewSwitcher;

public class ShowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<CuentasCorrientes> indexCA;

    private TreeItem<CuentasCorrientes> root;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private CuentasCorrientesHome dao = new CuentasCorrientesHome();

    private CuentasCorrientes cuentaCorriente;

    private Propietarios propietario;

    final ObservableList<CuentasCorrientes> cuentasCorrientes = FXCollections.observableArrayList();

    // Table columns
    private JFXTreeTableColumn<CuentasCorrientes, Propietarios> propietarios = new JFXTreeTableColumn<CuentasCorrientes, Propietarios>(
            "Propietarios");

    private JFXTreeTableColumn<CuentasCorrientes, String> descripcion = new JFXTreeTableColumn<CuentasCorrientes, String>(
            "Descripción");

    private JFXTreeTableColumn<CuentasCorrientes, BigDecimal> monto = new JFXTreeTableColumn<CuentasCorrientes, BigDecimal>(
            "Monto");

    private JFXTreeTableColumn<CuentasCorrientes, Date> fecha = new JFXTreeTableColumn<CuentasCorrientes, Date>(
            "Fecha");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexCA != null : "fx:id=\"indexCA\" was not injected: check your FXML file 'show.fxml'.";
        Platform.runLater(() -> {
            log.info("creating table");
            propietarios.setPrefWidth(200);
            propietarios.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<CuentasCorrientes, Propietarios> param) -> new ReadOnlyObjectWrapper<Propietarios>(
                            param.getValue().getValue().getPropietarios()));
            descripcion.setPrefWidth(200);
            descripcion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<CuentasCorrientes, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getDescripcion()));

            monto.setPrefWidth(150);
            monto.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<CuentasCorrientes, BigDecimal> param) -> new ReadOnlyObjectWrapper<BigDecimal>(
                            param.getValue().getValue().getMonto()));

            fecha.setPrefWidth(150);
            fecha.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<CuentasCorrientes, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFecha()));
            log.info("loading table items");

            cuentasCorrientes.setAll(dao.showByOwner(propietario));

            root = new RecursiveTreeItem<CuentasCorrientes>(cuentasCorrientes, RecursiveTreeObject::getChildren);
            indexCA.getColumns().setAll(fecha, propietarios, descripcion, monto);
            indexCA.setShowRoot(false);
            indexCA.setRoot(root);

            // Handle ListView selection changes.
            indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    cuentaCorriente = newValue.getValue();
                    log.info("Item selected.");
                }
            });

            btnEdit.setOnAction((event) -> {
                if (cuentaCorriente != null)
                    displayModal(event);
                else
                    DialogBox.displayWarning();
            });

            btnDelete.setOnAction((event) -> {
                if (cuentaCorriente != null)
                    if (DialogBox.confirmDialog("¿Desea guardar el registro?")) {
                        dao.delete(cuentaCorriente.getId());
                        TreeItem<CuentasCorrientes> selectedItem = indexCA.getSelectionModel().getSelectedItem();
                        indexCA.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        indexCA.refresh();
                        log.info("Item deleted.");
                    }
            });
        });
        // search filter
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            indexCA.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty())
                    return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getValue().toString().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (item.getValue().getDescripcion().toLowerCase().contains(lowerCaseFilter))
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

    public void setObject(Propietarios propietario) {
        this.propietario = propietario;
    } // Propietarios

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void refreshTable() {
        cuentasCorrientes.clear();
        cuentasCorrientes.setAll(dao.showByOwner(propietario));
        root = new RecursiveTreeItem<CuentasCorrientes>(cuentasCorrientes, RecursiveTreeObject::getChildren);
        indexCA.setRoot(root);
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.CUENTACORRIENTE.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(cuentaCorriente);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Editar - Cuenta Corriente");
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
}
