package controller.clinicalFile;

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

import dao.FichasClinicasHome;
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
import model.FichasClinicas;
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
    private JFXTreeTableView<FichasClinicas> indexCF;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static FichasClinicasHome daoFC = new FichasClinicasHome();

    private FichasClinicas fichaClinica;

    private Integer id;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexCF != null : "fx:id=\"indexCF\" was not injected: check your FXML file 'index.fxml'.";

        JFXTreeTableColumn<FichasClinicas, String> paciente = new JFXTreeTableColumn<FichasClinicas, String>(
                "Paciente");
        paciente.setPrefWidth(200);
        paciente.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getPacientes())));

        JFXTreeTableColumn<FichasClinicas, String> motivoConsulta = new JFXTreeTableColumn<FichasClinicas, String>(
                "Motivo Consulta");
        motivoConsulta.setPrefWidth(200);
        motivoConsulta.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getMotivoConsulta())));

        JFXTreeTableColumn<FichasClinicas, String> anamnesis = new JFXTreeTableColumn<FichasClinicas, String>(
                "Anamnesis");
        anamnesis.setPrefWidth(200);
        anamnesis.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getAnamnesis())));

        JFXTreeTableColumn<FichasClinicas, String> medicacionActual = new JFXTreeTableColumn<FichasClinicas, String>(
                "Medicación Actual");
        medicacionActual.setPrefWidth(200);
        medicacionActual.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getMedicacionActual())));

        JFXTreeTableColumn<FichasClinicas, String> medicacionAnterior = new JFXTreeTableColumn<FichasClinicas, String>(
                "Medicación Anterior");
        medicacionAnterior.setPrefWidth(200);
        medicacionAnterior.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getMedicacionAnterior())));

        JFXTreeTableColumn<FichasClinicas, String> estadoNutricion = new JFXTreeTableColumn<FichasClinicas, String>(
                "Estado Nutrición");
        estadoNutricion.setPrefWidth(200);
        estadoNutricion.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getEstadoNutricion())));

        JFXTreeTableColumn<FichasClinicas, String> estadoSanitario = new JFXTreeTableColumn<FichasClinicas, String>(
                "Estado Sanitario");
        estadoSanitario.setPrefWidth(200);
        estadoSanitario.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getEstadoSanitario())));

        JFXTreeTableColumn<FichasClinicas, String> aspectoGeneral = new JFXTreeTableColumn<FichasClinicas, String>(
                "Aspecto General");
        aspectoGeneral.setPrefWidth(200);
        aspectoGeneral.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getAspectoGeneral())));

        JFXTreeTableColumn<FichasClinicas, String> deterDiagComp = new JFXTreeTableColumn<FichasClinicas, String>(
                "Determinaciones Diagnósticas Complementarias");
        deterDiagComp.setPrefWidth(200);
        deterDiagComp.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getDeterDiagComp())));

        JFXTreeTableColumn<FichasClinicas, String> derivaciones = new JFXTreeTableColumn<FichasClinicas, String>(
                "Derivaciones");
        derivaciones.setPrefWidth(200);
        derivaciones.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getDerivaciones())));

        JFXTreeTableColumn<FichasClinicas, String> pronostico = new JFXTreeTableColumn<FichasClinicas, String>(
                "Pronóstico");
        pronostico.setPrefWidth(200);
        pronostico.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getPronostico())));

        JFXTreeTableColumn<FichasClinicas, String> diagnostico = new JFXTreeTableColumn<FichasClinicas, String>(
                "Diagnóstico");
        diagnostico.setPrefWidth(200);
        diagnostico.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getDiagnostico())));

        JFXTreeTableColumn<FichasClinicas, String> exploracion = new JFXTreeTableColumn<FichasClinicas, String>(
                "Exploración");
        exploracion.setPrefWidth(200);
        exploracion.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getExploracion())));

        JFXTreeTableColumn<FichasClinicas, String> evolucion = new JFXTreeTableColumn<FichasClinicas, String>(
                "Evolución");
        evolucion.setPrefWidth(200);
        evolucion.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        String.valueOf(param.getValue().getValue().getEvolucion())));

        log.info("loading table items");
        ObservableList<FichasClinicas> fichasList = FXCollections.observableArrayList();
        fichasList = loadTable(fichasList);
        TreeItem<FichasClinicas> root = new RecursiveTreeItem<FichasClinicas>(fichasList,
                RecursiveTreeObject::getChildren);

        indexCF.getColumns().setAll(paciente, motivoConsulta, anamnesis, medicacionActual, medicacionAnterior,
                estadoNutricion, estadoSanitario, aspectoGeneral, deterDiagComp, derivaciones, pronostico, exploracion,
                diagnostico, evolucion);
        indexCF.setShowRoot(false);
        indexCF.setRoot(root);

        // Handle ListView selection changes.
        indexCF.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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

    static ObservableList<FichasClinicas> loadTable(ObservableList<FichasClinicas> fichasClinicas) {

        List<FichasClinicas> list = daoFC.displayRecords();
        for (FichasClinicas ficha : list)
            fichasClinicas.add(ficha);

        return fichasClinicas;
    }

    private void confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción.");
        alert.setContentText("¿Desea eliminar el registro?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            daoFC.delete(id);
            indexCF.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexCF.refresh();
            log.info("Item deleted.");
        }
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/clinicalFile/modalDialog.fxml"));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        fichaClinica = daoFC.showById(id);
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(fichaClinica);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Ficha Clinica - " + fichaClinica.getPacientes());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                indexCF.refresh();
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
