package controller.patient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
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
import javafx.stage.Stage;
import model.Pacientes;
import model.Propietarios;
import utils.DialogBox;
import utils.ViewSwitcher;
import utils.validator.HibernateValidator;

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

    protected static final Logger log = (Logger) LogManager.getLogger(NewController.class);

    private PacientesHome daoPA = new PacientesHome();

    private PropietariosHome daoPO = new PropietariosHome();

    private Pacientes paciente = new Pacientes();

    private Stage stage;

    final ObservableList<Propietarios> propietariosList = FXCollections.observableArrayList();

    private Date fecha;

    @FXML
    void initialize() {
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'new.fxml'.";
        assert txtEspecie != null : "fx:id=\"txtEspecie\" was not injected: check your FXML file 'new.fxml'.";
        assert txtRaza != null : "fx:id=\"txtRaza\" was not injected: check your FXML file 'new.fxml'.";
        assert rbMale != null : "fx:id=\"rbMale\" was not injected: check your FXML file 'new.fxml'.";
        assert sexTogle != null : "fx:id=\"sexTogle\" was not injected: check your FXML file 'new.fxml'.";
        assert rbFemale != null : "fx:id=\"rbFemale\" was not injected: check your FXML file 'new.fxml'.";
        assert txtTemp != null : "fx:id=\"txtTemp\" was not injected: check your FXML file 'new.fxml'.";
        assert txtPelaje != null : "fx:id=\"txtPelaje\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFechaNac != null : "fx:id=\"dpFechaNac\" was not injected: check your FXML file 'new.fxml'.";
        assert comboPropietarios != null : "fx:id=\"comboPropietarios\" was not injected: check your FXML file 'new.fxml'.";
        assert foto != null : "fx:id=\"foto\" was not injected: check your FXML file 'new.fxml'.";
        assert btnFoto != null : "fx:id=\"btnFoto\" was not injected: check your FXML file 'new.fxml'.";

        log.info("Retrieving details");
        loadDao();

        Platform.runLater(() -> {
            sexTogle.selectToggle(rbFemale);
            setRadioToggle();
            setFoto();

        }); // required to prevent NullPointer

        btnFoto.setOnAction((event) -> {
            File file = fileChooser();
            if (file != null) {
                paciente.setFoto(file.toURI().toString());
                foto.setImage(new Image(file.toURI().toString()));
            } else
                paciente.setFoto("/images/DogCat.jpg");
        });

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnSave.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
                createRecord();
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
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
            log.info("record created");
            DialogBox.displaySuccess();
            this.stage.close();
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
        return fileChooser.showOpenDialog(stage);
    }

    /*
     * on load checks filepath from db. Warning: current file could become
     * unavailable.
     */
    private void setFoto() {
        /*
         * IT WORKS No me convence, quizas haya una mejor manera.
         */
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

    private void loadDao() {
        Task<List<Propietarios>> task = daoPO.displayRecords();

        task.setOnSucceeded(event -> {
            propietariosList.setAll(task.getValue());
            comboPropietarios.setItems(propietariosList);
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
        ViewSwitcher.getLoadingDialog().startTask();
    }
}
