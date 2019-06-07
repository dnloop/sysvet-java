package controller.vaccine;

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

import dao.VacunasHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Pacientes;
import model.Vacunas;
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
    private JFXTreeTableView<Vacunas> indexVC;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private VacunasHome dao = new VacunasHome();

    private Vacunas vacuna;

    final ObservableList<Vacunas> vaccineList = FXCollections.observableArrayList();

    private TreeItem<Vacunas> root;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexVC != null : "fx:id=\"indexVC\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

        Platform.runLater(() -> {
            JFXTreeTableColumn<Vacunas, Pacientes> pacientes = new JFXTreeTableColumn<Vacunas, Pacientes>("Pacientes");
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<Vacunas, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getPacientes()));

            JFXTreeTableColumn<Vacunas, String> descripcion = new JFXTreeTableColumn<Vacunas, String>("Descripción");
            descripcion.setPrefWidth(200);
            descripcion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Vacunas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getDescripcion())));

            JFXTreeTableColumn<Vacunas, Date> fecha = new JFXTreeTableColumn<Vacunas, Date>("Fecha Inicio");
            fecha.setPrefWidth(150);
            fecha.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Vacunas, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFecha()));

            log.info("loading table items");

            vaccineList.setAll(dao.displayDeletedRecords());
            root = new RecursiveTreeItem<Vacunas>(vaccineList, RecursiveTreeObject::getChildren);

            indexVC.getColumns().setAll(pacientes, fecha, descripcion, fecha);
            indexVC.setShowRoot(false);
            indexVC.setRoot(root);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexVC, vaccineList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexVC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    vacuna = newValue.getValue();
                    log.info("Item selected.");
                }
            });

            btnRecover.setOnAction((event) -> {
                if (vacuna != null) {
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        dao.recover(vacuna.getId());
                        TreeItem<Vacunas> selectedItem = indexVC.getSelectionModel().getSelectedItem();
                        indexVC.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        refreshTable();
                        vacuna = null;
                        DialogBox.displaySuccess();
                        log.info("Item recovered.");
                    }
                } else
                    DialogBox.displayWarning();
            });
            // search filter
            txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                indexVC.setPredicate(item -> {
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
        vaccineList.clear();
        vaccineList.setAll(dao.displayDeletedRecords());
        root = new RecursiveTreeItem<Vacunas>(vaccineList, RecursiveTreeObject::getChildren);
        indexVC.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexVC, vaccineList, tablePagination, index, 20));
    }
}
