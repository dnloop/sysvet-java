package controller.clinicalFile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.FichasClinicasHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.FichasClinicas;
import model.Pacientes;
import utils.DialogBox;
import utils.Route;
import utils.TableUtil;
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
    private TableView<FichasClinicas> indexCF;

    @FXML
    private Pagination tablePagination;

    // Table columns
    @FXML
    private TableColumn<FichasClinicas, String> tcPaciente;

    @FXML
    private TableColumn<FichasClinicas, String> tcMotivo;

    @FXML
    private TableColumn<FichasClinicas, String> tcAnamnesis;

    @FXML
    private TableColumn<FichasClinicas, String> tcMedicacion;

    @FXML
    private TableColumn<FichasClinicas, String> tcNutricion;

    @FXML
    private TableColumn<FichasClinicas, String> tcSanitario;

    @FXML
    private TableColumn<FichasClinicas, String> tcAspecto;

    @FXML
    private TableColumn<FichasClinicas, String> tcDeterComp;

    @FXML
    private TableColumn<FichasClinicas, String> tcDerivaciones;

    @FXML
    private TableColumn<FichasClinicas, String> tcPronostico;

    @FXML
    private TableColumn<FichasClinicas, String> tcDiagnostico;

    @FXML
    private TableColumn<FichasClinicas, String> tcExploracion;

    @FXML
    private TableColumn<FichasClinicas, String> tcEvolucion;

    private final ObservableList<FichasClinicas> fichasList = FXCollections.observableArrayList();

    private FilteredList<FichasClinicas> filteredData;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private FichasClinicasHome dao = new FichasClinicasHome();

    private FichasClinicas fichaClinica;

    private Pacientes pac;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'show.fxml'.";
        assert indexCF != null : "fx:id=\"indexCF\" was not injected: check your FXML file 'show.fxml'.";
        assert tcPaciente != null : "fx:id=\"tcPaciente\" was not injected: check your FXML file 'show.fxml'.";
        assert tcMotivo != null : "fx:id=\"tcMotivo\" was not injected: check your FXML file 'show.fxml'.";
        assert tcAnamnesis != null : "fx:id=\"tcAnamnesis\" was not injected: check your FXML file 'show.fxml'.";
        assert tcMedicacion != null : "fx:id=\"tcMedicacion\" was not injected: check your FXML file 'show.fxml'.";
        assert tcNutricion != null : "fx:id=\"tcNutricion\" was not injected: check your FXML file 'show.fxml'.";
        assert tcSanitario != null : "fx:id=\"tcSanitario\" was not injected: check your FXML file 'show.fxml'.";
        assert tcAspecto != null : "fx:id=\"tcAspecto\" was not injected: check your FXML file 'show.fxml'.";
        assert tcDeterComp != null : "fx:id=\"tcDeterComp\" was not injected: check your FXML file 'show.fxml'.";
        assert tcDiagnostico != null : "fx:id=\"tcDiagnostico\" was not injected: check your FXML file 'show.fxml'.";
        assert tcPronostico != null : "fx:id=\"tcPronostico\" was not injected: check your FXML file 'show.fxml'.";
        assert tcExploracion != null : "fx:id=\"tcExploracion\" was not injected: check your FXML file 'show.fxml'.";
        assert tcEvolucion != null : "fx:id=\"tcEvolucion\" was not injected: check your FXML file 'show.fxml'.";
        assert tcDerivaciones != null : "fx:id=\"tcDerivaciones\" was not injected: check your FXML file 'show.fxml'.";

        Platform.runLater(() -> {

            tcPaciente.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getPacientes())));

            tcMotivo.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getMotivoConsulta())));

            tcAnamnesis.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getAnamnesis())));

            tcMedicacion.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getMedicacion())));

            tcNutricion.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getEstadoNutricion())));

            tcSanitario.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getEstadoSanitario())));

            tcAspecto.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getAspectoGeneral())));

            tcDeterComp.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getDeterDiagComp())));

            tcDerivaciones.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getDerivaciones())));

            tcPronostico.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getPronostico())));

            tcDiagnostico.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getDiagnostico())));

            tcExploracion.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getExploracion())));

            tcEvolucion.setCellValueFactory(
                    (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                            String.valueOf(param.getValue().getEvolucion())));

            log.info("loading table items");
            fichasList.addAll(dao.showByPatient(pac));

            indexCF.getColumns().setAll(tcPaciente, tcMotivo, tcAnamnesis, tcMedicacion, tcNutricion, tcSanitario,
                    tcAspecto, tcDeterComp, tcDerivaciones, tcPronostico, tcExploracion, tcDiagnostico, tcEvolucion);

            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCF, fichasList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexCF.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    fichaClinica = newValue;
                    log.info("Item selected.");
                }
            });

            btnEdit.setOnAction((event) -> {
                if (fichaClinica != null)
                    displayModal(event);
                else
                    DialogBox.displayWarning();
            });

            btnDelete.setOnAction((event) -> {
                if (fichaClinica != null) {
                    if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                        dao.delete(fichaClinica.getId());
                        FichasClinicas selectedItem = indexCF.getSelectionModel().getSelectedItem();
                        indexCF.getItems().remove(selectedItem);
                        refreshTable();
                        fichaClinica = null;
                        DialogBox.displaySuccess();
                        log.info("Item deleted.");
                    }
                } else
                    DialogBox.displayWarning();
            });
        });

        // search filter
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty())
                    return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if (item.toString().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (item.getMotivoConsulta().toLowerCase().contains(lowerCaseFilter))
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

    public void setObject(Pacientes pac) {
        this.pac = pac;
    } // Pacientes

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.FICHACLINICA.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(fichaClinica);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Ficha Clínica");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHiding((stageEvent) -> {
                refreshTable();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        fichasList.clear();
        fichasList.setAll(dao.showByPatient(pac));
        indexCF.setItems(fichasList);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexCF, fichasList, tablePagination, index, 20));
    }
}
