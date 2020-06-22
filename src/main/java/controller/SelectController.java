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
		controller.currentAccount.NewController nc = ViewSwitcher.getController(Route.CUENTACORRIENTE.newView());
		nc.loadDao();

		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newDesp(ActionEvent event) {
		ViewSwitcher.loadModal(Route.DESPARASITACION.newView(), "Nuevo elemento - Desparasitación", true);
		controller.deworming.NewController nc = ViewSwitcher.getController(Route.DESPARASITACION.newView());
		nc.loadDao();

		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newExamen(ActionEvent event) {
		ViewSwitcher.loadModal(Route.EXAMEN.newView(), "Nuevo elemento - Exámen General", true);
		controller.exam.NewController nc = ViewSwitcher.getController(Route.EXAMEN.newView());
		nc.loadDao();

		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newFC(ActionEvent event) {
		ViewSwitcher.loadModal(Route.FICHACLINICA.newView(), "Nuevo elemento - Ficha Clínica", true);
		controller.clinicalFile.NewController nc = ViewSwitcher.getController(Route.FICHACLINICA.newView());
		nc.loadDao();

		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newHC(ActionEvent event) {
		ViewSwitcher.loadModal(Route.HISTORIACLINICA.newView(), "Nuevo elemento - Historia Clínica", true);
		controller.clinicHistory.NewController nc = ViewSwitcher.getController(Route.HISTORIACLINICA.newView());
		nc.loadDao();

		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newInter(ActionEvent event) {
		ViewSwitcher.loadModal(Route.INTERNACION.newView(), "Nuevo elemento - Internaciones", true);
		controller.hospitalization.NewController nc = ViewSwitcher.getController(Route.INTERNACION.newView());
		nc.loadDao();

		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newPac(ActionEvent event) {
		ViewSwitcher.loadModal(Route.PACIENTE.newView(), "Nuevo elemento - Paciente", true);
		controller.patient.NewController nc = ViewSwitcher.getController(Route.PACIENTE.newView());
		nc.loadDao();

		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newProp(ActionEvent event) {
		ViewSwitcher.loadModal(Route.PROPIETARIO.newView(), "Nuevo elemento - Propietario", true);
		controller.owner.NewController nc = ViewSwitcher.getController(Route.PROPIETARIO.newView());
		nc.loadDao();

		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newTC(ActionEvent event) {
		ViewSwitcher.loadModal(Route.TRATAMIENTO.newView(), "Nuevo elemento - Tratamiento", true);
		controller.treatment.NewController nc = ViewSwitcher.getController(Route.TRATAMIENTO.newView());
		nc.loadDao();

		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newVac(ActionEvent event) {
		ViewSwitcher.loadModal(Route.VACUNA.newView(), "Nuevo elemento - Vacunación", true);
		controller.vaccine.NewController nc = ViewSwitcher.getController(Route.VACUNA.newView());
		nc.loadDao();

		ViewSwitcher.modalStage.showAndWait();
	}
}
