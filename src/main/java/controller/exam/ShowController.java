
package controller.exam;

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

import dao.ExamenGeneralHome;
import javafx.application.Platform;
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
import model.ExamenGeneral;
import model.FichasClinicas;
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
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<ExamenGeneral> indexE;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private ExamenGeneralHome dao = new ExamenGeneralHome();

    private ExamenGeneral examenGeneral;

    private Integer id;

    private FichasClinicas fc;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexE != null : "fx:id=\"indexE\" was not injected: check your FXML file 'index.fxml'.";
        Platform.runLater(() -> {
            JFXTreeTableColumn<ExamenGeneral, Pacientes> pacientes = new JFXTreeTableColumn<ExamenGeneral, Pacientes>(
                    "Pacientes - (ficha)");
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<ExamenGeneral, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getFichasClinicas().getPacientes()));

            JFXTreeTableColumn<ExamenGeneral, Date> fecha = new JFXTreeTableColumn<ExamenGeneral, Date>("Fecha");
            fecha.setPrefWidth(150);
            fecha.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFecha()));

            JFXTreeTableColumn<ExamenGeneral, String> pesoCorporal = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Peso Corporal");
            pesoCorporal.setPrefWidth(200);
            pesoCorporal.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getPesoCorporal())));

            JFXTreeTableColumn<ExamenGeneral, String> tempCorporal = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Temp. Corporal");
            tempCorporal.setPrefWidth(200);
            tempCorporal.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getTempCorporal())));

            JFXTreeTableColumn<ExamenGeneral, String> frecResp = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Frec. Respiratoria");
            frecResp.setPrefWidth(200);
            frecResp.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getFrecResp())));

            JFXTreeTableColumn<ExamenGeneral, String> deshidratacion = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Deshidratación");
            deshidratacion.setPrefWidth(200);
            deshidratacion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getDeshidratacion())));

            JFXTreeTableColumn<ExamenGeneral, String> amplitud = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Amplitud");
            amplitud.setPrefWidth(200);
            amplitud.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getAmplitud()));

            JFXTreeTableColumn<ExamenGeneral, String> tipo = new JFXTreeTableColumn<ExamenGeneral, String>("Tipo");
            tipo.setPrefWidth(200);
            tipo.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getTipo()));

            JFXTreeTableColumn<ExamenGeneral, String> ritmo = new JFXTreeTableColumn<ExamenGeneral, String>("Ritmo");
            ritmo.setPrefWidth(200);
            ritmo.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getRitmo()));

            JFXTreeTableColumn<ExamenGeneral, String> frecCardio = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Frec. Cardíaca");
            frecCardio.setPrefWidth(200);
            frecCardio.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getFrecCardio())));

            JFXTreeTableColumn<ExamenGeneral, String> tllc = new JFXTreeTableColumn<ExamenGeneral, String>("T.L.L.C.");
            tllc.setPrefWidth(200);
            tllc.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getTllc())));

            JFXTreeTableColumn<ExamenGeneral, String> escleral = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Escleral");
            escleral.setPrefWidth(200);
            escleral.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getEscleral()));

            JFXTreeTableColumn<ExamenGeneral, String> pulso = new JFXTreeTableColumn<ExamenGeneral, String>("Pulso");
            pulso.setPrefWidth(200);
            pulso.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPulso()));

            JFXTreeTableColumn<ExamenGeneral, String> palperal = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Palperal");
            palperal.setPrefWidth(200);
            palperal.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPalperal()));

            JFXTreeTableColumn<ExamenGeneral, String> vulvar = new JFXTreeTableColumn<ExamenGeneral, String>("Vulvar");
            vulvar.setPrefWidth(200);
            vulvar.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getVulvar()));

            JFXTreeTableColumn<ExamenGeneral, String> peneana = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Peneana");
            peneana.setPrefWidth(200);
            peneana.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPeneana()));

            JFXTreeTableColumn<ExamenGeneral, String> submandibular = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Submandibular");
            submandibular.setPrefWidth(200);
            submandibular.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getSubmandibular()));

            JFXTreeTableColumn<ExamenGeneral, String> preescapular = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Preescapular");
            preescapular.setPrefWidth(200);
            preescapular.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPreescapular()));

            JFXTreeTableColumn<ExamenGeneral, String> precrural = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Precrural");
            precrural.setPrefWidth(200);
            precrural.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPrecrural()));

            JFXTreeTableColumn<ExamenGeneral, String> inguinal = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Inguinal");
            inguinal.setPrefWidth(200);
            inguinal.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getOtros()));

            JFXTreeTableColumn<ExamenGeneral, String> otros = new JFXTreeTableColumn<ExamenGeneral, String>("Inguinal");
            otros.setPrefWidth(200);
            otros.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getInguinal()));

            JFXTreeTableColumn<ExamenGeneral, String> popliteo = new JFXTreeTableColumn<ExamenGeneral, String>(
                    "Popliteo");
            popliteo.setPrefWidth(200);
            popliteo.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPopliteo()));

            JFXTreeTableColumn<ExamenGeneral, String> bucal = new JFXTreeTableColumn<ExamenGeneral, String>("Bucal");
            bucal.setPrefWidth(200);
            bucal.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getBucal()));

            log.info("loading table items");
            ObservableList<ExamenGeneral> examenList = FXCollections.observableArrayList();
            examenList = loadTable(examenList, fc);
            TreeItem<ExamenGeneral> root = new RecursiveTreeItem<ExamenGeneral>(examenList,
                    RecursiveTreeObject::getChildren);

            indexE.getColumns().setAll(
                    // Paciente
                    pacientes, fecha, pesoCorporal, tempCorporal, deshidratacion,
                    // Frecuencia respiratoria
                    frecResp, amplitud, tipo, ritmo,
                    // Frecuencia cardíaca
                    frecCardio, pulso, tllc,
                    // Mucosas
                    bucal, escleral, palperal, vulvar, peneana,
                    // Ganglios
                    submandibular, preescapular, precrural, inguinal, popliteo, otros);
            indexE.setShowRoot(false);
            indexE.setRoot(root);

            // Handle ListView selection changes.
            indexE.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                examenGeneral = newValue.getValue();
                id = examenGeneral.getId();
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

    private ObservableList<ExamenGeneral> loadTable(ObservableList<ExamenGeneral> examenList, FichasClinicas id) {
        List<ExamenGeneral> list = dao.showByFicha(id);
        for (ExamenGeneral item : list)
            examenList.add(item);
        return examenList;
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
            indexE.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexE.refresh();
            log.info("Item deleted.");
        }
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/exam/modalDialog.fxml"));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        examenGeneral = dao.showById(id);
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(examenGeneral);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Examen General");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                indexE.refresh();
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
