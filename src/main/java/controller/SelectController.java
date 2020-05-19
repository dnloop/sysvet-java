package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.routes.Route;
import utils.viewswitcher.ViewSwitcher;

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
        ViewSwitcher.loadModal(Route.CUENTACORRIENTE.newView(), "Nuevo elemento - Cuenta Corriente", true);
        String path[] = { "Cuenta Corriente", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newDesp(ActionEvent event) {
        ViewSwitcher.loadModal(Route.DESPARASITACION.newView(), "Nuevo elemento - Desparasitación", true);
        String path[] = { "Desparasitación", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newExamen(ActionEvent event) {
        ViewSwitcher.loadModal(Route.EXAMEN.newView(), "Nuevo elemento - Exámen General", true);
        String path[] = { "Exámen", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newFC(ActionEvent event) {
        ViewSwitcher.loadModal(Route.FICHACLINICA.newView(), "Nuevo elemento - Ficha Clínica", true);
        String path[] = { "Ficha Clínica", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newHC(ActionEvent event) {
        ViewSwitcher.loadModal(Route.HISTORIACLINICA.newView(), "Nuevo elemento - Historia Clínica", true);
        String path[] = { "Historia Clínica", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newInter(ActionEvent event) {
        ViewSwitcher.loadModal(Route.INTERNACION.newView(), "Nuevo elemento - Internaciones", true);
        String path[] = { "Internación", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newPac(ActionEvent event) {
        ViewSwitcher.loadModal(Route.PACIENTE.newView(), "Nuevo elemento - Paciente", true);
        String path[] = { "Paciente", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newProp(ActionEvent event) {
        ViewSwitcher.loadModal(Route.PROPIETARIO.newView(), "Nuevo elemento - Propietario", true);
        String path[] = { "Propietario", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newTC(ActionEvent event) {
        ViewSwitcher.loadModal(Route.TRATAMIENTO.newView(), "Nuevo elemento - Tratamiento", true);
        String path[] = { "Tratamiento", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newVac(ActionEvent event) {
        ViewSwitcher.loadModal(Route.VACUNA.newView(), "Nuevo elemento - Vacunación", true);
        String path[] = { "Vacunación", "Nuevo Registro" };
        ViewSwitcher.setPath(path);
        ViewSwitcher.modalStage.showAndWait();
    }
}
