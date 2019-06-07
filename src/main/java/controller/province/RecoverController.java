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
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Provincias;
import utils.DialogBox;

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
    private JFXTreeTableView<Provincias> indexPR;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private ProvinciasHome dao = new ProvinciasHome();

    private Provincias provincia;

    final ObservableList<Provincias> provincias = FXCollections.observableArrayList();

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexPR != null : "fx:id=\"indexPR\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

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

        btnRecover.setOnAction((event) -> {
            if (provincia != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(provincia.getId());
                    TreeItem<Provincias> selectedItem = indexPR.getSelectionModel().getSelectedItem();
                    indexPR.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    indexPR.refresh();
                    log.info("Item recovered.");
                    provincia = null;
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
}
