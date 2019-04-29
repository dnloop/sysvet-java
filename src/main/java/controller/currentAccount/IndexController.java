package controller.currentAccount;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
import dao.PropietariosHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Propietarios;
import model.Record;
import utils.ViewSwitcher;

public class IndexController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXTreeTableView<Record<Propietarios>> indexCA;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private static CuentasCorrientesHome dao = new CuentasCorrientesHome();

    private static PropietariosHome daoPO = new PropietariosHome();

    private Propietarios propietario;

    private Integer id;

    Parent root;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert indexCA != null : "fx:id=\"indexPatient\" was not injected: check your FXML file 'index.fxml'.";

        // this should be a helper class to load everything
        log.info("creating table");

        JFXTreeTableColumn<Record<Propietarios>, Propietarios> propietarios = new JFXTreeTableColumn<Record<Propietarios>, Propietarios>(
                "Propietarios");
        propietarios.setPrefWidth(200);
        propietarios.setCellValueFactory((
                TreeTableColumn.CellDataFeatures<Record<Propietarios>, Propietarios> param) -> new ReadOnlyObjectWrapper<Propietarios>(
                        param.getValue().getValue().getRecord()));

        log.info("loading table items");

        ObservableList<Record<Propietarios>> cuentasCorrientes = FXCollections.observableArrayList();
        cuentasCorrientes = loadTable(cuentasCorrientes);

        TreeItem<Record<Propietarios>> root = new RecursiveTreeItem<Record<Propietarios>>(cuentasCorrientes,
                RecursiveTreeObject::getChildren);
        indexCA.getColumns().setAll(propietarios);
        indexCA.setShowRoot(false);
        indexCA.setRoot(root);

        // Handle ListView selection changes.
        indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            propietario = newValue.getValue().getRecord();
            id = propietario.getId();
            log.info("Item selected.");
        });

        btnShow.setOnAction((event) -> {
            if (id != null)
                displayShow(event);
            else
                displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (id != null)
                confirmDialog();
            else
                displayWarning();
        });
        // TODO add search filter
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

    static ObservableList<Record<Propietarios>> loadTable(ObservableList<Record<Propietarios>> cuentasCorrientes) {

        List<Object> list = dao.displayRecordsWithOwners();
        for (Object object : list) {
            Object[] result = (Object[]) object;
            Record<Propietarios> ficha = new Record<Propietarios>();
            ficha.setId((Integer) result[0]);
            ficha.setRecord((Propietarios) result[1]);
            cuentasCorrientes.add(ficha);
        }

        return cuentasCorrientes;
    }

    private void displayShow(Event event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/currentAccount/show.fxml"));
        Propietarios propietario = daoPO.showById(id);
        try {
            Node node = fxmlLoader.load();
            ShowController sc = fxmlLoader.getController();
            sc.setObject(propietario);
            setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción.");
        alert.setContentText("¿Desea eliminar el registro?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.delete(id);
            indexCA.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexCA.refresh();
            log.info("Item deleted.");
        }
    }

    private void displayWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia.");
        alert.setHeaderText("Elemento vacío.");
        alert.setContentText("No se seleccionó ningún elemento de la lista. Elija un ítem e intente nuevamente.");
        alert.setResizable(true);

        alert.showAndWait();
    }
}
