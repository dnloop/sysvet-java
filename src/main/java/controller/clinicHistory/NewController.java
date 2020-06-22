package controller.clinicHistory;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dao.FichasClinicasHome;
import dao.HistoriaClinicaHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.FichasClinicas;
import model.HistoriaClinica;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

public class NewController {

	@FXML
	private JFXButton btnStore;

	@FXML
	private JFXButton btnCancel;

	@FXML
	private JFXComboBox<FichasClinicas> comboFC;

	@FXML
	private JFXTextField txtResultado;

	@FXML
	private JFXTextField txtSecuelas;

	@FXML
	private JFXTextField txtConsideraciones;

	@FXML
	private DatePicker dpFechaInicio;

	@FXML
	private DatePicker dpFechaResolucion;

	@FXML
	private JFXTextArea txtDescEvento;

	@FXML
	private JFXTextArea txtComentarios;

	private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private HistoriaClinicaHome daoCH = new HistoriaClinicaHome();

	private FichasClinicasHome daoCF = new FichasClinicasHome();

	private HistoriaClinica clinicHistory = new HistoriaClinica();

	private final ObservableList<FichasClinicas> clinicalFileList = FXCollections.observableArrayList();

	private Date date;

	private Date startDate;

	private Date resolutionDate;

	@FXML
	void initialize() {
		log.info(marker, "Retrieving details");

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

	public void setComboBox(FichasClinicas clinicalFile) {
		clinicalFileList.add(clinicalFile);
		comboFC.setItems(clinicalFileList);
		comboFC.getSelectionModel().select(clinicalFile);
		comboFC.setDisable(true);
	}

	private void storeRecord() {
		// date conversion from LocalDate
		date = new Date();
		if (dpFechaInicio.getValue() != null)
			startDate = java.sql.Date.valueOf(dpFechaInicio.getValue());

		if (dpFechaResolucion.getValue() != null)
			resolutionDate = java.sql.Date.valueOf(dpFechaResolucion.getValue());

		clinicHistory.setFechaInicio(startDate);
		clinicHistory.setFechaResolucion(resolutionDate);
		clinicHistory.setResultado(txtResultado.getText());
		clinicHistory.setSecuelas(txtSecuelas.getText());
		clinicHistory.setConsideraciones(txtConsideraciones.getText());
		clinicHistory.setComentarios(txtComentarios.getText());
		clinicHistory.setDescripcionEvento(txtDescEvento.getText());
		clinicHistory.setFichasClinicas(comboFC.getSelectionModel().getSelectedItem());
		clinicHistory.setCreatedAt(date);
		if (HibernateValidator.validate(clinicHistory)) {
			daoCH.add(clinicHistory);
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
		Task<List<FichasClinicas>> task = daoCF.displayRecords();

		task.setOnSucceeded(event -> {
			clinicalFileList.setAll(task.getValue());
			comboFC.setItems(clinicalFileList);
			log.info(marker, "Loaded Item.");
		});

		ViewSwitcher.loadingDialog.addTask(task);
		ViewSwitcher.loadingDialog.startTask();
	}

	/**
	 * Clear all fields in the view, otherwise the cache displays old data.
	 */
	public void cleanFields() {
		dpFechaInicio.setValue(null);
		dpFechaResolucion.setValue(null);
		txtResultado.clear();
		txtSecuelas.clear();
		txtConsideraciones.clear();
		txtComentarios.clear();
		txtDescEvento.clear();
		comboFC.setValue(null);
	}
}
