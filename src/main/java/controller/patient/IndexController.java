package controller.patient;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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

import dao.PacientesHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Pacientes;

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
    private JFXTreeTableView<Pacientes> indexPA;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private static PacientesHome dao = new PacientesHome();

    private Pacientes paciente;

    private Integer id;

    Parent root;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexPA != null : "fx:id=\"indexPA\" was not injected: check your FXML file 'index.fxml'.";

        log.info("creating table");

        JFXTreeTableColumn<Pacientes, String> nombre = new JFXTreeTableColumn<Pacientes, String>("Nombre");
        nombre.setPrefWidth(200);
        nombre.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getNombre()));

        JFXTreeTableColumn<Pacientes, String> especie = new JFXTreeTableColumn<Pacientes, String>("Especie");
        especie.setPrefWidth(200);
        especie.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getEspecie()));

        JFXTreeTableColumn<Pacientes, String> raza = new JFXTreeTableColumn<Pacientes, String>("Raza");
        raza.setPrefWidth(200);
        raza.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getRaza()));

        JFXTreeTableColumn<Pacientes, String> sexo = new JFXTreeTableColumn<Pacientes, String>("Sexo");
        sexo.setPrefWidth(200);
        sexo.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getSexo()));

        JFXTreeTableColumn<Pacientes, String> temp = new JFXTreeTableColumn<Pacientes, String>("Temperamento");
        temp.setPrefWidth(200);
        temp.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getTemperamento()));

        JFXTreeTableColumn<Pacientes, String> pelaje = new JFXTreeTableColumn<Pacientes, String>("Pelaje");
        pelaje.setPrefWidth(200);
        pelaje.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getPelaje()));

        JFXTreeTableColumn<Pacientes, String> peso = new JFXTreeTableColumn<Pacientes, String>("Peso");
        peso.setPrefWidth(200);
        peso.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getPeso()));

        JFXTreeTableColumn<Pacientes, Date> fecha = new JFXTreeTableColumn<Pacientes, Date>("Fecha");
        fecha.setPrefWidth(150);
        fecha.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                        param.getValue().getValue().getFechaNacimiento()));
        log.info("loading table items");

        ObservableList<Pacientes> cuentasCorrientes = FXCollections.observableArrayList();
        cuentasCorrientes = loadTable(cuentasCorrientes);

        TreeItem<Pacientes> root = new RecursiveTreeItem<Pacientes>(cuentasCorrientes,
                RecursiveTreeObject::getChildren);
        indexPA.getColumns().setAll(nombre, especie, raza, sexo, pelaje, peso, fecha);
        indexPA.setShowRoot(false);
        indexPA.setRoot(root);

        // Handle ListView selection changes.
        indexPA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            paciente = newValue.getValue();
            id = paciente.getId();
            log.info("Item selected.");
        });

        btnEdit.setOnAction((event) -> {
            if (id != null)
                displayEdit(event);
            else
                displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (id != null)
                confirmDialog();
            else
                displayWarning();
        });

    }

    /**
     *
     * Class Methods
     *
     */

    private void displayEdit(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/patient/modalDialog.fxml"));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController mdc = fxmlLoader.getController();
            mdc.setObject(paciente);
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Cuenta Corriente");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                indexPA.refresh();
            });
            mdc.showModal(stage);

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
            indexPA.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexPA.refresh();
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

    static ObservableList<Pacientes> loadTable(ObservableList<Pacientes> cuentasCorrientes) {
        List<Pacientes> list = dao.displayRecords();
        for (Pacientes item : list)
            cuentasCorrientes.add(item);
        return cuentasCorrientes;
    }
}
