package controller.clinicalFile;

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
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.FichasClinicas;
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
    private JFXTreeTableView<FichasClinicas> indexCF;

    @FXML
    private Pagination tablePagination;

    private TreeItem<FichasClinicas> root;

    private final ObservableList<FichasClinicas> fichasList = FXCollections.observableArrayList();

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private FichasClinicasHome dao = new FichasClinicasHome();

    private FichasClinicas fichaClinica;

    // Table columns

    private JFXTreeTableColumn<FichasClinicas, String> paciente = new JFXTreeTableColumn<FichasClinicas, String>(
            "Paciente");

    private JFXTreeTableColumn<FichasClinicas, String> motivoConsulta = new JFXTreeTableColumn<FichasClinicas, String>(
            "Motivo Consulta");

    private JFXTreeTableColumn<FichasClinicas, String> anamnesis = new JFXTreeTableColumn<FichasClinicas, String>(
            "Anamnesis");

    private JFXTreeTableColumn<FichasClinicas, String> medicacionActual = new JFXTreeTableColumn<FichasClinicas, String>(
            "Medicación Actual");

    private JFXTreeTableColumn<FichasClinicas, String> medicacionAnterior = new JFXTreeTableColumn<FichasClinicas, String>(
            "Medicación Anterior");

    private JFXTreeTableColumn<FichasClinicas, String> estadoNutricion = new JFXTreeTableColumn<FichasClinicas, String>(
            "Estado Nutrición");

    private JFXTreeTableColumn<FichasClinicas, String> estadoSanitario = new JFXTreeTableColumn<FichasClinicas, String>(
            "Estado Sanitario");

    private JFXTreeTableColumn<FichasClinicas, String> aspectoGeneral = new JFXTreeTableColumn<FichasClinicas, String>(
            "Aspecto General");

    private JFXTreeTableColumn<FichasClinicas, String> deterDiagComp = new JFXTreeTableColumn<FichasClinicas, String>(
            "Determinaciones Diagnósticas Complementarias");

    private JFXTreeTableColumn<FichasClinicas, String> derivaciones = new JFXTreeTableColumn<FichasClinicas, String>(
            "Derivaciones");

    private JFXTreeTableColumn<FichasClinicas, String> pronostico = new JFXTreeTableColumn<FichasClinicas, String>(
            "Pronóstico");

    private JFXTreeTableColumn<FichasClinicas, String> diagnostico = new JFXTreeTableColumn<FichasClinicas, String>(
            "Diagnóstico");

    private JFXTreeTableColumn<FichasClinicas, String> exploracion = new JFXTreeTableColumn<FichasClinicas, String>(
            "Exploración");

    private JFXTreeTableColumn<FichasClinicas, String> evolucion = new JFXTreeTableColumn<FichasClinicas, String>(
            "Evolución");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexCF != null : "fx:id=\"indexCF\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        Platform.runLater(() -> {

            paciente.setPrefWidth(200);
            paciente.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getPacientes())));

            motivoConsulta.setPrefWidth(200);
            motivoConsulta.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getMotivoConsulta())));

            anamnesis.setPrefWidth(200);
            anamnesis.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getAnamnesis())));

            medicacionActual.setPrefWidth(200);
            medicacionActual.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getMedicacionActual())));

            medicacionAnterior.setPrefWidth(200);
            medicacionAnterior.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getMedicacionAnterior())));

            estadoNutricion.setPrefWidth(200);
            estadoNutricion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getEstadoNutricion())));

            estadoSanitario.setPrefWidth(200);
            estadoSanitario.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getEstadoSanitario())));

            aspectoGeneral.setPrefWidth(200);
            aspectoGeneral.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getAspectoGeneral())));

            deterDiagComp.setPrefWidth(200);
            deterDiagComp.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getDeterDiagComp())));

            derivaciones.setPrefWidth(200);
            derivaciones.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getDerivaciones())));

            pronostico.setPrefWidth(200);
            pronostico.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getPronostico())));

            diagnostico.setPrefWidth(200);
            diagnostico.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getDiagnostico())));

            exploracion.setPrefWidth(200);
            exploracion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getExploracion())));

            evolucion.setPrefWidth(200);
            evolucion.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getValue().getEvolucion())));

            log.info("loading table items");
            fichasList.addAll(dao.displayDeletedRecords());

            root = new RecursiveTreeItem<FichasClinicas>(fichasList, RecursiveTreeObject::getChildren);

            indexCF.getColumns().setAll(paciente, motivoConsulta, anamnesis, medicacionActual, medicacionAnterior,
                    estadoNutricion, estadoSanitario, aspectoGeneral, deterDiagComp, derivaciones, pronostico,
                    exploracion, diagnostico, evolucion);
            indexCF.setShowRoot(false);
            indexCF.setRoot(root);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCF, fichasList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexCF.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    fichaClinica = newValue.getValue();
                    log.info("Item selected.");
                }
            });

            btnRecover.setOnAction((event) -> {
                if (fichaClinica != null) {
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        dao.recover(fichaClinica.getId());
                        TreeItem<FichasClinicas> selectedItem = indexCF.getSelectionModel().getSelectedItem();
                        indexCF.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        indexCF.refresh();
                        fichaClinica = null;
                        log.info("Item recovered.");
                    }
                } else
                    DialogBox.displayWarning();
            });
        });

        // search filter
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            indexCF.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty())
                    return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getValue().toString().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (item.getValue().getMotivoConsulta().toLowerCase().contains(lowerCaseFilter))
                    return true;
                return false;
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

    private void refreshTable() {
        fichasList.clear();
        fichasList.setAll(dao.displayDeletedRecords());
        root = new RecursiveTreeItem<FichasClinicas>(fichasList, RecursiveTreeObject::getChildren);
        indexCF.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexCF, fichasList, tablePagination, index, 20));
    }
}
