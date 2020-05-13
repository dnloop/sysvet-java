package controller.patient;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Pacientes;
import utils.routes.Route;
import utils.routes.RouteExtra;
import utils.viewswitcher.ViewSwitcher;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane mainPatient;

    @FXML
    private Tab tabPaciente;

    @FXML
    private AnchorPane apPaciente;

    @FXML
    private Tab tabFicha;

    @FXML
    private AnchorPane apFicha;

    @FXML
    private Tab tabExamen;

    @FXML
    private AnchorPane apExamen;

    @FXML
    private Tab tabInternacion;

    @FXML
    private AnchorPane apInternacion;

    @FXML
    private Tab tabVacunas;

    @FXML
    private AnchorPane apVacuna;

    @FXML
    private Tab tabDesparasitaciones;

    @FXML
    private AnchorPane apDesparasitaciones;

    private Pacientes paciente;

    private controller.patient.ViewController pacienteController = null;

    private controller.clinicalFile.OverviewController fichaController = null;

    private controller.exam.ViewController examenController = null;

    private controller.hospitalization.ShowController internacionController = null;

    private controller.vaccine.ShowController vacunaController = null;

    private controller.deworming.ShowController desparasitacionController = null;

    protected static final Logger log = (Logger) LogManager.getLogger(MainController.class);

//    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    @FXML
    void initialize() {
    }

    /*
     * Class Methods
     */

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    }

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    public void loadPanes() {
        ViewSwitcher vs = new ViewSwitcher();
        log.info("[ Loading panes ]");
        log.debug("Attempting to load Pacientes-View.");
        pacienteController = vs.loadNode(RouteExtra.PACIENTEVIEW.getPath(), apPaciente);
        pacienteController.setObject(paciente);
        pacienteController.loadFields();
        log.debug("Attempting to load FichaClinica-View.");
        fichaController = vs.loadNode(RouteExtra.CLINICOVERVIEW.getPath(), apFicha);
        fichaController.setObject(paciente);
        log.debug("Attempting to load ExamenGeneral-View.");
        examenController = vs.loadNode(RouteExtra.EXAMVIEW.getPath(), apExamen);
        examenController.setObject(paciente);
        examenController.loadDao();
        log.debug("Attempting to load Internaciones-View.");
        internacionController = vs.loadNode(Route.INTERNACION.showView(), apInternacion);
        internacionController.setObject(paciente);
        internacionController.loadDao();
        log.debug("Attempting to load Vacunas-View.");
        vacunaController = vs.loadNode(Route.VACUNA.showView(), apVacuna);
        vacunaController.setObject(paciente);
        vacunaController.loadDao();
        log.debug("Attempting to load Desparasitaciones-View.");
        desparasitacionController = vs.loadNode(Route.DESPARASITACION.showView(), apDesparasitaciones);
        desparasitacionController.setObject(paciente);
        desparasitacionController.loadDao();
        log.info("[ Panes Loaded ]");
    }
}
