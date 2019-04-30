package controller.internation;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Internaciones;
import model.Pacientes;
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
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<Internaciones> indexI;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static InternacionesHome dao = new InternacionesHome();

    private Internaciones internacion;

    private Integer id;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexI != null : "fx:id=\"indexI\" was not injected: check your FXML file 'index.fxml'.";

        JFXTreeTableColumn<Internaciones, Pacientes> pacientes = new JFXTreeTableColumn<Internaciones, Pacientes>(
                "Pacientes - (ficha)");
        pacientes.setPrefWidth(200);
        pacientes.setCellValueFactory((
                TreeTableColumn.CellDataFeatures<Internaciones, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue().getValue().getFichasClinicas().getPacientes()));

        JFXTreeTableColumn<Internaciones, Date> fechaIngreso = new JFXTreeTableColumn<Internaciones, Date>(
                "Fecha Ingreso");
        fechaIngreso.setPrefWidth(150);
        fechaIngreso.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Internaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                        param.getValue().getValue().getFechaIngreso()));

        JFXTreeTableColumn<Internaciones, Date> fechaAlta = new JFXTreeTableColumn<Internaciones, Date>("Fecha Alta");
        fechaAlta.setPrefWidth(150);
        fechaAlta.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Internaciones, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                        param.getValue().getValue().getFechaAlta()));

        log.info("loading table items");

        ObservableList<Internaciones> fichasClinicas = FXCollections.observableArrayList();
        fichasClinicas = loadTable(fichasClinicas);

        TreeItem<Internaciones> root = new RecursiveTreeItem<Internaciones>(fichasClinicas,
                RecursiveTreeObject::getChildren);

        indexI.getColumns().setAll(pacientes, fechaIngreso, fechaAlta);
        indexI.setShowRoot(false);
        indexI.setRoot(root);

        // Handle ListView selection changes.
        indexI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            id = newValue.getValue().getId();
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
    }

    /**
     *
     * Class Methods
     *
     */

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    static ObservableList<Internaciones> loadTable(ObservableList<Internaciones> internaciones) {

        List<Internaciones> list = dao.displayRecords();
        for (Internaciones item : list)
            internaciones.add(item);
        return internaciones;
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/internation/modalDialog.fxml"));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        internacion = dao.showById(id);
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(internacion);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Internación");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                indexI.refresh();
            });
            sc.showModal(stage);

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
            indexI.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexI.refresh();
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
