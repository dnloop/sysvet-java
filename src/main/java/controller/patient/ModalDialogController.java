package controller.patient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

public class ModalDialogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

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

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private PacientesHome daoPA = new PacientesHome();

    private PropietariosHome daoPO = new PropietariosHome();

    private Pacientes paciente;

    private Stage stage;

//    private String imgPath;

    final ObservableList<Propietarios> propietarios = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEspecie != null : "fx:id=\"txtEspecie\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtRaza != null : "fx:id=\"txtRaza\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert rbMale != null : "fx:id=\"rbMale\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert sexTogle != null : "fx:id=\"sexTogle\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert rbFemale != null : "fx:id=\"rbFemale\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTemp != null : "fx:id=\"txtTemp\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPelaje != null : "fx:id=\"txtPelaje\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFechaNac != null : "fx:id=\"dpFechaNac\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboPropietarios != null : "fx:id=\"comboPropietario\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert foto != null : "fx:id=\"foto\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnFoto != null : "fx:id=\"btnFoto\" was not injected: check your FXML file 'modalDialog.fxml'.";

        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            propietarios.setAll(daoPO.displayRecords());
            log.info("Loading fields");
            // required conversion for datepicker
            Date fecha = new Date(paciente.getFechaNacimiento().getTime());
            LocalDate lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            txtNombre.setText(paciente.getNombre());
            txtEspecie.setText(paciente.getEspecie());
            dpFechaNac.setValue(lfecha);
            txtRaza.setText(paciente.getRaza());
            txtTemp.setText(paciente.getTemperamento());
            txtPelaje.setText(paciente.getPelaje());
            // Radio button selection
            setRadioToggle();
            setFoto();

            comboPropietarios.setItems(propietarios);
            comboPropietarios.getSelectionModel().select(paciente.getPropietarios().getId() - 1); // arrays starts
                                                                                                  // at 0 =)
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

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
                updateRecord();
        });
    }

    /**
     *
     * Class Methods
     *
     */

    private void updateRecord() {
        // date conversion from LocalDate
        Date fecha = java.sql.Date.valueOf(dpFechaNac.getValue());
        paciente.setFechaNacimiento(fecha);
        paciente.setNombre(txtNombre.getText());
        paciente.setEspecie(txtEspecie.getText());
        paciente.setRaza(txtRaza.getText());
        paciente.setSexo(getToggleValue());
        paciente.setPropietarios(comboPropietarios.getSelectionModel().getSelectedItem());
        fecha = new Date();
        paciente.setUpdatedAt(fecha);
        daoPA.update(paciente);
        log.info("record updated");
        this.stage.close();
    }

    private void setRadioToggle() {
        rbMale.setUserData('M');
        rbMale.setToggleGroup(sexTogle);
        rbFemale.setUserData('F');
        rbFemale.setToggleGroup(sexTogle);
        if (paciente.getSexo().equals(Character.toString('F')))
            rbFemale.setSelected(true);
        else
            rbMale.setSelected(true);
    }

    private String getToggleValue() {
        return sexTogle.getSelectedToggle().getUserData().toString();
    }

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
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
            if (paciente.getFoto() != null)
                url = new URL(paciente.getFoto());
            else
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
}
