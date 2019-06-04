package controller.internation;

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
import dao.InternacionesHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
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
    private JFXTreeTableView<Pacientes> indexI;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private InternacionesHome dao = new InternacionesHome();

    private Integer id;

    final ObservableList<Pacientes> internaciones = FXCollections.observableArrayList();

    private TreeItem<Pacientes> root;

    private Pacientes paciente;

    // Table column

    private JFXTreeTableColumn<Pacientes, Pacientes> pacientes = new JFXTreeTableColumn<Pacientes, Pacientes>(
            "Pacientes - (ficha)");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexI != null : "fx:id=\"indexI\" was not injected: check your FXML file 'index.fxml'.";

        pacientes.setPrefWidth(200);
        pacientes.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue().getValue()));

        log.info("loading table items");
        internaciones.setAll(daoFC.displayRecordsWithInternations());

        root = new RecursiveTreeItem<Pacientes>(internaciones, RecursiveTreeObject::getChildren);

        indexI.getColumns().setAll(pacientes);
        indexI.setShowRoot(false);
        indexI.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexI, internaciones, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                paciente = newValue.getValue();
                log.info("Item selected.");
            }
        });

        btnNew.setOnAction((event) -> displayNew(event));

        btnShow.setOnAction((event) -> {
            if (paciente != null)
                displayShow(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (paciente != null)
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(paciente.getId());
                    TreeItem<Pacientes> selectedItem = indexI.getSelectionModel().getSelectedItem();
                    indexI.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    refreshTable();
                    log.info("Item deleted.");
                }
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.INTERNACION.showView()));
        try {
            Node node = fxmlLoader.load();
            ShowController sc = fxmlLoader.getController();
            sc.setObject(paciente);
            setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayNew(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.INTERNACION.newView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            NewController sc = fxmlLoader.getController();
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Nuevo elemento - Internaciones");
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

    private void refreshTable() {
        internaciones.clear();
        internaciones.setAll(daoFC.displayRecordsWithInternations());
        root = new RecursiveTreeItem<Pacientes>(internaciones, RecursiveTreeObject::getChildren);
        indexI.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexI, internaciones, tablePagination, index, 20));
    }
}
