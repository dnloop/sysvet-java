package controller.treatment;

import java.net.URL;
import java.sql.Time;
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
import com.jfoenix.controls.JFXTimePicker;

import dao.FichasClinicasHome;
import dao.TratamientosHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.FichasClinicas;
import model.Tratamientos;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

public class NewController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXComboBox<FichasClinicas> comboFicha;

	@FXML
	private JFXTextField txtTratamiento;

	@FXML
	private JFXTextField txtProcAdicional;

	@FXML
	private DatePicker dpFecha;

	@FXML
	private JFXTimePicker tpHora;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnCancel;

	private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private static TratamientosHome daoTR = new TratamientosHome();

	private static FichasClinicasHome daoCF = new FichasClinicasHome();

	private Tratamientos treatment = new Tratamientos();

	private final ObservableList<FichasClinicas> clinicalFileList = FXCollections.observableArrayList();

	private Date date;

	@FXML
	void initialize() {

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

	public void setComboBox(FichasClinicas clinicalFile) {
		clinicalFileList.add(clinicalFile);
		comboFicha.setItems(clinicalFileList);
		comboFicha.getSelectionModel().select(clinicalFile);
		comboFicha.setDisable(true);
	}

	private void storeRecord() {
		// date conversion from LocalDate
		if (dpFecha.getValue() != null) {
			date = java.sql.Date.valueOf(dpFecha.getValue());
			treatment.setFecha(date);
		}
		treatment.setTratamiento(txtTratamiento.getText());
		if (tpHora.getValue() != null)
			treatment.setHora(Time.valueOf(tpHora.getValue()));
		treatment.setFichasClinicas((comboFicha.getSelectionModel().getSelectedItem()));
		date = new Date();
		treatment.setCreatedAt(date);
		if (HibernateValidator.validate(treatment)) {
			daoTR.add(treatment);
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

	public void setObject(Tratamientos tratamiento) {
		this.treatment = tratamiento;
	}

	public void loadDao() {
		Task<List<FichasClinicas>> task = daoCF.displayRecords();

		task.setOnSucceeded(event -> {
			clinicalFileList.setAll(task.getValue());
			comboFicha.setItems(clinicalFileList);
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
		txtTratamiento.clear();
		tpHora.setValue(null);
		comboFicha.setValue(null);
	}
}
