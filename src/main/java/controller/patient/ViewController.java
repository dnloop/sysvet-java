package controller.patient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Pacientes;
import utils.DialogBox;
import utils.ViewSwitcher;
import utils.routes.Route;

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

    protected static final Logger log = (Logger) LogManager.getLogger(ViewController.class);

    private Pacientes paciente;

    @FXML
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'view.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'view.fxml'.";
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'view.fxml'.";
        assert txtEspecie != null : "fx:id=\"txtEspecie\" was not injected: check your FXML file 'view.fxml'.";
        assert txtRaza != null : "fx:id=\"txtRaza\" was not injected: check your FXML file 'view.fxml'.";
        assert txtSexo != null : "fx:id=\"txtSexo\" was not injected: check your FXML file 'view.fxml'.";
        assert txtTemp != null : "fx:id=\"txtTemp\" was not injected: check your FXML file 'view.fxml'.";
        assert txtPelaje != null : "fx:id=\"txtPelaje\" was not injected: check your FXML file 'view.fxml'.";
        assert txtPeso != null : "fx:id=\"txtPeso\" was not injected: check your FXML file 'view.fxml'.";
        assert txtFechaNac != null : "fx:id=\"txtFechaNac\" was not injected: check your FXML file 'view.fxml'.";
        assert txtPropietario != null : "fx:id=\"txtPropietario\" was not injected: check your FXML file 'view.fxml'.";
        assert ivFoto != null : "fx:id=\"ivFoto\" was not injected: check your FXML file 'view.fxml'.";

        Platform.runLater(() -> {
            loadFields();
        });

        btnBack.setOnAction((event) -> {
            IndexController ic = new IndexController();
            ic.setView(Route.PACIENTE.indexView());
        });

        btnEdit.setOnAction((event) -> {
            if (paciente != null)
                displayModal(event);
            else
                DialogBox.displayWarning();
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

    private void displayModal(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        ModalDialogController mc = vs.loadModal(Route.PACIENTE.modalView(), "Editar - Paciente", event);
        vs.getStage().setOnHidden((stageEvent) -> {
            loadFields();
        });
        mc.setObject(paciente);
        mc.showModal(vs.getStage());
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
                ivFoto.setImage(image);
            }
        } catch (IOException e) {
            DialogBox.setHeader("Ruta incorrecta");
            DialogBox.setContent(e.getMessage());
            DialogBox.displayError();
            ivFoto = new ImageView("/images/DogCat.jpg");
        }
    }

    private void loadFields() {
        log.info("Loading fields");
        // required conversion for datepicker
        txtNombre.setText(paciente.getNombre());
        txtEspecie.setText(paciente.getEspecie());
        txtFechaNac.setText(paciente.getFechaNacimiento().toString());
        txtRaza.setText(paciente.getRaza());
        txtSexo.setText(paciente.getSexo());
        txtTemp.setText(paciente.getTemperamento());
        txtPelaje.setText(paciente.getPelaje());
        txtPropietario.setText(paciente.getPropietarios().toString());
        setFoto();
    }
}
