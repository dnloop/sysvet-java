package controller.clinicalFile;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import dao.FichasClinicasHome;
import dao.HistoriaClinicaHome;
import dao.TratamientosHome;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import model.FichasClinicas;
import model.HistoriaClinica;
import model.Pacientes;
import model.Tratamientos;
import utils.routes.Route;
import utils.viewswitcher.ViewSwitcher;

public class OverviewController extends ViewSwitcher {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker dpDesde;

    @FXML
    private DatePicker dpHasta;

    @FXML
    private TableView<FichasClinicas> tvFicha;

    @FXML
    private TableView<Tratamientos> tvTratamiento;

    @FXML
    private TableView<HistoriaClinica> tvHistoria;

    @FXML
    private TableColumn<FichasClinicas, String> tcMotivo;

    @FXML
    private AnchorPane apContent;

    @FXML
    private TableColumn<Tratamientos, String> tcIdTratamiento;

    @FXML
    private TableColumn<Tratamientos, String> tcTratamiento;

    @FXML
    private TableColumn<HistoriaClinica, String> tcIdDescripcion;

    @FXML
    private TableColumn<HistoriaClinica, String> tcDescripcion;

    protected static final Logger log = (Logger) LogManager.getLogger(ViewController.class);

    private Pacientes paciente;

    private FichasClinicas ficha;

    private HistoriaClinica historia;

    private Tratamientos tratamiento;

    private Date start;

    private Date end;

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private TratamientosHome daoTR = new TratamientosHome();

    private HistoriaClinicaHome daoHC = new HistoriaClinicaHome();

    final ObservableList<FichasClinicas> fichasList = FXCollections.observableArrayList();

    final ObservableList<Tratamientos> tratamientoList = FXCollections.observableArrayList();

    final ObservableList<HistoriaClinica> historiaList = FXCollections.observableArrayList();

    private controller.clinicalFile.ViewController fichaController;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert dpDesde != null : "fx:id=\"dpDesde\" was not injected: check your FXML file 'view.fxml'.";
        assert dpHasta != null : "fx:id=\"dpHasta\" was not injected: check your FXML file 'view.fxml'.";
        assert tvFicha != null : "fx:id=\"tvFicha\" was not injected: check your FXML file 'view.fxml'.";
        assert apContent != null : "fx:id=\"apContent\" was not injected: check your FXML file 'view.fxml'.";
        assert tvTratamiento != null : "fx:id=\"tvTratamiento\" was not injected: check your FXML file 'view.fxml'.";
        assert tvHistoria != null : "fx:id=\"tvHistoria\" was not injected: check your FXML file 'view.fxml'.";

        log.info("creating table");

        tcMotivo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getMotivoConsulta()));

        tcIdTratamiento.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getId().toString()));

        tcTratamiento.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTratamiento()));

        tcIdDescripcion.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getId().toString()));

        tcDescripcion
                .setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getDescripcionEvento()));

        tvFicha.getColumns().setAll(tcMotivo);
        tvHistoria.getColumns().setAll(tcIdDescripcion, tcDescripcion);
        tvTratamiento.getColumns().setAll(tcIdTratamiento, tcTratamiento);
        log.info("loading table items");
        dpHasta.setOnAction((event) -> {
            if (dpDesde.getValue() != null) {
                start = Date.valueOf(dpDesde.getValue());
                end = Date.valueOf(dpHasta.getValue());
                fichasList.setAll(daoFC.showByPatientBeetween(paciente, start, end));
                tvFicha.setItems(fichasList);
            }
        });

        tvFicha.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ficha = newValue;
                log.info("Item selected.");
            }
        });

        tvFicha.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY)
                if (event.getClickCount() == 2 && ficha != null) {
                    loadContent();
                    loadTables(ficha);
                }
        });

        tvHistoria.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                historia = newValue;
                log.info("Item selected.");
            }
        });

        tvHistoria.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY)
                if (event.getClickCount() == 2 && historia != null)
                    editHistory(event);
        });

        tvTratamiento.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tratamiento = newValue;
                log.info("Item selected (tratamiento).");
            }
        });

        tvTratamiento.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY)
                if (event.getClickCount() == 2 && tratamiento != null)
                    editTreatment(event);
        });
    }

    private void loadContent() {
        log.info("[ Loading panes ]");
//        fichaController = super.loadCustomAnchor(RouteExtra.CLINICVIEW.getPath(), apContent, fichaController);
        fichaController.setObject(ficha);
    }

    private void loadTables(FichasClinicas ficha) {
        log.info("Loading table items.");
        Task<List<Tratamientos>> task1 = daoTR.showByFicha(ficha);

        Task<List<HistoriaClinica>> task2 = daoHC.showByPatient(ficha);

        task1.setOnSucceeded(event -> {
            tratamientoList.setAll(task1.getValue());
            tvTratamiento.setItems(tratamientoList);
            ViewSwitcher.getLoadingDialog().getStage().close();
            log.info("Treatments Loaded.");
        });

        task2.setOnSucceeded(event -> {
            historiaList.setAll(task2.getValue());
            tvHistoria.setItems(historiaList);
            ViewSwitcher.getLoadingDialog().getStage().close();
            log.info("History Loaded.");
        });

        ViewSwitcher.getLoadingDialog().showStage();
        ViewSwitcher.getLoadingDialog().setTask(task1);
        ViewSwitcher.getLoadingDialog().setTask(task2);
        ViewSwitcher.getLoadingDialog().startTask();
    }

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    }

    private void editTreatment(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.treatment.ModalDialogController mc = vs.loadModal(Route.TRATAMIENTO.modalView(), "Tratamiento",
                event);
        mc.setObject(tratamiento);
        vs.getStage().setOnHiding(stageEvent -> {
            tratamiento = null;
        });
        mc.showModal(vs.getStage());
    }

    private void editHistory(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.clinicHistory.ModalDialogController mc = vs.loadModal(Route.HISTORIACLINICA.modalView(),
                "Historia Clínica", event);
        mc.setObject(historia);
        vs.getStage().setOnHiding(stageEvent -> {
            historia = null;
        });
        mc.showModal(vs.getStage());
    }
}
