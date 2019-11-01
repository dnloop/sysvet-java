package controller.patient;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Pacientes;
import utils.ViewSwitcher;
import utils.routes.Route;
import utils.routes.RouteExtra;

public class MainController extends ViewSwitcher {

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

    private controller.internation.ShowController internacionController = null;

    private controller.vaccine.ShowController vacunaController = null;

    private controller.deworming.ShowController desparasitacionController = null;

    protected static final Logger log = (Logger) LogManager.getLogger(MainController.class);

//    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    @FXML
    void initialize() {
        assert tabPaciente != null : "fx:id=\"tabPaciente\" was not injected: check your FXML file 'main.fxml'.";
        assert apPaciente != null : "fx:id=\"apPaciente\" was not injected: check your FXML file 'main.fxml'.";
        assert tabFicha != null : "fx:id=\"tabFicha\" was not injected: check your FXML file 'main.fxml'.";
        assert apFicha != null : "fx:id=\"apFicha\" was not injected: check your FXML file 'main.fxml'.";
        assert tabExamen != null : "fx:id=\"tabExamen\" was not injected: check your FXML file 'main.fxml'.";
        assert apExamen != null : "fx:id=\"apExamen\" was not injected: check your FXML file 'main.fxml'.";
        assert tabInternacion != null : "fx:id=\"tabInternacion\" was not injected: check your FXML file 'main.fxml'.";
        assert apInternacion != null : "fx:id=\"apInternacion\" was not injected: check your FXML file 'main.fxml'.";
        assert tabVacunas != null : "fx:id=\"tabVacunas\" was not injected: check your FXML file 'main.fxml'.";
        assert apVacuna != null : "fx:id=\"apVacuna\" was not injected: check your FXML file 'main.fxml'.";
        assert tabDesparasitaciones != null : "fx:id=\"tabDesparasitaciones\" was not injected: check your FXML file 'main.fxml'.";
        assert apDesparasitaciones != null : "fx:id=\"apDesparasitaciones\" was not injected: check your FXML file 'main.fxml'.";

        Platform.runLater(() -> {
            loadPanes();
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    }

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    public void loadPanes() {
        log.info("[ Loading panes ]");
        log.debug("Attempting to load Pacientes-View.");
        pacienteController = super.loadCustomAnchor(RouteExtra.PACIENTEVIEW.getPath(), apPaciente, pacienteController);
        pacienteController.setObject(paciente);
        log.debug("Attempting to load FichaClinica-View.");
        fichaController = super.loadCustomAnchor(RouteExtra.CLINICOVERVIEW.getPath(), apFicha, fichaController);
        fichaController.setObject(paciente);
        log.debug("Attempting to load ExamenGeneral-View.");
        examenController = super.loadCustomAnchor(RouteExtra.EXAMVIEW.getPath(), apExamen, examenController);
        examenController.setObject(paciente);
        log.debug("Attempting to load Internaciones-View.");
        internacionController = super.loadCustomAnchor(Route.INTERNACION.showView(), apInternacion,
                internacionController);
        internacionController.setObject(paciente);
        log.debug("Attempting to load Vacunas-View.");
        vacunaController = super.loadCustomAnchor(Route.VACUNA.showView(), apVacuna, vacunaController);
        vacunaController.setObject(paciente);
        log.debug("Attempting to load Desparasitaciones-View.");
        desparasitacionController = super.loadCustomAnchor(Route.DESPARASITACION.showView(), apDesparasitaciones,
                desparasitacionController);
        desparasitacionController.setObject(paciente);
        log.info("[ Panes Loaded ]");
    }

}
