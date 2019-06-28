package controller.patient;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

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
    private JFXTextField txPropietario;

    @FXML
    private ImageView ivFoto;

    @FXML
    void initialize() {
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'view.fxml'.";
        assert txtEspecie != null : "fx:id=\"txtEspecie\" was not injected: check your FXML file 'view.fxml'.";
        assert txtRaza != null : "fx:id=\"txtRaza\" was not injected: check your FXML file 'view.fxml'.";
        assert txtSexo != null : "fx:id=\"txtSexo\" was not injected: check your FXML file 'view.fxml'.";
        assert txtTemp != null : "fx:id=\"txtTemp\" was not injected: check your FXML file 'view.fxml'.";
        assert txtPelaje != null : "fx:id=\"txtPelaje\" was not injected: check your FXML file 'view.fxml'.";
        assert txtPeso != null : "fx:id=\"txtPeso\" was not injected: check your FXML file 'view.fxml'.";
        assert txtFechaNac != null : "fx:id=\"txtFechaNac\" was not injected: check your FXML file 'view.fxml'.";
        assert txPropietario != null : "fx:id=\"txPropietario\" was not injected: check your FXML file 'view.fxml'.";
        assert ivFoto != null : "fx:id=\"ivFoto\" was not injected: check your FXML file 'view.fxml'.";

    }
}
