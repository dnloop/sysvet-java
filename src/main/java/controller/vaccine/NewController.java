package controller.vaccine;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

public class NewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<?> comboPaciente;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXTextField txtDesc;

    @FXML
    void initialize() {
        assert comboPaciente != null : "fx:id=\"comboPaciente\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDesc != null : "fx:id=\"txtDesc\" was not injected: check your FXML file 'new.fxml'.";

    }
}
