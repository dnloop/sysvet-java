package controller.returns;

import java.io.IOException;
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

import dao.FichasClinicasHome;
import dao.RetornosHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Pacientes;
import utils.DialogBox;
import utils.Route;
import utils.TableUtil;
import utils.ViewSwitcher;

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
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private Pagination tablePagination;

    @FXML
    private JFXTreeTableView<Pacientes> indexRT;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private RetornosHome daoRT = new RetornosHome();

    private Pacientes paciente;

    final ObservableList<Pacientes> fichasClinicas = FXCollections.observableArrayList();

    private TreeItem<Pacientes> root;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexRT != null : "fx:id=\"indexRT\" was not injected: check your FXML file 'index.fxml'.";

        JFXTreeTableColumn<Pacientes, Pacientes> pacientes = new JFXTreeTableColumn<Pacientes, Pacientes>(
                "Pacientes - (ficha)");
        pacientes.setPrefWidth(200);
        pacientes.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue().getValue()));

        log.info("loading table items");

        fichasClinicas.setAll(daoFC.displayRecordsWithReturns());

        root = new RecursiveTreeItem<Pacientes>(fichasClinicas, RecursiveTreeObject::getChildren);

        indexRT.getColumns().setAll(pacientes);
        indexRT.setShowRoot(false);
        indexRT.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexRT, fichasClinicas, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexRT.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                paciente = newValue.getValue();
                log.info("Item selected.");
            }
        });

        btnShow.setOnAction((event) -> {
            if (paciente != null)
                displayShow(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (paciente != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    daoRT.deleteAll(paciente.getId());
                    TreeItem<Pacientes> selectedItem = indexRT.getSelectionModel().getSelectedItem();
                    indexRT.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    refreshTable();
                    paciente = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            indexRT.setPredicate(item -> {
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

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void setView(Node node) {
        ViewSwitcher.loadNode(node);
    }

    private void displayShow(Event event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.RETORNO.showView()));
        try {
            Node node = fxmlLoader.load();
            ShowController sc = fxmlLoader.getController();
            sc.setObject(paciente);
            setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        fichasClinicas.clear();
        fichasClinicas.setAll(daoFC.displayRecordsWithReturns());
        root = new RecursiveTreeItem<Pacientes>(fichasClinicas, RecursiveTreeObject::getChildren);
        indexRT.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexRT, fichasClinicas, tablePagination, index, 20));
    }
}