package controller.deworming;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import dao.DesparasitacionesHome;
import dao.PacientesHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.Desparasitaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.validator.Trim;
import utils.viewswitcher.ViewSwitcher;

public class NewController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXComboBox<Pacientes> comboPatient;

	@FXML
	private JFXTextField txtTreatment;

	@FXML
	private JFXTextField txtType;

	@FXML
	private DatePicker dpDate;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnCancel;

	@FXML
	private DatePicker dpNextDate;

	private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private DesparasitacionesHome daoD = new DesparasitacionesHome();

	private PacientesHome daoPA = new PacientesHome();

	private Desparasitaciones deworming = new Desparasitaciones();

	private final ObservableList<Pacientes> patientList = FXCollections.observableArrayList();

	private Date date;

	private Date nextDate;

	@FXML
	void initialize() {

		log.info(marker, "Retrieving details");

		btnCancel.setOnAction((event) -> {
			cleanFields();
			ViewSwitcher.modalStage.close();
		});

		btnSave.setOnAction((event) -> {
			if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
				storeRecord();
		});
	}

	/*
	 * Class Methods
	 */

	public void setComboBox(Pacientes patient) {
		patientList.add(patient);
		comboPatient.setItems(patientList);
		comboPatient.getSelectionModel().select(patient);
		comboPatient.setDisable(true);
	}

	private void storeRecord() {
		// date conversion from LocalDate
		if (dpDate.getValue() != null)
			date = java.sql.Date.valueOf(dpDate.getValue());

		if (dpNextDate.getValue() != null)
			nextDate = java.sql.Date.valueOf(dpNextDate.getValue());

		deworming.setFecha(date);
		deworming.setTratamiento(Trim.trim(txtTreatment.getText()));
		deworming.setTipo(Trim.trim(txtType.getText()));
		deworming.setPacientes(comboPatient.getSelectionModel().getSelectedItem());
		deworming.setFechaProxima(nextDate);
		date = new Date();
		deworming.setCreatedAt(date);
		if (HibernateValidator.validate(deworming)) {
			daoD.add(deworming);
			log.info(marker, "record created");
			DialogBox.displaySuccess();
			cleanFields();
			ViewSwitcher.modalStage.close();
		} else {
			DialogBox.setHeader("Fallo en la carga del registro");
			DialogBox.setContent(HibernateValidator.getError());
			DialogBox.displayError();
			log.error("failed to create record");
		}
	}

	public void loadDao() {
		Task<List<Pacientes>> task = daoPA.displayRecords();

		task.setOnSucceeded(event -> {
			patientList.setAll(task.getValue());
			comboPatient.setItems(patientList);
			log.info(marker, "Loaded Item.");
		});

		ViewSwitcher.loadingDialog.addTask(task);
		ViewSwitcher.loadingDialog.startTask();
	}

	/**
	 * Clear all fields in the view, otherwise the cache displays old data.
	 */
	public void cleanFields() {
		dpDate.setValue(null);
		dpNextDate.setValue(null);
		txtTreatment.clear();
		txtType.clear();
		comboPatient.setValue(null);
	}
}
