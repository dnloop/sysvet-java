package controller.currentAccount;

import java.math.BigDecimal;
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

import dao.CuentasCorrientesHome;
import dao.PropietariosHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyEvent;
import model.CuentasCorrientes;
import model.Propietarios;
import utils.DialogBox;
import utils.FieldFormatter;
import utils.RecordInsertCallback;
import utils.validator.HibernateValidator;
import utils.validator.Trim;
import utils.viewswitcher.ViewSwitcher;

public class NewController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXComboBox<Propietarios> comboPropietario;

	@FXML
	private JFXTextField txtDescription;

	@FXML
	private JFXTextField txtAmount;

	@FXML
	private DatePicker dpDate;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnCancel;

	private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private static CuentasCorrientesHome daoCC = new CuentasCorrientesHome();

	private static PropietariosHome daoPO = new PropietariosHome();

	private CuentasCorrientes cuentaCorriente = new CuentasCorrientes();

	final ObservableList<Propietarios> propietarios = FXCollections.observableArrayList();

	private FieldFormatter fieldFormatter = new FieldFormatter();

	private RecordInsertCallback created;

	public void setCreatedCallback(RecordInsertCallback created) {
		this.created = created;
	}

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

		Platform.runLater(() -> {
			fieldFormatter.setFloatPoint();
		});

	}

	/*
	 * Class Methods
	 */

	@FXML
	void formatMask(KeyEvent event) {
		txtAmount.setTextFormatter(fieldFormatter.getFloatPoint());
	}

	private void storeRecord() {
		// date conversion from LocalDate
		Date fecha = dpDate.getValue() != null ? java.sql.Date.valueOf(dpDate.getValue()) : null;
		cuentaCorriente.setFecha(fecha);
		cuentaCorriente.setDescripcion(Trim.trim(txtDescription.getText()));
		if (!txtAmount.getText().isEmpty())
			cuentaCorriente.setMonto(new BigDecimal(txtAmount.getText()));
		else
			cuentaCorriente.setMonto(null);
		cuentaCorriente.setPropietarios(comboPropietario.getSelectionModel().getSelectedItem());
		fecha = new Date();
		cuentaCorriente.setCreatedAt(fecha);
		if (HibernateValidator.validate(cuentaCorriente)) {
			daoCC.add(cuentaCorriente);
			log.info(marker, "record created");
			DialogBox.displaySuccess();
			cleanFields();
			created.recordCreated(true);
			ViewSwitcher.modalStage.close();
		} else {
			DialogBox.setHeader("Fallo en la carga del registro");
			DialogBox.setContent(HibernateValidator.getError());
			DialogBox.displayError();
			log.error(marker, "failed to create record");
		}
	}

	public void loadDao() {
		log.info(marker, "loading table items");
		Task<List<Propietarios>> task = daoPO.displayRecords();

		task.setOnSucceeded(event -> {
			propietarios.setAll(task.getValue());
			comboPropietario.setItems(propietarios);
			log.info(marker, "Loaded Item.");
		});

		ViewSwitcher.loadingDialog.addTask(task);
	}

	/**
	 * Clear all fields in the view, otherwise the cache displays old data.
	 */
	public void cleanFields() {
		dpDate.setValue(null);
		txtDescription.clear();
		txtAmount.clear();
		txtAmount.clear();
		comboPropietario.setValue(null);
	}
}
