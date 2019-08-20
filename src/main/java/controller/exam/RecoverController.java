package controller.exam;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.ExamenGeneralHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import model.ExamenGeneral;
import model.Pacientes;
import utils.DialogBox;
import utils.LoadingDialog;
import utils.TableUtil;
import utils.ViewSwitcher;
import utils.routes.RouteExtra;

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
    private TableView<ExamenGeneral> indexE;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<ExamenGeneral, Pacientes> tcPaciente;

    @FXML
    private TableColumn<ExamenGeneral, Date> fecha;

    @FXML
    private TableColumn<ExamenGeneral, String> pesoCorporal;

    @FXML
    private TableColumn<ExamenGeneral, String> tempCorporal;

    @FXML
    private TableColumn<ExamenGeneral, String> frecResp;

    @FXML
    private TableColumn<ExamenGeneral, String> deshidratacion;

    @FXML
    private TableColumn<ExamenGeneral, String> amplitud;

    @FXML
    private TableColumn<ExamenGeneral, String> tipo;

    @FXML
    private TableColumn<ExamenGeneral, String> ritmo;

    @FXML
    private TableColumn<ExamenGeneral, String> frecCardio;

    @FXML
    private TableColumn<ExamenGeneral, String> tllc;

    @FXML
    private TableColumn<ExamenGeneral, String> escleral;

    @FXML
    private TableColumn<ExamenGeneral, String> pulso;

    @FXML
    private TableColumn<ExamenGeneral, String> palperal;

    @FXML
    private TableColumn<ExamenGeneral, String> vulvar;

    @FXML
    private TableColumn<ExamenGeneral, String> peneana;

    @FXML
    private TableColumn<ExamenGeneral, String> submandibular;

    @FXML
    private TableColumn<ExamenGeneral, String> preescapular;

    @FXML
    private TableColumn<ExamenGeneral, String> precrural;

    @FXML
    private TableColumn<ExamenGeneral, String> inguinal;

    @FXML
    private TableColumn<ExamenGeneral, String> otros;

    @FXML
    private TableColumn<ExamenGeneral, String> popliteo;

    @FXML
    private TableColumn<ExamenGeneral, String> bucal;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private ExamenGeneralHome dao = new ExamenGeneralHome();

    private ExamenGeneral examenGeneral;

    final ObservableList<ExamenGeneral> examenList = FXCollections.observableArrayList();

    private FilteredList<ExamenGeneral> filteredData;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexE != null : "fx:id=\"indexE\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

        fecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        pesoCorporal.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getPesoCorporal())));

        tempCorporal.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getTempCorporal())));

        frecResp.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getFrecResp())));

        deshidratacion.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDeshidratacion())));

        amplitud.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getAmplitud()));

        tipo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTipo()));

        ritmo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getRitmo()));

        frecCardio.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getFrecCardio())));

        tllc.setCellValueFactory((param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getTllc())));

        escleral.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getEscleral()));

        pulso.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPulso()));

        palperal.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPalperal()));

        vulvar.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getVulvar()));

        peneana.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPeneana()));

        submandibular.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getSubmandibular()));

        preescapular.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPreescapular()));

        precrural.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPrecrural()));

        inguinal.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getOtros()));

        otros.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getInguinal()));

        popliteo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPopliteo()));

        bucal.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getBucal()));

        tcPaciente
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getPacientes()));

        log.info("loading table items");
        loadDao();

        // Handle ListView selection changes.
        indexE.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                examenGeneral = newValue;
                log.info("Item selected.");
            }
        });

        btnRecover.setOnAction((event) -> {
            if (examenGeneral != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(examenGeneral.getId());
                    ExamenGeneral selectedItem = indexE.getSelectionModel().getSelectedItem();
                    indexE.getItems().remove(selectedItem);
                    indexE.refresh();
                    examenGeneral = null;
                    DialogBox.displaySuccess();
                    log.info("Item recovered.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(examenList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> newValue == null || newValue.isEmpty()
                    || ficha.getPacientes().toString().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getAmplitud().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getBucal().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getEscleral().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getInguinal().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getOtros().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getPopliteo().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getPrecrural().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getPreescapular().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getPulso().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getRitmo().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getSubmandibular().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getTipo().toLowerCase().contains(newValue.toLowerCase()));
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
        int toIndex = Math.min(fromIndex + limit, examenList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<ExamenGeneral> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexE.comparatorProperty());
        indexE.setItems(sortedData);
    }

    private void loadDao() {
        ViewSwitcher vs = new ViewSwitcher();
        LoadingDialog form = vs.loadModal(RouteExtra.LOADING.getPath());
        Task<List<ExamenGeneral>> task = new Task<List<ExamenGeneral>>() {
            @Override
            protected List<ExamenGeneral> call() throws Exception {
                updateMessage("Cargando listado de pacientes eliminados.");
                Thread.sleep(500);
                return dao.displayDeletedRecords();
            }
        };

        form.setStage(vs.getStage());
        form.setProgress(task);

        task.setOnSucceeded(event -> {
            examenList.setAll(task.getValue());
            indexE.setItems(examenList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexE, examenList, tablePagination, index, 20));
            form.getStage().close();
            log.info("Loaded Item.");
        });

        task.setOnFailed(event -> {
            form.getStage().close();
            log.debug("Failed to Query Patient list.");
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}
