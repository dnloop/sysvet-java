package controller.clinicalFile;

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

import dao.FichasClinicasHome;
import dao.PacientesHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.FichasClinicas;
import model.Pacientes;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

public class NewController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXButton btnStore;

	@FXML
	private JFXButton btnCancel;

	@FXML
	private JFXComboBox<Pacientes> comboPA;

	@FXML
	private JFXTextField txtMotivoConsulta;

	@FXML
	private JFXTextArea txtAnamnesis;

	@FXML
	private JFXTextField txtMed;

	@FXML
	private JFXTextField txtEstNutricion;

	@FXML
	private JFXTextField txtEstSanitario;

	@FXML
	private JFXTextField txtAspectoGeneral;

	@FXML
	private JFXTextField txtDerivaciones;

	@FXML
	private JFXTextArea txtDeterDiagComp;

	@FXML
	private JFXTextArea txtPronostico;

	@FXML
	private JFXTextArea txtDiagnostico;

	@FXML
	private JFXTextArea txtExploracion;

	@FXML
	private JFXTextArea txtEvolucion;

	@FXML
	private DatePicker dpFecha;

	private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private FichasClinicasHome daoCF = new FichasClinicasHome();

	private PacientesHome daoPA = new PacientesHome();

	private FichasClinicas clinicalFile = new FichasClinicas();

	private final ObservableList<Pacientes> patientList = FXCollections.observableArrayList();

	private Date date;

	@FXML
	void initialize() {

		log.info(marker, "Retrieving details");

		comboPA.setItems(patientList);

		btnCancel.setOnAction((event) -> {
			cleanFields();
			ViewSwitcher.modalStage.close();
		});

		btnStore.setOnAction((event) -> {
			if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
				storeRecord();
		});
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

	private void storeRecord() {
		clinicalFile.setPacientes(comboPA.getSelectionModel().getSelectedItem());
		clinicalFile.setMotivoConsulta(txtMotivoConsulta.getText());
		clinicalFile.setAnamnesis(txtAnamnesis.getText());
		clinicalFile.setMedicacion(txtMed.getText());
		clinicalFile.setEstadoNutricion(txtEstNutricion.getText());
		clinicalFile.setEstadoSanitario(txtEstSanitario.getText());
		clinicalFile.setAspectoGeneral(txtAspectoGeneral.getText());
		clinicalFile.setDerivaciones(txtDerivaciones.getText());
		clinicalFile.setDeterDiagComp(txtDeterDiagComp.getText());
		clinicalFile.setPronostico(txtPronostico.getText());
		clinicalFile.setDiagnostico(txtDiagnostico.getText());
		clinicalFile.setExploracion(txtExploracion.getText());
		clinicalFile.setEvolucion(txtEvolucion.getText());
		if (dpFecha.getValue() != null)
			date = java.sql.Date.valueOf(dpFecha.getValue());

		clinicalFile.setFecha(date);
		date = new Date();
		clinicalFile.setCreatedAt(date);
		if (HibernateValidator.validate(clinicalFile)) {
			daoCF.add(clinicalFile);
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

	public void setObject(FichasClinicas fichaClinica) {
		this.clinicalFile = fichaClinica;
	}

	public void loadDao() {
		Task<List<Pacientes>> task = daoPA.displayRecords();

		task.setOnSucceeded(event -> {
			patientList.setAll(task.getValue());
			log.info(marker, "Loading fields");
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
		comboPA.setValue(null);
		txtMotivoConsulta.clear();
		txtAnamnesis.clear();
		txtMed.clear();
		txtEstNutricion.clear();
		txtEstSanitario.clear();
		txtAspectoGeneral.clear();
		txtDerivaciones.clear();
		txtDeterDiagComp.clear();
		txtPronostico.clear();
		txtDiagnostico.clear();
		txtExploracion.clear();
		txtEvolucion.clear();
		dpFecha.setValue(null);

	}
}
