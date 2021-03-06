package controller.patient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import dao.PacientesHome;
import dao.PropietariosHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Pacientes;
import model.Propietarios;
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
	private JFXButton btnCancel;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXTextField txtNombre;

	@FXML
	private JFXTextField txtEspecie;

	@FXML
	private JFXTextField txtRaza;

	@FXML
	private JFXRadioButton rbMale;

	@FXML
	private ToggleGroup sexTogle;

	@FXML
	private JFXRadioButton rbFemale;

	@FXML
	private JFXTextField txtTemp;

	@FXML
	private JFXTextField txtPelaje;

	@FXML
	private DatePicker dpFechaNac;

	@FXML
	private JFXComboBox<Propietarios> comboPropietarios;

	@FXML
	private ImageView foto;

	@FXML
	private JFXButton btnFoto;

	private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private PacientesHome daoPA = new PacientesHome();

	private PropietariosHome daoPO = new PropietariosHome();

	private Pacientes paciente = new Pacientes();

	private final ObservableList<Propietarios> propietariosList = FXCollections.observableArrayList();

	private Date fecha;

	private RecordInsertCallback created;

	public void setCreatedCallback(RecordInsertCallback created) {
		this.created = created;
	}

	@FXML
	void initialize() {

		log.info(marker, "Retrieving details");

		Platform.runLater(() -> {
			sexTogle.selectToggle(rbFemale);
			setRadioToggle();
			setFoto();

		});

		btnFoto.setOnAction((event) -> {
			File file = fileChooser();
			if (file != null) {
				paciente.setFoto(file.toURI().toString());
				foto.setImage(new Image(file.toURI().toString()));
			} else
				paciente.setFoto("/images/DogCat.jpg");
		});

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

	public void setObject(Pacientes paciente) {
		this.paciente = paciente;
	}

	public Integer getID() {
		return paciente.getId();
	}

	private void createRecord() {
		// date conversion from LocalDate
		if (dpFechaNac.getValue() != null)
			fecha = java.sql.Date.valueOf(dpFechaNac.getValue());

		paciente.setFechaNacimiento(fecha);
		paciente.setNombre(txtNombre.getText());
		paciente.setEspecie(txtEspecie.getText());
		paciente.setRaza(txtRaza.getText());
		paciente.setTemperamento(txtTemp.getText());
		paciente.setPelaje(txtPelaje.getText());
		paciente.setSexo(getToggleValue());
		paciente.setPropietarios(comboPropietarios.getSelectionModel().getSelectedItem());
		fecha = new Date();
		paciente.setCreatedAt(fecha);
		if (HibernateValidator.validate(paciente)) {
			daoPA.add(paciente);
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
			log.error("failed to create record");
		}
	}

	private String getToggleValue() {
		return sexTogle.getSelectedToggle().getUserData().toString();
	}

	private void setRadioToggle() {
		rbMale.setUserData('M');
		rbMale.setToggleGroup(sexTogle);
		rbFemale.setUserData('F');
		rbFemale.setToggleGroup(sexTogle);
	}

	/*
	 * Image handling methods This could also be a class but for the next refactor
	 * =)
	 */

	private File fileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Seleccionar Imagen");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
		return fileChooser.showOpenDialog(ViewSwitcher.modalStage);
	}

	/**
	 * On load checks the file path stored in the database.
	 * 
	 * Warning: current file could become unavailable.
	 */
	private void setFoto() {
		URL url;
		try {

			url = getClass().getResource("/images/DogCat.jpg");

			if (ImageIO.read(url) != null) {
				Image image = new Image(url.toString());
				foto.setImage(image);
			}
		} catch (IOException e) {
			DialogBox.setHeader("Ruta incorrecta");
			DialogBox.setContent(e.getMessage());
			DialogBox.displayError();
			foto = new ImageView("/images/DogCat.jpg");
		}
	}

	public void loadDao() {
		Task<List<Propietarios>> task = daoPO.displayRecords();

		task.setOnSucceeded(event -> {
			propietariosList.setAll(task.getValue());
			comboPropietarios.setItems(propietariosList);
			log.info(marker, "Loaded Item.");
		});

		ViewSwitcher.loadingDialog.addTask(task);
	}

	/**
	 * Clear all fields in the view, otherwise the cache displays old data.
	 */
	public void cleanFields() {
		dpFechaNac.setValue(null);
		txtNombre.clear();
		txtEspecie.clear();
		txtRaza.clear();
		txtTemp.clear();
		txtPelaje.clear();
		comboPropietarios.setValue(null);
	}
}
