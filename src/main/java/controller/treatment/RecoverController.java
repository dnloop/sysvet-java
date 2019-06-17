package controller.treatment;

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

import dao.TratamientosHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Pacientes;
import model.Tratamientos;
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
    private JFXTreeTableView<Tratamientos> indexTR;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private static TratamientosHome dao = new TratamientosHome();

    private Tratamientos tratamiento;

    private TreeItem<Tratamientos> root;

    final ObservableList<Tratamientos> pacientesList = FXCollections.observableArrayList();

    // Table columns
    private JFXTreeTableColumn<Tratamientos, Pacientes> pacientes = new JFXTreeTableColumn<Tratamientos, Pacientes>(
            "Pacientes");

    private JFXTreeTableColumn<Tratamientos, Date> fecha = new JFXTreeTableColumn<Tratamientos, Date>("Fecha");

    private JFXTreeTableColumn<Tratamientos, Date> hora = new JFXTreeTableColumn<Tratamientos, Date>("Hora");

    private JFXTreeTableColumn<Tratamientos, String> procAdicional = new JFXTreeTableColumn<Tratamientos, String>(
            "Pacientes");

    @SuppressWarnings("unchecked")

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexTR != null : "fx:id=\"indexTR\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        Platform.runLater(() -> {
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<Tratamientos, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getFichasClinicas().getPacientes()));

            fecha.setPrefWidth(150);
            fecha.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Tratamientos, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFecha()));

            hora.setPrefWidth(150);
            hora.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Tratamientos, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getHora()));

            log.info("loading table items");
            pacientesList.setAll(dao.showByInternacion((tratamiento.getFichasClinicas())));

            root = new RecursiveTreeItem<Tratamientos>(pacientesList, RecursiveTreeObject::getChildren);

            indexTR.getColumns().setAll(pacientes, fecha, hora, procAdicional);
            indexTR.setShowRoot(false);
            indexTR.setRoot(root);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexTR, pacientesList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexTR.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    tratamiento = newValue.getValue();
                    log.info("Item selected." + tratamiento.getId());
                }
            });

            btnRecover.setOnAction((event) -> {
                if (tratamiento != null) {
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        dao.recover(tratamiento.getId());
                        TreeItem<Tratamientos> selectedItem = indexTR.getSelectionModel().getSelectedItem();
                        indexTR.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        refreshTable();
                        tratamiento = null;
                        DialogBox.displaySuccess();
                        log.info("Item recovered.");
                    }
                } else
                    DialogBox.displayWarning();
            });
            // search filter
            txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                indexTR.setPredicate(item -> {
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

    private void refreshTable() {
        pacientesList.clear();
        pacientesList.setAll(dao.showByInternacion(tratamiento.getFichasClinicas()));
        root = new RecursiveTreeItem<Tratamientos>(pacientesList, RecursiveTreeObject::getChildren);
        indexTR.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexTR, pacientesList, tablePagination, index, 20));
    }
}
