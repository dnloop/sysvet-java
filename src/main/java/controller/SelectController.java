package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.RecordInsertCallback;
import utils.routes.Route;
import utils.viewswitcher.ViewSwitcher;

public class SelectController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXButton btnIndCC;

	@FXML
	private JFXButton btnIndDesp;

	@FXML
	private JFXButton btnIndExamen;

	@FXML
	private JFXButton btnIndFC;

	@FXML
	private JFXButton btnIndHC;

	@FXML
	private JFXButton btnIndInter;

	@FXML
	private JFXButton btnIndPac;

	@FXML
	private JFXButton btnIndProp;

	@FXML
	private JFXButton btnIndTC;

	@FXML
	private JFXButton btnIndVac;

	/**
	 * Boolean property used to confirm the database has changed and perform
	 * subsequent actions.
	 */
	private SimpleBooleanProperty updated = new SimpleBooleanProperty(false);

	/**
	 * Used as a callback to confirm the database has changed and the controls needs
	 * to be updated.
	 */
	private RecordInsertCallback created = new RecordInsertCallback() {

		@Override
		public void recordCreated(boolean record) {
			if (record)
				updated.setValue(Boolean.FALSE);
		}
	};

	public boolean isUpdated() {
		return updated.get();
	}

	public void setUpdated(boolean updated) {
		this.updated.setValue(updated);
	}

	@FXML
	void newCC(ActionEvent event) {
		ViewSwitcher.loadModal(Route.CUENTACORRIENTE.newView(), "Nuevo elemento - Cuenta Corriente", true);
		controller.currentAccount.NewController nc = ViewSwitcher.getController(Route.CUENTACORRIENTE.newView());
		nc.loadDao();
		ViewSwitcher.loadingDialog.startTask();
		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newDesp(ActionEvent event) {
		ViewSwitcher.loadModal(Route.DESPARASITACION.newView(), "Nuevo elemento - Desparasitación", true);
		controller.deworming.NewController nc = ViewSwitcher.getController(Route.DESPARASITACION.newView());
		nc.setCreatedCallback(created);
		nc.loadDao();
		updated.addListener((obs, oldVal, newVal) -> {
			if (!updated.getValue()) {
				nc.cleanFields();
			}
		});
		ViewSwitcher.loadingDialog.startTask();
		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newExamen(ActionEvent event) {
		ViewSwitcher.loadModal(Route.EXAMEN.newView(), "Nuevo elemento - Exámen General", true);
		controller.exam.NewController nc = ViewSwitcher.getController(Route.EXAMEN.newView());
		nc.setCreatedCallback(created);
		nc.loadDao();
		updated.addListener((obs, oldVal, newVal) -> {
			if (!updated.getValue()) {
				nc.cleanFields();
			}
		});
		ViewSwitcher.loadingDialog.startTask();
		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newFC(ActionEvent event) {
		ViewSwitcher.loadModal(Route.FICHACLINICA.newView(), "Nuevo elemento - Ficha Clínica", true);
		controller.clinicalFile.NewController nc = ViewSwitcher.getController(Route.FICHACLINICA.newView());
		nc.setCreatedCallback(created);
		nc.loadDao();
		updated.addListener((obs, oldVal, newVal) -> {
			if (!updated.getValue()) {
				nc.cleanFields();
			}
		});
		ViewSwitcher.loadingDialog.startTask();
		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newHC(ActionEvent event) {
		ViewSwitcher.loadModal(Route.HISTORIACLINICA.newView(), "Nuevo elemento - Historia Clínica", true);
		controller.clinicHistory.NewController nc = ViewSwitcher.getController(Route.HISTORIACLINICA.newView());
		nc.loadDao();
		ViewSwitcher.modalStage.setOnHidden(stageEvent -> {
			nc.cleanFields();
		});
		ViewSwitcher.loadingDialog.startTask();
		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newInter(ActionEvent event) {
		ViewSwitcher.loadModal(Route.INTERNACION.newView(), "Nuevo elemento - Internaciones", true);
		controller.hospitalization.NewController nc = ViewSwitcher.getController(Route.INTERNACION.newView());
		nc.setCreatedCallback(created);
		nc.loadDao();
		updated.addListener((obs, oldVal, newVal) -> {
			if (!updated.getValue()) {
				nc.cleanFields();
			}
		});
		ViewSwitcher.loadingDialog.startTask();
		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newPac(ActionEvent event) {
		ViewSwitcher.loadModal(Route.PACIENTE.newView(), "Nuevo elemento - Paciente", true);
		controller.patient.NewController nc = ViewSwitcher.getController(Route.PACIENTE.newView());
		nc.setCreatedCallback(created);
		nc.loadDao();
		ViewSwitcher.modalStage.setOnHidden((stageEvent) -> {
			controller.patient.IndexController ic;
			try {
				ic = ViewSwitcher.getController(Route.PACIENTE.indexView());
				ic.setUpdated(false);
				ViewSwitcher.setController(Route.PACIENTE.indexView(), ic);
			} catch (NullPointerException e) {
				ViewSwitcher.uiLoader.buildNode(Route.PACIENTE.indexView());
				ic = ViewSwitcher.getController(Route.PACIENTE.indexView());
				ic.setUpdated(false);
				ViewSwitcher.setController(Route.PACIENTE.indexView(), ic);
			}
		});

		ViewSwitcher.loadingDialog.startTask();
		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newProp(ActionEvent event) {
		ViewSwitcher.loadModal(Route.PROPIETARIO.newView(), "Nuevo elemento - Propietario", true);
		controller.owner.NewController nc = ViewSwitcher.getController(Route.PROPIETARIO.newView());
		nc.setCreatedCallback(created);
		nc.loadDao();
		ViewSwitcher.modalStage.setOnHidden((stageEvent) -> {
			controller.owner.IndexController ic;
			try {
				ic = ViewSwitcher.getController(Route.PROPIETARIO.indexView());
				ic.setUpdated(false);
				ViewSwitcher.setController(Route.PROPIETARIO.indexView(), ic);
			} catch (NullPointerException e) {
				ViewSwitcher.uiLoader.buildNode(Route.PROPIETARIO.indexView());
				ic = ViewSwitcher.getController(Route.PROPIETARIO.indexView());
				ic.setUpdated(false);
				ViewSwitcher.setController(Route.PROPIETARIO.indexView(), ic);
			}
		});

		ViewSwitcher.loadingDialog.startTask();
		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newTC(ActionEvent event) {
		ViewSwitcher.loadModal(Route.TRATAMIENTO.newView(), "Nuevo elemento - Tratamiento", true);
		controller.treatment.NewController nc = ViewSwitcher.getController(Route.TRATAMIENTO.newView());
		nc.loadDao();
		ViewSwitcher.modalStage.setOnHidden(stageEvent -> {
			nc.cleanFields();
		});
		ViewSwitcher.loadingDialog.startTask();
		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newVac(ActionEvent event) {
		ViewSwitcher.loadModal(Route.VACUNA.newView(), "Nuevo elemento - Vacunación", true);
		controller.vaccine.NewController nc = ViewSwitcher.getController(Route.VACUNA.newView());
		nc.loadDao();
		ViewSwitcher.loadingDialog.startTask();
		ViewSwitcher.modalStage.showAndWait();
	}
}
