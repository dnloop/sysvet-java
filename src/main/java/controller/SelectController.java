/**
 * Sample Skeleton for 'select.fxml' Controller Class
 */

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import utils.ViewSwitcher;
import utils.routes.Route;

public class SelectController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnIndCC"
    private JFXButton btnIndCC; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndDesp"
    private JFXButton btnIndDesp; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndExamen"
    private JFXButton btnIndExamen; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndFC"
    private JFXButton btnIndFC; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndHC"
    private JFXButton btnIndHC; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndInter"
    private JFXButton btnIndInter; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndPac"
    private JFXButton btnIndPac; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndProp"
    private JFXButton btnIndProp; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndTC"
    private JFXButton btnIndTC; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndVac"
    private JFXButton btnIndVac; // Value injected by FXMLLoader

    @FXML
    void newCC(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.currentAccount.NewController nc = vs.loadModal(Route.CUENTACORRIENTE.newView(),
                "Nuevo elemento - Cuenta Corriente", event);
        String path[] = { "Cuenta Corriente", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        nc.showModal(vs.getStage());
    }

    @FXML
    void newDesp(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.deworming.NewController nc = vs.loadModal(Route.DESPARASITACION.newView(),
                "Nuevo elemento - Desparasitación", event);
        String path[] = { "Desparasitación", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        nc.showModal(vs.getStage());
    }

    @FXML
    void newExamen(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.exam.NewController nc = vs.loadModal(Route.EXAMEN.newView(), "Nuevo elemento - Exámen General",
                event);
        String path[] = { "Exámen", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        nc.showModal(vs.getStage());
    }

    @FXML
    void newFC(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.clinicalFile.NewController nc = vs.loadModal(Route.FICHACLINICA.newView(),
                "Nuevo elemento - Ficha Clínica", event);
        String path[] = { "Ficha Clínica", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        nc.showModal(vs.getStage());
    }

    @FXML
    void newHC(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.clinicHistory.NewController nc = vs.loadModal(Route.HISTORIACLINICA.newView(),
                "Nuevo elemento - Historia Clínica", event);
        String path[] = { "Historia Clínica", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        nc.showModal(vs.getStage());
    }

    @FXML
    void newInter(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.internation.NewController nc = vs.loadModal(Route.INTERNACION.newView(),
                "Nuevo elemento - Internaciones", event);
        String path[] = { "Internación", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        nc.showModal(vs.getStage());
    }

    @FXML
    void newPac(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.patient.NewController sc = vs.loadModal(Route.PACIENTE.newView(), "Nuevo elemento - Paciente",
                event);
        String path[] = { "Paciente", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        sc.showModal(vs.getStage());
    }

    @FXML
    void newProp(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.owner.NewController nc = vs.loadModal(Route.PROPIETARIO.newView(), "Nuevo elemento - Propietario",
                event);
        String path[] = { "Propietario", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        nc.showModal(vs.getStage());
    }

    @FXML
    void newTC(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.treatment.NewController nc = vs.loadModal(Route.TRATAMIENTO.newView(),
                "Nuevo elemento - Tratamiento", event);
        String path[] = { "Tratamiento", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        nc.showModal(vs.getStage());
    }

    @FXML
    void newVac(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        controller.vaccine.NewController nc = vs.loadModal(Route.VACUNA.newView(), "Nuevo elemento - Vacunación",
                event);
        String path[] = { "Vacunación", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        nc.showModal(vs.getStage());
    }

    protected static final Logger log = (Logger) LogManager.getLogger(SelectController.class);

    private Stage stage;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnIndCC != null : "fx:id=\"btnIndCC\" was not injected: check your FXML file 'select.fxml'.";
        assert btnIndDesp != null : "fx:id=\"btnIndDesp\" was not injected: check your FXML file 'select.fxml'.";
        assert btnIndExamen != null : "fx:id=\"btnIndExamen\" was not injected: check your FXML file 'select.fxml'.";
        assert btnIndFC != null : "fx:id=\"btnIndFC\" was not injected: check your FXML file 'select.fxml'.";
        assert btnIndHC != null : "fx:id=\"btnIndHC\" was not injected: check your FXML file 'select.fxml'.";
        assert btnIndInter != null : "fx:id=\"btnIndInter\" was not injected: check your FXML file 'select.fxml'.";
        assert btnIndPac != null : "fx:id=\"btnIndPac\" was not injected: check your FXML file 'select.fxml'.";
        assert btnIndProp != null : "fx:id=\"btnIndProp\" was not injected: check your FXML file 'select.fxml'.";
        assert btnIndTC != null : "fx:id=\"btnIndTC\" was not injected: check your FXML file 'select.fxml'.";
        assert btnIndVac != null : "fx:id=\"btnIndVac\" was not injected: check your FXML file 'select.fxml'.";
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}
