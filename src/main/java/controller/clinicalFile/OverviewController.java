package controller.clinicalFile;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import dao.FichasClinicasHome;
import dao.HistoriaClinicaHome;
import dao.TratamientosHome;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import utils.ViewSwitcher;
import utils.routes.RouteExtra;

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
    private TableColumn<FichasClinicas, String> tcMotivo;

    @FXML
    private AnchorPane apContent;

    @FXML
    private TableView<Tratamientos> tvTratamiento;

    @FXML
    private TableColumn<Tratamientos, String> tcIdTratamiento;

    @FXML
    private TableColumn<Tratamientos, String> tcTratamiento;

    @FXML
    private TableView<HistoriaClinica> tvHistoria;

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

        tcMotivo.setCellValueFactory(
                (TableColumn.CellDataFeatures<FichasClinicas, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getMotivoConsulta()));

        tcIdTratamiento.setCellValueFactory(
                (TableColumn.CellDataFeatures<Tratamientos, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getId().toString()));

        tcTratamiento.setCellValueFactory(
                (TableColumn.CellDataFeatures<Tratamientos, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getTratamiento()));

        tcIdDescripcion.setCellValueFactory(
                (TableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getId().toString()));

        tcDescripcion.setCellValueFactory(
                (TableColumn.CellDataFeatures<HistoriaClinica, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getDescripcionEvento()));

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
    }

    private void loadContent() {
        log.info("[ Loading panes ]");
        fichaController = super.loadCustomAnchor(RouteExtra.CLINICVIEW.getPath(), apContent, fichaController);
        fichaController.setObject(ficha);
    }

    private void loadTables(FichasClinicas ficha) {
        tratamientoList.setAll(daoTR.showByFicha(ficha));
        tvTratamiento.setItems(tratamientoList);
        historiaList.setAll(daoHC.showByPatient(ficha));
        tvHistoria.setItems(historiaList);
    }

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    }
}
