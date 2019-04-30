package controller.deworming;

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

import dao.DesparasitacionesHome;
import dao.PacientesHome;
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
import model.Pacientes;
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
    private JFXButton btnNew;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<Record<Pacientes>> indexD;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private static DesparasitacionesHome dao = new DesparasitacionesHome();

    private static PacientesHome daoPO = new PacientesHome();

    private Pacientes paciente;

    private Integer id;

    Parent root;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexD != null : "fx:id=\"indexD\" was not injected: check your FXML file 'index.fxml'.";

        log.info("creating table");

        JFXTreeTableColumn<Record<Pacientes>, Pacientes> propietarios = new JFXTreeTableColumn<Record<Pacientes>, Pacientes>(
                "Pacientes");
        propietarios.setPrefWidth(200);
        propietarios.setCellValueFactory((
                TreeTableColumn.CellDataFeatures<Record<Pacientes>, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue().getValue().getRecord()));

        log.info("loading table items");

        ObservableList<Record<Pacientes>> desparasitaciones = FXCollections.observableArrayList();
        desparasitaciones = loadTable(desparasitaciones);

        TreeItem<Record<Pacientes>> root = new RecursiveTreeItem<Record<Pacientes>>(desparasitaciones,
                RecursiveTreeObject::getChildren);
        indexD.getColumns().setAll(propietarios);
        indexD.setShowRoot(false);
        indexD.setRoot(root);

        // Handle ListView selection changes.
        indexD.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            paciente = newValue.getValue().getRecord();
            id = paciente.getId();
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

    static ObservableList<Record<Pacientes>> loadTable(ObservableList<Record<Pacientes>> cuentasCorrientes) {

        List<Object> list = dao.displayRecordsWithPatients();
        for (Object object : list) {
            Object[] result = (Object[]) object;
            Record<Pacientes> ficha = new Record<Pacientes>();
            ficha.setId((Integer) result[0]);
            ficha.setRecord((Pacientes) result[1]);
            cuentasCorrientes.add(ficha);
        }

        return cuentasCorrientes;
    }

    private void displayShow(Event event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/deworming/show.fxml"));
        Pacientes paciente = daoPO.showById(id);
        try {
            Node node = fxmlLoader.load();
            ShowController sc = fxmlLoader.getController();
            sc.setObject(paciente);
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
            indexD.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexD.refresh();
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
