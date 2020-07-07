package controller.owner;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import dao.LocalidadesHome;
import dao.PropietariosHome;
import dao.ProvinciasHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import model.Localidades;
import model.Propietarios;
import model.Provincias;
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
	private JFXTextField txtNombre;

	@FXML
	private JFXTextField txtApellido;

	@FXML
	private JFXTextField txtDomicilio;

	@FXML
	private JFXTextField txtTelCel;

	@FXML
	private JFXTextField txtTelFijo;

	@FXML
	private JFXTextField txtMail;

	@FXML
	private JFXComboBox<Provincias> comboProvincia;

	@FXML
	private JFXComboBox<Localidades> comboLocalidad;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnCancel;

	protected static final Logger log = (Logger) LogManager.getLogger(NewController.class);

	private ProvinciasHome daoPR = new ProvinciasHome();

	private LocalidadesHome daoLC = new LocalidadesHome();

	private PropietariosHome daoO = new PropietariosHome();

	final ObservableList<Localidades> localidades = FXCollections.observableArrayList();

	final ObservableList<Provincias> provinciasList = FXCollections.observableArrayList();

	private Propietarios owner = new Propietarios();

	private RecordInsertCallback created;

	public void setCreatedCallback(RecordInsertCallback created) {
		this.created = created;
	}

	@FXML
	void initialize() {

		log.info("Retrieving details");

		comboProvincia.setItems(provinciasList);
		comboLocalidad.setDisable(true);

		comboProvincia.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				comboLocalidad.setDisable(false);
				Task<List<Localidades>> task = daoLC.showByProvincia(newValue);

				task.setOnSucceeded(event -> {
					localidades.setAll(task.getValue());
					comboLocalidad.getItems().clear();
					comboLocalidad.getItems().setAll(localidades);
					log.info("Loaded Items.");
				});

				ViewSwitcher.loadingDialog.addTask(task);
				ViewSwitcher.loadingDialog.startTask();

			} else {
				comboLocalidad.getItems().clear();
				comboLocalidad.setDisable(true);
			}
		});

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
	 * Class methods
	 */

	private void storeRecord() {
		owner.setNombre(txtNombre.getText());
		owner.setApellido(txtApellido.getText());
		owner.setDomicilio(txtDomicilio.getText());
		owner.setLocalidades(comboLocalidad.getSelectionModel().getSelectedItem());
		Date fecha = new Date();
		owner.setCreatedAt(fecha);
		if (HibernateValidator.validate(owner)) {
			daoO.add(owner);
			log.info("record created");
			DialogBox.displaySuccess();
			cleanFields();
			created.recordCreated(true);
			ViewSwitcher.modalStage.close();
		} else {
			DialogBox.setHeader("Fallo en la carga del registro");
			DialogBox.setContent(HibernateValidator.getError());
			DialogBox.displayError();
			log.error("failed to create record");
		}
	}

	public void loadDao() {
		Task<List<Provincias>> task = daoPR.displayRecords();

		task.setOnSucceeded(event -> {
			provinciasList.setAll(task.getValue());
			comboProvincia.setItems(provinciasList);
			log.info("Loaded Item.");
		});

		ViewSwitcher.loadingDialog.addTask(task);
		ViewSwitcher.loadingDialog.startTask();
	}

	/**
	 * Clear all fields in the view, otherwise the cache displays old data.
	 */
	public void cleanFields() {
		txtNombre.clear();
		txtApellido.clear();
		txtDomicilio.clear();
		comboLocalidad.setValue(null);
		comboProvincia.setValue(null);
	}

	public Integer getID() {
		return owner.getId();
	}

}
