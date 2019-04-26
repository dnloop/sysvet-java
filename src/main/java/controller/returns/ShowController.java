package controller.returns;

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

import dao.RetornosHome;
import javafx.application.Platform;
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
import model.FichasClinicas;
import model.Pacientes;
import model.Retornos;
import utils.ViewSwitcher;

public class ShowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<Retornos> indexRT;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private RetornosHome dao = new RetornosHome();

    private Retornos retorno;

    private Integer id;

    private FichasClinicas fc;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexRT != null : "fx:id=\"indexRT\" was not injected: check your FXML file 'show.fxml'.";
        Platform.runLater(() -> {
            JFXTreeTableColumn<Retornos, Pacientes> pacientes = new JFXTreeTableColumn<Retornos, Pacientes>(
                    "Pacientes - (ficha)");
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<Retornos, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getFichasClinicas().getPacientes()));

            JFXTreeTableColumn<Retornos, Date> fecha = new JFXTreeTableColumn<Retornos, Date>("Fecha");
            fecha.setPrefWidth(150);
            fecha.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Retornos, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFecha()));

            log.info("loading table items");
            ObservableList<Retornos> returnsList = FXCollections.observableArrayList();
            returnsList = loadTable(returnsList, fc);
            TreeItem<Retornos> root = new RecursiveTreeItem<Retornos>(returnsList, RecursiveTreeObject::getChildren);

            indexRT.getColumns().setAll(pacientes, fecha);
            indexRT.setShowRoot(false);
            indexRT.setRoot(root);

            // Handle ListView selection changes.
            indexRT.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                retorno = newValue.getValue();
                id = retorno.getId();
                log.info("Item selected.");
            });

            btnShow.setOnAction((event) -> {
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
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setFC(FichasClinicas fc) {
        this.fc = fc;
    } // FichasClinicas

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private ObservableList<Retornos> loadTable(ObservableList<Retornos> returnsList, FichasClinicas id) {
        List<Retornos> list = dao.showByFicha(id);
        for (Retornos item : list)
            returnsList.add(item);
        return returnsList;
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
            indexRT.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexRT.refresh();
            log.info("Item deleted.");
        }
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/returns/modalDialog.fxml"));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        retorno = dao.showById(id);
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(retorno);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Retorno");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                indexRT.refresh();
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
