package controller.clinicHistory;

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

import dao.HistoriaClinicaHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.HistoriaClinica;
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
    private JFXTreeTableView<HistoriaClinica> indexCH;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private HistoriaClinicaHome dao = new HistoriaClinicaHome();

    private HistoriaClinica historiaClinica;

    private Pacientes paciente;

    final ObservableList<HistoriaClinica> historiaList = FXCollections.observableArrayList();

    private TreeItem<HistoriaClinica> root;

    // Table Columns

    private JFXTreeTableColumn<HistoriaClinica, Pacientes> pacientes = new JFXTreeTableColumn<HistoriaClinica, Pacientes>(
            "Pacientes - (ficha)");

    private JFXTreeTableColumn<HistoriaClinica, String> descripcionEvento = new JFXTreeTableColumn<HistoriaClinica, String>(
            "Descripción de evento");

    private JFXTreeTableColumn<HistoriaClinica, Date> fechaResolucion = new JFXTreeTableColumn<HistoriaClinica, Date>(
            "Fecha Resolución");

    private JFXTreeTableColumn<HistoriaClinica, String> resultado = new JFXTreeTableColumn<HistoriaClinica, String>(
            "Resultado");

    private JFXTreeTableColumn<HistoriaClinica, String> secuelas = new JFXTreeTableColumn<HistoriaClinica, String>(
            "Secuelas");

    private JFXTreeTableColumn<HistoriaClinica, String> consideraciones = new JFXTreeTableColumn<HistoriaClinica, String>(
            "Considetaciones complementarias");

    private JFXTreeTableColumn<HistoriaClinica, String> comentarios = new JFXTreeTableColumn<HistoriaClinica, String>(
            "Comentarios");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexCH != null : "fx:id=\"indexCH\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

        Platform.runLater(() -> {

            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<HistoriaClinica, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getFichasClinicas().getPacientes()));

            descripcionEvento.setPrefWidth(200);
            descripcionEvento.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getDescripcionEvento())));

            fechaResolucion.setPrefWidth(150);
            fechaResolucion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFechaResolucion()));

            resultado.setPrefWidth(200);
            resultado.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getResultado())));

            secuelas.setPrefWidth(200);
            secuelas.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getSecuelas())));

            consideraciones.setPrefWidth(200);
            consideraciones.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getConsideraciones())));

            comentarios.setPrefWidth(200);
            comentarios.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getComentarios())));

            log.info("loading table items");

            historiaList.setAll(dao.showByPatient(paciente));
            root = new RecursiveTreeItem<HistoriaClinica>(historiaList, RecursiveTreeObject::getChildren);

            indexCH.getColumns().setAll(pacientes, descripcionEvento, fechaResolucion, resultado, secuelas,
                    consideraciones, comentarios);
            indexCH.setShowRoot(false);
            indexCH.setRoot(root);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCH, historiaList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexCH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    historiaClinica = newValue.getValue();
                    log.info("Item selected.");
                }
            });

            btnRecover.setOnAction((event) -> {
                if (historiaClinica != null) {
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        dao.recover(historiaClinica.getId());
                        TreeItem<HistoriaClinica> selectedItem = indexCH.getSelectionModel().getSelectedItem();
                        indexCH.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        indexCH.refresh();
                        historiaClinica = null;
                        DialogBox.displaySuccess();
                        log.info("Item recovered.");
                    }
                } else
                    DialogBox.displayWarning();
            });
            // search filter
            txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                indexCH.setPredicate(item -> {
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
