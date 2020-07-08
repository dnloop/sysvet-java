package controller.vaccine;

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

import dao.PacientesHome;
import dao.VacunasHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.Pacientes;
import model.Vacunas;
import utils.DialogBox;
import utils.RecordInsertCallback;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

public class NewController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXComboBox<Pacientes> comboPaciente;

	@FXML
	private DatePicker dpFecha;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnCancel;

	@FXML
	private JFXTextField txtDesc;

	private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private static PacientesHome daoP = new PacientesHome();

	private static VacunasHome daoVC = new VacunasHome();

	private Vacunas vaccine = new Vacunas();

	private final ObservableList<Pacientes> patientList = FXCollections.observableArrayList();

	private Date date;

	private RecordInsertCallback created;

	public void setCreatedCallback(RecordInsertCallback created) {
		this.created = created;
	}

	@FXML
	void initialize() {

		btnCancel.setOnAction((event) -> {
			cleanFields();
			ViewSwitcher.modalStage.close();
		});

		btnSave.setOnAction((event) -> {
			if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
				createRecord();
		});
	}

	/*
	 * Class Methods
	 */

	public void setComboBox(Pacientes patient) {
		patientList.add(patient);
		comboPaciente.setItems(patientList);
		comboPaciente.getSelectionModel().select(patient);
		comboPaciente.setDisable(true);
	}

	private void createRecord() {
		// date conversion from LocalDate
		if (dpFecha.getValue() != null) {
			date = java.sql.Date.valueOf(dpFecha.getValue());
			vaccine.setFecha(date);
		}
		vaccine.setPacientes(comboPaciente.getSelectionModel().getSelectedItem());
		vaccine.setDescripcion(txtDesc.getText());
		date = new Date();
		vaccine.setCreatedAt(date);
		if (HibernateValidator.validate(vaccine)) {
			daoVC.add(vaccine);
			log.info(marker, "record created");
			DialogBox.displaySuccess();
			cleanFields();
			created.recordCreated(true);
			ViewSwitcher.modalStage.close();
		} else {
			DialogBox.setHeader("Fallo en la carga del registro");
			DialogBox.setContent(HibernateValidator.getError());
			DialogBox.displayError();
			HibernateValidator.resetError();
			log.error(marker, "failed to create record");
		}
	}

	public void setObject(Vacunas vacuna) {
		this.vaccine = vacuna;
	}

	public void loadDao() {
		Task<List<Pacientes>> task = daoP.displayRecords();

		task.setOnSucceeded(event -> {
			patientList.setAll(task.getValue());
			comboPaciente.setItems(patientList);
			log.info(marker, "Loaded Item.");
		});

		ViewSwitcher.loadingDialog.addTask(task);
		ViewSwitcher.loadingDialog.startTask();
	}

	/**
	 * Clear all fields in the view, otherwise the cache displays old data.
	 */
	public void cleanFields() {
		dpFecha.setValue(null);
		comboPaciente.setValue(null);
		txtDesc.clear();
		comboPaciente.setDisable(false);
	}

	public Integer getID() {
		return vaccine.getId();
	}
}
