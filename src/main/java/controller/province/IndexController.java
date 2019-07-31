package controller.province;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.ProvinciasHome;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Provincias;
import utils.DialogBox;
import utils.ViewSwitcher;
import utils.routes.Route;

public class IndexController {
//TODO adjust fxml
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
    private TableView<Provincias> indexPR;

    @FXML
    private TableColumn<Provincias, String> nombre;

    private FilteredList<Provincias> filteredData;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private ProvinciasHome dao = new ProvinciasHome();

    private Provincias provincia;

    final ObservableList<Provincias> provinciasList = FXCollections.observableArrayList();

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexPR != null : "fx:id=\"indexPR\" was not injected: check your FXML file 'index.fxml'.";

        nombre.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getNombre()));

        log.info("loading table items");
        provinciasList.setAll(dao.displayRecords());

        indexPR.getColumns().setAll(nombre);
        indexPR.setItems(provinciasList);

        // Handle ListView selection changes.
        indexPR.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                provincia = newValue;
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
                    Provincias selectedItem = indexPR.getSelectionModel().getSelectedItem();
                    indexPR.getItems().remove(selectedItem);
                    indexPR.refresh();
                    provincia = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(provinciasList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(provincia -> newValue == null || newValue.isEmpty()
                    || provincia.getNombre().toLowerCase().contains(newValue.toLowerCase()));

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
            refreshTable();
        });
        mc.setObject(provincia);
        mc.showModal(vs.getStage());
    }

    private void refreshTable() {
        provinciasList.clear();
        provinciasList.setAll(dao.displayRecords());
        indexPR.setItems(provinciasList);
    }

}
