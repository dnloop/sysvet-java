package controller.patient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
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
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

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

    private static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private PacientesHome daoPA = new PacientesHome();

    private PropietariosHome daoPO = new PropietariosHome();

    private Pacientes paciente;

    private final ObservableList<Propietarios> propietariosList = FXCollections.observableArrayList();

    @FXML
    void initialize() {

        Platform.runLater(() -> loadFields()); // TODO Required to prevent NullPointer, find alternative

        btnFoto.setOnAction((event) -> {
            File file = fileChooser();
            if (file != null) {
                paciente.setFoto(file.toURI().toString());
                foto.setImage(new Image(file.toURI().toString()));
            } else
                paciente.setFoto("/images/DogCat.jpg");
        });

        btnCancel.setOnAction((event) -> {
            ViewSwitcher.modalStage.close();
        });

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
                updateRecord();
        });
    }

    /*
     * Class Methods
     */

    private void updateRecord() {
        // date conversion from LocalDate
        Date fecha = java.sql.Date.valueOf(dpFechaNac.getValue());
        paciente.setFechaNacimiento(fecha);
        paciente.setNombre(txtNombre.getText());
        paciente.setEspecie(txtEspecie.getText());
        paciente.setRaza(txtRaza.getText());
        paciente.setSexo(getToggleValue());
        paciente.setTemperamento(txtTemp.getText());
        paciente.setPropietarios(comboPropietarios.getSelectionModel().getSelectedItem());
        fecha = new Date();
        paciente.setUpdatedAt(fecha);
        if (HibernateValidator.validate(paciente)) {
            daoPA.update(paciente);
            log.info(marker, "record updated");
            DialogBox.displaySuccess();
            ViewSwitcher.modalStage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            log.error(marker, "failed to update record");
        }
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

    /*
     * On load checks file path inserted on the database. Warning: current file
     * could become unavailable.
     */
    private void setFoto() {
        /*
         * TODO encapsulate method.
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

    private void loadDao() {
        Task<List<Propietarios>> task = daoPO.displayRecords();

        task.setOnSucceeded(event -> {
            propietariosList.setAll(task.getValue());
            comboPropietarios.setItems(propietariosList);
            for (Propietarios propietario : comboPropietarios.getItems())
                if (paciente.getPropietarios().getId().equals(propietario.getId())) {
                    comboPropietarios.getSelectionModel().select(propietario);
                    break;
                }
            log.info(marker, "Table Loaded.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void loadFields() {
        log.info("Loading fields");
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
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

                comboPropietarios.setItems(propietariosList);
                for (Propietarios propietario : comboPropietarios.getItems())
                    if (paciente.getPropietarios().getId().equals(propietario.getId())) {
                        comboPropietarios.getSelectionModel().select(propietario);
                        break;
                    }

                return null;
            }
        };

        ViewSwitcher.loadingDialog.setTask(task);

        loadDao();
    }
}
