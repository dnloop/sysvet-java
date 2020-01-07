package controller.clinicalFile;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.FichasClinicasHome;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.FichasClinicas;
import utils.DialogBox;
import utils.TableUtil;
import utils.viewswitcher.ViewSwitcher;

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
    private TableView<FichasClinicas> indexCF;

    @FXML
    private Pagination tablePagination;

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

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexCF != null : "fx:id=\"indexCF\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcPaciente != null : "fx:id=\"tcPaciente\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcMotivo != null : "fx:id=\"tcMotivo\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcAnamnesis != null : "fx:id=\"tcAnamnesis\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcMedicacion != null : "fx:id=\"tcMedicacion\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcNutricion != null : "fx:id=\"tcNutricion\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcSanitario != null : "fx:id=\"tcSanitario\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcAspecto != null : "fx:id=\"tcAspecto\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcDeterComp != null : "fx:id=\"tcDeterComp\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcDiagnostico != null : "fx:id=\"tcDiagnostico\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcPronostico != null : "fx:id=\"tcPronostico\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcExploracion != null : "fx:id=\"tcExploracion\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcEvolucion != null : "fx:id=\"tcEvolucion\" was not injected: check your FXML file 'recover.fxml'.";
        assert tcDerivaciones != null : "fx:id=\"tcDerivaciones\" was not injected: check your FXML file 'recover.fxml'.";

        tcPaciente.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getPacientes())));

        tcMotivo.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getMotivoConsulta())));

        tcAnamnesis.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getAnamnesis())));

        tcMedicacion.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getMedicacion())));

        tcNutricion.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getEstadoNutricion())));

        tcSanitario.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getEstadoSanitario())));

        tcAspecto.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getAspectoGeneral())));

        tcDeterComp.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDeterDiagComp())));

        tcDerivaciones.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDerivaciones())));

        tcPronostico.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getPronostico())));

        tcDiagnostico.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDiagnostico())));

        tcExploracion.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getExploracion())));

        tcEvolucion.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getEvolucion())));

        log.info("loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexCF.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fichaClinica = newValue;
                log.info("Item selected.");
            }
        });

        btnRecover.setOnAction((event) -> {
            if (fichaClinica != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(fichaClinica.getId());
                    FichasClinicas selectedItem = indexCF.getSelectionModel().getSelectedItem();
                    fichasList.remove(selectedItem);
                    indexCF.setItems(fichasList);
                    indexCF.refresh();
                    fichaClinica = null;
                    DialogBox.displaySuccess();
                    log.info("Item recovered.");
                }
            } else
                DialogBox.displayWarning();
        });

        // search filter
        filteredData = new FilteredList<>(fichasList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(paciente -> newValue == null || newValue.isEmpty()
                    || paciente.getPacientes().toString().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
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

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, fichasList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<FichasClinicas> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCF.comparatorProperty());
        indexCF.setItems(sortedData);
    }

    private void loadDao() {
        Task<List<FichasClinicas>> task = dao.displayDeletedRecords();

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            indexCF.setItems(fichasList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCF, fichasList, tablePagination, index, 20));
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}
