package controller.patient;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import javafx.fxml.FXML;
import javafx.scene.Node;
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

    private static final Logger log = (Logger) LogManager.getLogger(MainController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    /*
     * Class Methods
     */

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    }

    public void loadPanes() {
        Node node; // empty node for readability
        log.info(marker, "[ Loading panes ]");
        log.debug(marker, "Attempting to load Pacientes-View.");
        node = ViewSwitcher.getView(RouteExtra.PACIENTEVIEW.getPath());
        apPaciente.getChildren().setAll(node);
        pacienteController = ViewSwitcher.getController(RouteExtra.PACIENTEVIEW.getPath());
        pacienteController.setObject(paciente);
        pacienteController.loadFields();
        log.debug(marker, "Attempting to load FichaClinica-View.");
        node = ViewSwitcher.getView(RouteExtra.CLINICOVERVIEW.getPath());
        apFicha.getChildren().setAll(node);
        fichaController = ViewSwitcher.getController(RouteExtra.CLINICOVERVIEW.getPath());
        fichaController.setObject(paciente);
        log.debug(marker, "Attempting to load ExamenGeneral-View.");
        node = ViewSwitcher.getView(RouteExtra.EXAMVIEW.getPath());
        apExamen.getChildren().setAll(node);
        examenController = ViewSwitcher.getController(RouteExtra.EXAMVIEW.getPath());
        examenController.setObject(paciente);
        examenController.loadDao();
        log.debug(marker, "Attempting to load Internaciones-View.");
        node = ViewSwitcher.getView(Route.INTERNACION.showView());
        apInternacion.getChildren().setAll(node);
        internacionController = ViewSwitcher.getController(Route.INTERNACION.showView());
        internacionController.setObject(paciente);
        internacionController.loadDao();
        log.debug(marker, "Attempting to load Vacunas-View.");
        node = ViewSwitcher.getView(Route.VACUNA.showView());
        apVacuna.getChildren().setAll(node);
        vacunaController = ViewSwitcher.getController(Route.VACUNA.showView());
        vacunaController.setObject(paciente);
        vacunaController.loadDao();
        log.debug(marker, "Attempting to load Desparasitaciones-View.");
        node = ViewSwitcher.getView(Route.DESPARASITACION.showView());
        apDesparasitaciones.getChildren().setAll(node);
        desparasitacionController = ViewSwitcher.getController(Route.DESPARASITACION.showView());
        desparasitacionController.setObject(paciente);
        desparasitacionController.loadDao();
        log.info(marker, "[ Panes Loaded ]");
    }
}
