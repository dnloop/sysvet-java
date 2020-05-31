package controller.patient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Pacientes;
import utils.DialogBox;
import utils.routes.Route;
import utils.viewswitcher.ViewSwitcher;

public class ViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtEspecie;

    @FXML
    private JFXTextField txtRaza;

    @FXML
    private JFXTextField txtSexo;

    @FXML
    private JFXTextField txtTemp;

    @FXML
    private JFXTextField txtPelaje;

    @FXML
    private JFXTextField txtPeso;

    @FXML
    private JFXTextField txtFechaNac;

    @FXML
    private JFXTextField txtPropietario;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private ImageView ivFoto;

    private static final Logger log = (Logger) LogManager.getLogger(ViewController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private Pacientes paciente;

    @FXML
    void initialize() {

        btnBack.setOnAction((event) -> {
            IndexController.setView(Route.PACIENTE.indexView());
            String path[] = { "Paciente", "Índice" };
            ViewSwitcher.setPath(path);
            ViewSwitcher.loadingDialog.startTask();
        });

        btnEdit.setOnAction((event) -> {
            if (paciente != null)
                displayModal();
            else
                DialogBox.displayWarning();
        });

    }

    /*
     * Class Methods
     */

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    }

    private void displayModal() {
        ViewSwitcher.loadModal(Route.PACIENTE.modalView(), "Editar - Paciente", true);
        ModalDialogController mc = ViewSwitcher.getController(Route.PACIENTE.modalView());
        ViewSwitcher.modalStage.setOnHidden((stageEvent) -> {
            loadFields();
        });
        mc.setObject(paciente);
        ViewSwitcher.modalStage.showAndWait();
    }

    /*
     * On load checks file path inserted in the database. Warning: current file
     * could become unavailable.
     */
    private void setFoto() {
        /*
         * TODO encapsulate behavior
         */
        URL url;
        try {
            if (paciente.getFoto() != null)
                url = new URL(paciente.getFoto());
            else
                url = getClass().getResource("/images/DogCat.jpg");

            if (ImageIO.read(url) != null) {
                Image image = new Image(url.toString());
                ivFoto.setImage(image);
            }
        } catch (IOException e) {
            DialogBox.setHeader("Ruta incorrecta");
            DialogBox.setContent(e.getMessage());
            DialogBox.displayError();
            ivFoto = new ImageView("/images/DogCat.jpg");
        }
    }

    public void loadFields() {
        log.info(marker, "Loading patient's fields");
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                txtNombre.setText(paciente.getNombre());
                txtEspecie.setText(paciente.getEspecie());
                txtFechaNac.setText(paciente.getFechaNacimiento().toString());
                txtRaza.setText(paciente.getRaza());
                txtSexo.setText(paciente.getSexo());
                txtTemp.setText(paciente.getTemperamento());
                txtPelaje.setText(paciente.getPelaje());
                txtPropietario.setText(paciente.getPropietarios().toString());
                setFoto();
                return null;
            }
        };

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
