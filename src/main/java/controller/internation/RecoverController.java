package controller.internation;

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

import dao.InternacionesHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Internaciones;
import model.Pacientes;
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
    private JFXTreeTableView<Internaciones> indexI;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private InternacionesHome dao = new InternacionesHome();

    private Internaciones internacion;

    final ObservableList<Internaciones> fichasList = FXCollections.observableArrayList();

    private TreeItem<Internaciones> root;

    // Table columns

    private JFXTreeTableColumn<Internaciones, Pacientes> pacientes = new JFXTreeTableColumn<Internaciones, Pacientes>(
            "Pacientes - (ficha)");

    private JFXTreeTableColumn<Internaciones, Date> fechaIngreso = new JFXTreeTableColumn<Internaciones, Date>(
            "Fecha Ingreso");

    private JFXTreeTableColumn<Internaciones, Date> fechaAlta = new JFXTreeTableColumn<Internaciones, Date>(
            "Fecha Alta");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexI != null : "fx:id=\"indexI\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

        Platform.runLater(() -> {
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<Internaciones, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getPacientes()));

            fechaIngreso.setPrefWidth(150);
            fechaIngreso.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Internaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFechaIngreso()));

            fechaAlta.setPrefWidth(150);
            fechaAlta.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Internaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFechaAlta()));

            log.info("loading table items");
            fichasList.setAll(dao.displayDeletedRecords());

            root = new RecursiveTreeItem<Internaciones>(fichasList, RecursiveTreeObject::getChildren);

            indexI.getColumns().setAll(pacientes, fechaIngreso, fechaAlta);
            indexI.setShowRoot(false);
            indexI.setRoot(root);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexI, fichasList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    internacion = newValue.getValue();
                    log.info("Item selected." + internacion.getId());
                }
            });

            btnRecover.setOnAction((event) -> {
                if (internacion != null) {
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        dao.recover(internacion.getId());
                        TreeItem<Internaciones> selectedItem = indexI.getSelectionModel().getSelectedItem();
                        indexI.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        indexI.refresh();
                        internacion = null;
                        DialogBox.displaySuccess();
                        log.info("Item recovered.");
                    }
                } else
                    DialogBox.displayWarning();
            });
            // search filter
            txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                indexI.setPredicate(item -> {
                    if (newValue == null || newValue.isEmpty())
                        return true;

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (item.getValue().toString().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    return false;
                });
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
