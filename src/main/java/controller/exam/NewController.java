package controller.exam;

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
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dao.ExamenGeneralHome;
import dao.PacientesHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import model.ExamenGeneral;
import model.Pacientes;
import utils.DialogBox;
import utils.FieldFormatter;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

public class NewController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnCancel;

	@FXML
	private JFXComboBox<Pacientes> comboPA;

	@FXML
	private JFXTextField txtPesoCorp;

	@FXML
	private DatePicker dpFecha;

	@FXML
	private JFXTextField txtTempCorp;

	@FXML
	private JFXTextField txtDeshidratacion;

	@FXML
	private JFXTextField txtFrecResp;

	@FXML
	private JFXTextField txtAmplitud;

	@FXML
	private JFXTextField txtTipo;

	@FXML
	private JFXTextField txtRitmo;

	@FXML
	private JFXTextField txtFrecCardio;

	@FXML
	private JFXTextField txtPulso;

	@FXML
	private JFXTextField txtTllc;

	@FXML
	private JFXTextField txtBucal;

	@FXML
	private JFXTextField txtEscleral;

	@FXML
	private JFXTextField txtPalperal;

	@FXML
	private JFXTextField txtSexual;

	@FXML
	private JFXTextField txtSubmandibular;

	@FXML
	private JFXTextField txtPreescapular;

	@FXML
	private JFXTextField txtPrecrural;

	@FXML
	private JFXTextField txtInguinal;

	@FXML
	private JFXTextField txtPopliteo;

	@FXML
	private JFXTextArea txtOtros;

	@FXML
	private Label lblSexual;

	private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private ExamenGeneralHome daoEX = new ExamenGeneralHome();

	private PacientesHome daoPA = new PacientesHome();

	private ExamenGeneral exam = new ExamenGeneral();

	private Pacientes patient;

	private final ObservableList<Pacientes> patientList = FXCollections.observableArrayList();

	private Date date;

	private FieldFormatter fieldFormatter = new FieldFormatter();

	@FXML
	void initialize() {

		log.info(marker, "Retrieving details");

		comboPA.setOnAction((event) -> {
			patient = comboPA.getSelectionModel().getSelectedItem();
			if (patient != null)
				if (patient.getSexo().equals("F"))
					lblSexual.setText("Vulvar");
				else
					lblSexual.setText("Peneana");
		});

		btnCancel.setOnAction((event) -> {
			cleanFields();
			ViewSwitcher.modalStage.close();
		});

		btnSave.setOnAction((event) -> {
			if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
				storeRecord();
		});

		formatMask();
	}

	/*
	 * Class Methods
	 */

	public void setComboBox(Pacientes patient) {
		patientList.add(patient);
		comboPA.setItems(patientList);
		comboPA.getSelectionModel().select(patient);
		comboPA.setDisable(true);
	}

	private void formatMask() {
		fieldFormatter.setInteger();
		txtPesoCorp.setTextFormatter(fieldFormatter.getInteger());
		fieldFormatter = new FieldFormatter();
		fieldFormatter.setInteger();
		txtTempCorp.setTextFormatter(fieldFormatter.getInteger());
		fieldFormatter = new FieldFormatter();
		fieldFormatter.setInteger();
		txtDeshidratacion.setTextFormatter(fieldFormatter.getInteger());
		fieldFormatter = new FieldFormatter();
		fieldFormatter.setInteger();
		txtFrecResp.setTextFormatter(fieldFormatter.getInteger());
		fieldFormatter = new FieldFormatter();
		fieldFormatter.setInteger();
		txtFrecCardio.setTextFormatter(fieldFormatter.getInteger());
		fieldFormatter = new FieldFormatter();
		fieldFormatter.setInteger();
		txtAmplitud.setTextFormatter(fieldFormatter.getInteger());
		fieldFormatter = new FieldFormatter();
		fieldFormatter.setInteger();
		txtRitmo.setTextFormatter(fieldFormatter.getInteger());
		fieldFormatter = new FieldFormatter();
		fieldFormatter.setInteger();
		txtPulso.setTextFormatter(fieldFormatter.getInteger());
		fieldFormatter = new FieldFormatter();
		fieldFormatter.setInteger();
		txtTllc.setTextFormatter(fieldFormatter.getInteger());
	} // quick and dirty

	private void storeRecord() {
		// date conversion from LocalDate
		if (dpFecha.getValue() != null) {
			date = java.sql.Date.valueOf(dpFecha.getValue());
			exam.setFecha(date);
		}
		if (!txtPesoCorp.getText().isEmpty())
			exam.setPesoCorporal(Integer.valueOf(txtPesoCorp.getText()));
		if (!txtTempCorp.getText().isEmpty())
			exam.setTempCorporal(Integer.valueOf(txtTempCorp.getText()));
		if (!txtDeshidratacion.getText().isEmpty())
			exam.setDeshidratacion(Integer.valueOf(txtDeshidratacion.getText()));
		if (!txtFrecResp.getText().isEmpty())
			exam.setFrecResp(Integer.valueOf(txtFrecResp.getText()));
		if (!txtFrecCardio.getText().isEmpty())
			exam.setFrecCardio(Integer.valueOf(txtFrecCardio.getText()));
		exam.setAmplitud(txtAmplitud.getText());
		exam.setTipo(txtTipo.getText());
		exam.setRitmo(txtRitmo.getText());
		exam.setPulso(Integer.valueOf(txtPulso.getText()));
		if (!txtTllc.getText().isEmpty())
			exam.setTllc(Integer.valueOf(txtTllc.getText()));
		exam.setBucal(txtBucal.getText());
		exam.setEscleral(txtEscleral.getText());
		exam.setPalperal(txtPalperal.getText());
		exam.setSexual(txtSexual.getText());
		exam.setSubmandibular(txtSubmandibular.getText());
		exam.setPreescapular(txtPreescapular.getText());
		exam.setPrecrural(txtPrecrural.getText());
		exam.setInguinal(txtInguinal.getText());
		exam.setPopliteo(txtPopliteo.getText());
		exam.setOtros(txtOtros.getText());
		exam.setPacientes(comboPA.getSelectionModel().getSelectedItem());
		date = new Date();
		exam.setCreatedAt(date);
		if (HibernateValidator.validate(exam)) {
			daoEX.add(exam);
			log.info(marker, "record created");
			DialogBox.displaySuccess();
			cleanFields();
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
		Task<List<Pacientes>> task = daoPA.displayRecords();

		task.setOnSucceeded(event -> {
			patientList.setAll(task.getValue());
			comboPA.setItems(patientList);
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
		txtPesoCorp.clear();
		txtPesoCorp.clear();
		txtTempCorp.clear();
		txtTempCorp.clear();
		txtDeshidratacion.clear();
		txtDeshidratacion.clear();
		txtFrecResp.clear();
		txtFrecResp.clear();
		txtFrecCardio.clear();
		txtFrecCardio.clear();
		txtAmplitud.clear();
		txtTipo.clear();
		txtRitmo.clear();
		txtPulso.clear();
		txtTllc.clear();
		txtTllc.clear();
		txtBucal.clear();
		txtEscleral.clear();
		txtPalperal.clear();
		txtSexual.clear();
		txtSubmandibular.clear();
		txtPreescapular.clear();
		txtPrecrural.clear();
		txtInguinal.clear();
		txtPopliteo.clear();
		txtOtros.clear();
		comboPA.setValue(null);
	}
}
