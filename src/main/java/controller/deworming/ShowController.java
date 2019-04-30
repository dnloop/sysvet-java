package controller.deworming;

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

import dao.DesparasitacionesHome;
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
import model.Desparasitaciones;
import model.Pacientes;
import utils.ViewSwitcher;

public class ShowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<Desparasitaciones> indexD;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private DesparasitacionesHome dao = new DesparasitacionesHome();

    private Desparasitaciones desparasitacion;

    private Integer id;

    private Pacientes paciente;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexD != null : "fx:id=\"indexD\" was not injected: check your FXML file 'show.fxml'.";

        log.info("creating table");
        JFXTreeTableColumn<Desparasitaciones, Pacientes> pacientes = new JFXTreeTableColumn<Desparasitaciones, Pacientes>(
                "Pacientes");
        pacientes.setPrefWidth(200);
        pacientes.setCellValueFactory((
                TreeTableColumn.CellDataFeatures<Desparasitaciones, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue().getValue().getPacientes()));

        JFXTreeTableColumn<Desparasitaciones, String> tratamiento = new JFXTreeTableColumn<Desparasitaciones, String>(
                "Tratamiento");
        tratamiento.setPrefWidth(200);
        tratamiento.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Desparasitaciones, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getTratamiento()));

        JFXTreeTableColumn<Desparasitaciones, Date> fecha = new JFXTreeTableColumn<Desparasitaciones, Date>("Fecha");
        fecha.setPrefWidth(150);
        fecha.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Desparasitaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                        param.getValue().getValue().getFecha()));

        JFXTreeTableColumn<Desparasitaciones, Date> fechaProxima = new JFXTreeTableColumn<Desparasitaciones, Date>(
                "Fecha Próxima");
        fechaProxima.setPrefWidth(150);
        fechaProxima.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Desparasitaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                        param.getValue().getValue().getFechaProxima()));

        log.info("loading table items");

        ObservableList<Desparasitaciones> desparasitaciones = FXCollections.observableArrayList();
        desparasitaciones = loadTable(desparasitaciones);

        TreeItem<Desparasitaciones> root = new RecursiveTreeItem<Desparasitaciones>(desparasitaciones,
                RecursiveTreeObject::getChildren);
        indexD.getColumns().setAll(fecha, pacientes, tratamiento, fechaProxima);
        indexD.setShowRoot(false);
        indexD.setRoot(root);

        // Handle ListView selection changes.
        indexD.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            desparasitacion = newValue.getValue();
            id = desparasitacion.getId();
            log.info("Item selected.");
        });

        btnEdit.setOnAction((event) -> {
            if (id != null)
                displayModal(event);
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

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    } // Pacientes

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private ObservableList<Desparasitaciones> loadTable(ObservableList<Desparasitaciones> cuentasList) {
        List<Desparasitaciones> list = dao.showByPatient(paciente);
        for (Desparasitaciones item : list)
            cuentasList.add(item);
        return cuentasList;
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

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/deworming/modalDialog.fxml"));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        desparasitacion = dao.showById(id);
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(desparasitacion);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            /*
             * This could be used to display patient name in the title in order to be more
             * descriptive.
             */
            stage.setTitle("Examen General");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                indexD.refresh();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
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