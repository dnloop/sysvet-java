package controller.currentAccount;

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
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.CuentasCorrientes;
import model.Propietarios;
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
    private JFXTreeTableView<CuentasCorrientes> indexCA;

    @FXML
    private Pagination tablePagination;

    private TreeItem<CuentasCorrientes> root;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private CuentasCorrientesHome dao = new CuentasCorrientesHome();

    private CuentasCorrientes cuentaCorriente;

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
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexCA != null : "fx:id=\"indexCA\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
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

            cuentasCorrientes.setAll(dao.displayDeletedRecords());

            root = new RecursiveTreeItem<CuentasCorrientes>(cuentasCorrientes, RecursiveTreeObject::getChildren);
            indexCA.getColumns().setAll(fecha, propietarios, descripcion, monto);
            indexCA.setShowRoot(false);
            indexCA.setRoot(root);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexCA, cuentasCorrientes, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    cuentaCorriente = newValue.getValue();
                    log.info("Item selected.");
                }
            });

            btnRecover.setOnAction((event) -> {
                if (cuentaCorriente != null) {
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        try {
                            dao.recover(cuentaCorriente.getId());
                            TreeItem<CuentasCorrientes> selectedItem = indexCA.getSelectionModel().getSelectedItem();
                            indexCA.getSelectionModel().getSelectedItem().getParent().getChildren()
                                    .remove(selectedItem);
                            indexCA.refresh();
                            cuentaCorriente = null;
                            DialogBox.displaySuccess();
                        } catch (RuntimeException e) {
                            DialogBox.setHeader("Fallo en la recuperación del registro.");
                            DialogBox.setContent("Motivo: " + e.getMessage());
                            DialogBox.displayError();
                        } // seems overkill but who knows =)

                        log.info("Item recovered.");
                    }
                } else
                    DialogBox.displayWarning();
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
                return false;
            });
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
}
