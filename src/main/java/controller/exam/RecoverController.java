package controller.exam;

import java.net.URL;
import java.util.Date;
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
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.ExamenGeneral;
import model.Pacientes;
import utils.DialogBox;
import utils.TableUtil;
import utils.ViewSwitcher;

public class RecoverController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnRecover;

    @FXML
    private JFXTreeTableView<ExamenGeneral> indexE;

    @FXML
    private Pagination tablePagination;

    private TreeItem<ExamenGeneral> root;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private ExamenGeneralHome dao = new ExamenGeneralHome();

    private ExamenGeneral examenGeneral;

    final ObservableList<ExamenGeneral> examenList = FXCollections.observableArrayList();

    // Table columns
    private JFXTreeTableColumn<ExamenGeneral, Pacientes> pacientes = new JFXTreeTableColumn<ExamenGeneral, Pacientes>(
            "Pacientes - (ficha)");

    private JFXTreeTableColumn<ExamenGeneral, Date> fecha = new JFXTreeTableColumn<ExamenGeneral, Date>("Fecha");

    private JFXTreeTableColumn<ExamenGeneral, String> pesoCorporal = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Peso Corporal");

    private JFXTreeTableColumn<ExamenGeneral, String> tempCorporal = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Temp. Corporal");

    private JFXTreeTableColumn<ExamenGeneral, String> frecResp = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Frec. Respiratoria");

    private JFXTreeTableColumn<ExamenGeneral, String> deshidratacion = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Deshidratación");

    private JFXTreeTableColumn<ExamenGeneral, String> amplitud = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Amplitud");

    private JFXTreeTableColumn<ExamenGeneral, String> tipo = new JFXTreeTableColumn<ExamenGeneral, String>("Tipo");

    private JFXTreeTableColumn<ExamenGeneral, String> ritmo = new JFXTreeTableColumn<ExamenGeneral, String>("Ritmo");

    private JFXTreeTableColumn<ExamenGeneral, String> frecCardio = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Frec. Cardíaca");

    private JFXTreeTableColumn<ExamenGeneral, String> tllc = new JFXTreeTableColumn<ExamenGeneral, String>("T.L.L.C.");

    private JFXTreeTableColumn<ExamenGeneral, String> escleral = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Escleral");

    private JFXTreeTableColumn<ExamenGeneral, String> pulso = new JFXTreeTableColumn<ExamenGeneral, String>("Pulso");

    private JFXTreeTableColumn<ExamenGeneral, String> palperal = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Palperal");

    private JFXTreeTableColumn<ExamenGeneral, String> vulvar = new JFXTreeTableColumn<ExamenGeneral, String>("Vulvar");

    private JFXTreeTableColumn<ExamenGeneral, String> peneana = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Peneana");

    private JFXTreeTableColumn<ExamenGeneral, String> submandibular = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Submandibular");

    private JFXTreeTableColumn<ExamenGeneral, String> preescapular = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Preescapular");

    private JFXTreeTableColumn<ExamenGeneral, String> precrural = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Precrural");

    private JFXTreeTableColumn<ExamenGeneral, String> inguinal = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Inguinal");

    private JFXTreeTableColumn<ExamenGeneral, String> otros = new JFXTreeTableColumn<ExamenGeneral, String>("Inguinal");

    private JFXTreeTableColumn<ExamenGeneral, String> popliteo = new JFXTreeTableColumn<ExamenGeneral, String>(
            "Popliteo");

    private JFXTreeTableColumn<ExamenGeneral, String> bucal = new JFXTreeTableColumn<ExamenGeneral, String>("Bucal");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexE != null : "fx:id=\"indexE\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        Platform.runLater(() -> {
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<ExamenGeneral, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getPacientes()));

            fecha.setPrefWidth(150);
            fecha.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFecha()));

            pesoCorporal.setPrefWidth(200);
            pesoCorporal.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getPesoCorporal())));

            tempCorporal.setPrefWidth(200);
            tempCorporal.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getTempCorporal())));

            frecResp.setPrefWidth(200);
            frecResp.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getFrecResp())));

            deshidratacion.setPrefWidth(200);
            deshidratacion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getDeshidratacion())));

            amplitud.setPrefWidth(200);
            amplitud.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getAmplitud()));

            tipo.setPrefWidth(200);
            tipo.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getTipo()));

            ritmo.setPrefWidth(200);
            ritmo.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getRitmo()));

            frecCardio.setPrefWidth(200);
            frecCardio.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getFrecCardio())));

            tllc.setPrefWidth(200);
            tllc.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getTllc())));

            escleral.setPrefWidth(200);
            escleral.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getEscleral()));

            pulso.setPrefWidth(200);
            pulso.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPulso()));

            palperal.setPrefWidth(200);
            palperal.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPalperal()));

            vulvar.setPrefWidth(200);
            vulvar.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getVulvar()));

            peneana.setPrefWidth(200);
            peneana.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPeneana()));

            submandibular.setPrefWidth(200);
            submandibular.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getSubmandibular()));

            preescapular.setPrefWidth(200);
            preescapular.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPreescapular()));

            precrural.setPrefWidth(200);
            precrural.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPrecrural()));

            inguinal.setPrefWidth(200);
            inguinal.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getOtros()));

            otros.setPrefWidth(200);
            otros.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getInguinal()));

            popliteo.setPrefWidth(200);
            popliteo.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getPopliteo()));

            bucal.setPrefWidth(200);
            bucal.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<ExamenGeneral, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getBucal()));

            log.info("loading table items");
            examenList.setAll(dao.displayDeletedRecords());
            root = new RecursiveTreeItem<ExamenGeneral>(examenList, RecursiveTreeObject::getChildren);

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
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexE, examenList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexE.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    examenGeneral = newValue.getValue();
                    log.info("Item selected.");
                }
            });

            btnRecover.setOnAction((event) -> {
                if (examenGeneral != null) {
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        dao.recover(examenGeneral.getId());
                        TreeItem<ExamenGeneral> selectedItem = indexE.getSelectionModel().getSelectedItem();
                        indexE.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        indexE.refresh();
                        examenGeneral = null;
                        DialogBox.displaySuccess();
                        log.info("Item recovered.");
                    }
                } else
                    DialogBox.displayWarning();
            });
            // search filter
            txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                indexE.setPredicate(item -> {
                    if (newValue == null || newValue.isEmpty())
                        return true;

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (item.getValue().toString().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    return false;
                });
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
}
