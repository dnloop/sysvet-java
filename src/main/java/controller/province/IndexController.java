package controller.province;

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

import dao.ProvinciasHome;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Provincias;
import utils.DialogBox;
import utils.ViewSwitcher;
import utils.routes.Route;

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
    private JFXTreeTableView<Provincias> indexPR;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private ProvinciasHome dao = new ProvinciasHome();

    private Provincias provincia;

    final ObservableList<Provincias> provincias = FXCollections.observableArrayList();

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexPR != null : "fx:id=\"indexPR\" was not injected: check your FXML file 'index.fxml'.";

        JFXTreeTableColumn<Provincias, String> nombre = new JFXTreeTableColumn<Provincias, String>("Nombre");
        nombre.setPrefWidth(200);
        nombre.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Provincias, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getNombre()));

        log.info("loading table items");
        provincias.setAll(dao.displayRecords());

        TreeItem<Provincias> root = new RecursiveTreeItem<Provincias>(provincias, RecursiveTreeObject::getChildren);
        indexPR.getColumns().setAll(nombre);
        indexPR.setShowRoot(false);
        indexPR.setRoot(root);

        // Handle ListView selection changes.
        indexPR.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                provincia = newValue.getValue();
                log.info("Item selected.");
            }
        });

        btnEdit.setOnAction((event) -> {
            if (provincia != null)
                displayEdit(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (provincia != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(provincia.getId());
                    TreeItem<Provincias> selectedItem = indexPR.getSelectionModel().getSelectedItem();
                    indexPR.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    indexPR.refresh();
                    provincia = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            indexPR.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty())
                    return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getValue().getNombre().toLowerCase().contains(lowerCaseFilter))
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
        ViewSwitcher vs = new ViewSwitcher();
        ModalDialogController mc = vs.loadModal(Route.PROVINCIA.modalView(), "Provincia", event);
        vs.getStage().setOnHidden((stageEvent) -> {
            indexPR.refresh();
        });
        mc.setObject(provincia);
        mc.showModal(vs.getStage());
    }

}
