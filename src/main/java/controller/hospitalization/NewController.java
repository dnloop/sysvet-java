package controller.hospitalization;

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

import dao.FichasClinicasHome;
import dao.InternacionesHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.Internaciones;
import model.Pacientes;
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
	private DatePicker dpFechaIngreso;

	@FXML
	private DatePicker dpFechaAlta;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnCancel;

	private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private InternacionesHome daoH = new InternacionesHome();

	private FichasClinicasHome daoCF = new FichasClinicasHome();

	private Internaciones hospitalization = new Internaciones();

	private final ObservableList<Pacientes> clinicalFileList = FXCollections.observableArrayList();

	private Date admissionDate;

	private RecordInsertCallback created;

	public void setCreatedCallback(RecordInsertCallback created) {
		this.created = created;
	}

	@FXML
	void initialize() {

		log.info("Retrieving details");

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
		clinicalFileList.add(patient);
		comboPaciente.setItems(clinicalFileList);
		comboPaciente.getSelectionModel().select(patient);
		comboPaciente.setDisable(true);
	}

	private void storeRecord() {
		// date conversion from LocalDate
		if (dpFechaAlta.getValue() != null) {
			Date fechaAlta = java.sql.Date.valueOf(dpFechaAlta.getValue());
			hospitalization.setFechaAlta(fechaAlta);
		}
		if (dpFechaIngreso.getValue() != null)
			admissionDate = java.sql.Date.valueOf(dpFechaIngreso.getValue());

		hospitalization.setFechaIngreso(admissionDate);
		hospitalization.setPacientes(comboPaciente.getSelectionModel().getSelectedItem());
		Date fecha = new Date();
		hospitalization.setCreatedAt(fecha);
		if (HibernateValidator.validate(hospitalization)) {
			daoH.add(hospitalization);
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

	public void loadDao() {
		Task<List<Pacientes>> task = daoCF.displayRecordsWithPatients();

		task.setOnSucceeded(event -> {
			clinicalFileList.setAll(task.getValue());
			comboPaciente.setItems(clinicalFileList); // to string?
			log.info(marker, "Loaded Item.");
		});

		ViewSwitcher.loadingDialog.addTask(task);
		ViewSwitcher.loadingDialog.startTask();
	}

	/**
	 * Clear all fields in the view, otherwise the cache displays old data.
	 */
	public void cleanFields() {
		dpFechaAlta.setValue(null);
		dpFechaIngreso.setValue(null);
		comboPaciente.setValue(null);
	}

	public Integer getID() {
		return hospitalization.getId();
	}
}
