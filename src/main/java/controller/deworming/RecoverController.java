package controller.deworming;

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

import dao.DesparasitacionesHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Desparasitaciones;
import model.Pacientes;
import utils.DialogBox;
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
    private JFXTreeTableView<Desparasitaciones> indexD;

    @FXML
    private Pagination tablePagination;

    private TreeItem<Desparasitaciones> root;

    private final ObservableList<Desparasitaciones> desparasitaciones = FXCollections.observableArrayList();

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private DesparasitacionesHome dao = new DesparasitacionesHome();

    private Desparasitaciones desparasitacion;

    private Pacientes paciente;

    // Table columns
    private JFXTreeTableColumn<Desparasitaciones, String> tratamiento = new JFXTreeTableColumn<Desparasitaciones, String>(
            "Tratamiento");

    private JFXTreeTableColumn<Desparasitaciones, String> tipo = new JFXTreeTableColumn<Desparasitaciones, String>(
            "Tratamiento");

    private JFXTreeTableColumn<Desparasitaciones, Date> fecha = new JFXTreeTableColumn<Desparasitaciones, Date>(
            "Fecha");

    private JFXTreeTableColumn<Desparasitaciones, Date> fechaProxima = new JFXTreeTableColumn<Desparasitaciones, Date>(
            "Fecha Próxima");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexD != null : "fx:id=\"indexD\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        Platform.runLater(() -> {
            log.info("creating table");
            tratamiento.setPrefWidth(200);
            tratamiento.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Desparasitaciones, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getTratamiento()));

            tipo.setPrefWidth(200);
            tipo.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Desparasitaciones, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getTipo()));

            fecha.setPrefWidth(150);
            fecha.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<Desparasitaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFecha()));

            fechaProxima.setPrefWidth(150);
            fechaProxima.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<Desparasitaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFechaProxima()));

            log.info("loading table items");

            desparasitaciones.setAll(dao.showByPatient(paciente));
            root = new RecursiveTreeItem<Desparasitaciones>(desparasitaciones, RecursiveTreeObject::getChildren);
            indexD.getColumns().setAll(fecha, tratamiento, tipo, fechaProxima);
            indexD.setShowRoot(false);
            indexD.setRoot(root);

            // Handle ListView selection changes.
            indexD.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    desparasitacion = newValue.getValue();
                    log.info("Item selected.");
                }
            });

            btnRecover.setOnAction((event) -> {
                if (desparasitacion != null) {
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        dao.recover(desparasitacion.getId());
                        TreeItem<Desparasitaciones> selectedItem = indexD.getSelectionModel().getSelectedItem();
                        indexD.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        indexD.refresh();
                        desparasitacion = null;
                        log.info("Item recovered.");
                    }
                } else
                    DialogBox.displayWarning();
            });
        });
        // search filter
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            indexD.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty())
                    return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getValue().getTratamiento().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (item.getValue().getTipo().toLowerCase().contains(lowerCaseFilter))
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
